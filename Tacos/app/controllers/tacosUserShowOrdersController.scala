package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import dao._
import javax.inject.{Inject, Singleton}
import models.Order
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
class tacosUserShowOrdersController @Inject()(cc: ControllerComponents, orderDAO: OrderDAO)
  extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  /**
    * Call the "tacos_user_show_orders" html template.
    */
  def tacosUserShowOrders = Action.async { implicit request =>
    val todayDate = new SimpleDateFormat("y-MM-dd").format(Calendar.getInstance().getTime)
    val todayTime = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime)

    request.session.get("connected").map { id =>
      for {
        orders <- orderDAO.showOrdersByIdUserPerDay(id.toLong, "2018-05-09", "10")
      } yield Ok(views.html.tacos_user_show_orders(title, orders.zipWithIndex))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }
}
