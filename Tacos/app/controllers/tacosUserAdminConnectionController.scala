package controllers

import dao.UserDAO
import javax.inject.{Inject, Singleton}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserAdminConnectionController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"


  // Declare a case class that will be used in the new connection's form
  case class ConnectionRequest(email: String, password: String)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def connectionForm = Form(
    mapping(
      "email" -> text,
      "password" -> text
    )(ConnectionRequest.apply)(ConnectionRequest.unapply)
  )

  /**
    * Called when the user try to post a new connection from the view.
    * See https://scalaplayschool.wordpress.com/2014/08/14/lesson-4-handling-form-data-with-play-forms/ for more information
    */
  def checkConnection = Action.async { implicit request =>
    val connectionRequest = connectionForm.bindFromRequest.get

    val optionalUser = userDAO.findByEmail(connectionRequest.email)

    optionalUser.map{
      //si un user qui correspond au email a été trouvé
      case Some(u) =>
        //si le mot de passe correspond
        if(u.password.equals(connectionRequest.password)){
          //s'il s'agit d'un admin
          if(u.roleUser == 1){
            Ok(views.html.tacos_admin_show_orders(title))
          }
          //s'il s'agit d'un user
          else{
            Ok(views.html.tacos_user_order(title))
          }
        }
        //si le mot de passe ne correspond pas
        else{
          Ok(views.html.tacos_user_admin_connection(title))
        }
      //si un user qui correspond au email n'a pas été trouvé
      case None => Ok(views.html.tacos_user_admin_connection(title))
    }
  }

  /**
    * Call the "tacos_user_admin_connection" html template.
    */
  def tacosUserAdminConnection = Action {
    Ok(views.html.tacos_user_admin_connection(title))
  }


}
