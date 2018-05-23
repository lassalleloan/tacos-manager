package controllers

//import dao.{CoursesDAO, StudentsDAO}
import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import dao.OrderDAO
import models.Order
import play.api.mvc._

import scala.concurrent.Future
//import play.api.routing.JavaScriptReverseRouter
//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosShowOrdersController @Inject()(cc: ControllerComponents, orderDAO: OrderDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"


  /**
    * Call the "tacos_home" html template.
    */
  def tacosAdminShowOrders = Action.async { implicit request =>
    /*val ordersList:Future[Seq[Order]] = orderDAO.list()

    request.session.get("connected").map { id: String =>
      println(id)
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }


    for(orders <- ordersList)yield Ok(views.html.tacos_admin_show_orders(title, orders))*/
    //request.session

    request.session.get("connected").map { id =>
      val ordersList:Future[Seq[Order]] = orderDAO.list()
      for {
        orders <- ordersList
      } yield Ok(views.html.tacos_admin_show_orders(title, orders))
    }.getOrElse {
      Future.successful(Unauthorized("Oops, you are not connected"))
    }
  }
}
