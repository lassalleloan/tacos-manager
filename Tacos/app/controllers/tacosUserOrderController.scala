package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import dao._
import javax.inject.{Inject, Singleton}
import models.{Order, OrderDrink, OrderFry, OrderTacos}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's order page.
  */
@Singleton
class tacosUserOrderController @Inject()(cc: ControllerComponents, orderDAO: OrderDAO,
                                         orderFryDAO: OrderFryDAO, orderDrinkDAO: OrderDrinkDAO,
                                         orderTacosDAO: OrderTacosDAO, fryDAO: FryDAO,
                                         drinkDAO: DrinkDAO, tacosDAO: TacosDAO)
  extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  // Declare a case class that will be used in the new connection's form
  case class OrderRequest(fryId: Long, fryQuantity: Int, drinkId: Long, drinkQuantity: Int, tacosId: Long, tacosQuantity: Int)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def orderForm = Form(
    mapping(
      "fryId" -> longNumber
        .verifying("Le choix des frites incorrect", _ >= 0),
      "fryQuantity" -> number
        .verifying("La quantité de frites est incorrecte", q => q >= 0 && q <= 1000),
      "drinkId" -> longNumber
        .verifying("Le choix de la boisson incorrect", q => q >= 0 && q <= 1000),
      "drinkQuantity" -> number
        .verifying("La quantité de boisson est incorrecte", _ >= 0),
      "tacosId" -> longNumber
        .verifying("Le choix du tacos incorrect", q => q >= 0 && q <= 1000),
      "tacosQuantity" -> number
        .verifying("La quantité de tacos est incorrecte", _ >= 0),
    )(OrderRequest.apply)(OrderRequest.unapply)
  )

  def order = Action.async { implicit request =>
    val todayDate = new SimpleDateFormat("y-MM-dd").format(Calendar.getInstance().getTime)
    val todayTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime)

    request.session.get("connected").map { id =>
      orderForm.bindFromRequest.fold(
        errorForm => {
          val errorList = errorForm.errors.map(f => f.message).toList

          for {
            fries <- fryDAO.list()
            drinks <- drinkDAO.list()
            tacos <- tacosDAO.list()
          } yield Ok(views.html.tacos_user_order(title, fries, drinks, tacos, errorList))
        },
        orderForm => {
          try {
            val sumPrice = for {
              f <- fryDAO.findById(orderForm.fryId)
              d <- drinkDAO.findById(orderForm.drinkId)
              t <- tacosDAO.findById(orderForm.tacosId)
            } yield f.get.price * orderForm.fryQuantity + d.get.price * orderForm.drinkQuantity +
              t.get.price * orderForm.tacosQuantity

            sumPrice.map { priceOrder =>
              orderDAO.insert(Order(None, Some(todayDate), todayTime, priceOrder, id.toLong)).map {
                order =>
                  orderFryDAO.insert(OrderFry(order.id.get, orderForm.fryId, orderForm.fryQuantity))
                  orderDrinkDAO.insert(OrderDrink(order.id.get, orderForm.drinkId, orderForm.drinkQuantity))
                  orderTacosDAO.insert(OrderTacos(order.id.get, orderForm.tacosId, orderForm.tacosQuantity))
              }
            }
          } catch {
            case _: Throwable => Future.successful(BAD_REQUEST)
          }

          Future.successful(Redirect("/orders", MOVED_PERMANENTLY)
            .withSession("connected" -> id.toString))
        })
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }

  /**
    * Call the "tacos_user_order" html template.
    */
  def tacosUserOrder = Action.async { implicit request =>
    request.session.get("connected").map { id =>
      for {
        fries <- fryDAO.list()
        drinks <- drinkDAO.list()
        tacos <- tacosDAO.list()
      } yield Ok(views.html.tacos_user_order(title, fries, drinks, tacos))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }
}
