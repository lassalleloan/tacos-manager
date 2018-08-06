package controllers

import java.text.SimpleDateFormat
import java.util.Calendar

import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import dao.{OrderDAO, UserDAO}
import play.api.mvc._

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosShowOrdersController @Inject()(cc: ControllerComponents, orderDAO: OrderDAO, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"


  /**
    * show the orders for today
    */
  def tacosAdminShowOrders = Action.async { implicit request =>

    // we get the actual date with the good format
    val today = new SimpleDateFormat("y-MM-dd").format(Calendar.getInstance().getTime)

    request.session.get("connected").map { id =>
      for {
          toShow <- orderDAO.showOrdersByDate(today)
      } yield Ok(views.html.tacos_admin_show_orders(title, toShow))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }

}
