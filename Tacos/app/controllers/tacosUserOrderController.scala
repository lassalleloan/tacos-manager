package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import dao._
import javax.inject.{Inject, Singleton}
import models.{Order, OrderDrink, OrderFry}
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
            orders <- orderDAO.findByIdUserPerDay(id.toLong, todayDate)
          } yield Ok(views.html.tacos_user_order(title, orders.zipWithIndex, fries, drinks, tacos, errorList))
        },
        orderForm => {
          try {
            val sumPrice = for {
              f <- fryDAO.findById(orderForm.fryId)
              d <- drinkDAO.findById(orderForm.drinkId)
              t <- tacosDAO.findById(orderForm.tacosId)
            } yield f.get.price + d.get.price + t.get.price

            sumPrice.map {x =>
              orderDAO.insert(Order(None, Some(todayDate), todayTime, x, id.toLong))
            }
          } catch {
            case _: Throwable => Future.successful(BAD_REQUEST)
          }

          for {
            fries <- fryDAO.list()
            drinks <- drinkDAO.list()
            tacos <- tacosDAO.list()
            orders <- orderDAO.findByIdUserPerDay(id.toLong, todayDate, todayTime.substring(0, 2))
          } yield Ok(views.html.tacos_user_order(title, orders.zipWithIndex, fries, drinks, tacos))
        })
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }

  /**
    * Call the "tacos_user_order" html template.
    */
  def tacosUserOrder = Action.async { implicit request =>
    val todayDate = new SimpleDateFormat("y-MM-dd").format(Calendar.getInstance().getTime)
    val todayTime = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime)

    request.session.get("connected").map { id =>
      for {
        fries <- fryDAO.list()
        drinks <- drinkDAO.list()
        tacos <- tacosDAO.list()
        orders <- orderDAO.findByIdUserPerDay(id.toLong, todayDate, todayTime)
      } yield Ok(views.html.tacos_user_order(title, orders.zipWithIndex, fries, drinks, tacos))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }
}
