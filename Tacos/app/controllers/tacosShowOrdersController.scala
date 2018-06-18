package controllers

//import dao.{CoursesDAO, StudentsDAO}
import java.util.Calendar
import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import dao.{OrderDAO, UserDAO}
import models.Order
import models._
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

    //we get the actual date with the good format
    val cal = Calendar.getInstance()
    val day =cal.get(Calendar.DAY_OF_MONTH )
    val year =cal.get(Calendar.YEAR )
    val month =cal.get(Calendar.MONTH )+ 1
    val yearString = year.toString
    var dayString = day.toString
    var monthString = month.toString
    if(monthString.size == 1){
      monthString = "0"+monthString
    }
    if(dayString.size == 1){
      dayString = "0"+dayString
    }
    val today = yearString+"-"+monthString+"-"+dayString


    request.session.get("connected").map { id =>
      for {
          toShow <- orderDAO.showOrdersByDate(today)
      }yield Ok(views.html.tacos_admin_show_orders(title, toShow))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }


  }
}
