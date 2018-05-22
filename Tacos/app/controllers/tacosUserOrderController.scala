package controllers

import dao.DrinkDAO
import javax.inject._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserOrderController @Inject()(cc: ControllerComponents, drinkDAO: DrinkDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"


  /**
    * Call the "tacos_user_order" html template.
    */
  def tacosUserOrder = Action {
    Ok(views.html.tacos_user_order(title))
  }
}
