package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserCreateAccountController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  /**
    * Call the "tacos_home" html template.
    */
  def tacosUserCreateAccount = Action {
    Ok(views.html.tacos_user_create_account(title))
  }
}
