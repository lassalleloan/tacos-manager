package controllers

import javax.inject._
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosHomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
      )
    ).as("text/javascript")
  }

  /**
    * Call the "tacos_home" html template.
    */
  def tacosHome = Action {
    Ok(views.html.tacos_home(title))
  }



  /**
    * logout : termintae the session and goes home
    */
  def logout = Action {
    Ok(views.html.tacos_home(title)).withNewSession
  }

}
