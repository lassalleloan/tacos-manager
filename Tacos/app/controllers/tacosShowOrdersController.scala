package controllers

//import dao.{CoursesDAO, StudentsDAO}
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

    request.session.get("connected").map { id =>

      //will keep all the orders to display
      val ordersToShowList: List[OrderToShow] = List()

      //we get all the orders in the database. We have to joins various tables to build an order that can be properly displayed (called OrdersToShow)
      val ordersList:Future[Seq[Order]] = orderDAO.list()
      //we get all the users who made an order
      val futurUsersList = ordersList.map(x => x.map(order => (userDAO.findById(order.person), order.hourOrder)))


      Future.successful(Ok(views.html.tacos_admin_show_orders(title, ordersToShowList)))
    }.getOrElse {
      Future.successful(Unauthorized("Il faut vous connecter d'abord pour accéder à cette page."))
    }


  }
}
