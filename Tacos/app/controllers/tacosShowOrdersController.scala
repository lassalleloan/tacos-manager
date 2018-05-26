package controllers

//import dao.{CoursesDAO, StudentsDAO}
import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import dao.{OrderDAO, UserDAO}
import models.Order
import models.User
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

    request.session.get("connected").map { id =>
      val ordersList:Future[Seq[Order]] = orderDAO.list()
      //val tuplesOrdersClients = ordersList.map(order => (order, userDAO.findById(order)))
      //val usersList:Future[Seq[User]] = userDAO.list()


      for {
        orders <- ordersList
      } yield Ok(views.html.tacos_admin_show_orders(title, orders))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }
  }
}
