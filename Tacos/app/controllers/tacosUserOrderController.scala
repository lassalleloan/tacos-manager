package controllers

import dao.{DrinkDAO, FryDAO, TacosDAO}
import javax.inject.{Inject, Singleton}
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
class tacosUserOrderController @Inject()(cc: ControllerComponents, fryDAO: FryDAO, drinkDAO: DrinkDAO, tacosDAO: TacosDAO) extends AbstractController(cc) {

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
    orderForm.bindFromRequest.fold(
      errorForm => {
        val errorList = errorForm.errors.map(f => f.message).toList

        for {
          fries <- fryDAO.list()
          drinks <- drinkDAO.list()
          tacos <- tacosDAO.list()
        } yield Ok(views.html.tacos_user_order(title, fries, drinks, tacos, errorList))
      } ,
      o => {
        val message = "La commande est :\n" + o.fryId + " x " + o.fryQuantity + " + " + o.drinkId + " x " + o.drinkQuantity + " + " + o.tacosId + " x " + o.tacosQuantity
          Future.successful(Ok(views.html.tacos_user_order(title, errorList = List(message))))
      })
  }

  /**
    * Call the "tacos_user_order" html template.
    */
  def tacosUserOrder = Action.async { implicit request =>
    // Wait for the promises to resolve, then return the list of students and courses.
    for {
      fries <- fryDAO.list()
      drinks <- drinkDAO.list()
      tacos <- tacosDAO.list()
    } yield Ok(views.html.tacos_user_order(title, fries, drinks, tacos))
  }
}
