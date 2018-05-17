package controllers

import dao.UserDAO
import javax.inject.{Inject, Singleton}
import models.User
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserCreateAccountController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"


  // Declare a case class that will be used in the new connection's form
  case class CreationRequest(firstName: String, lastName: String, phone: String, email: String, password: String)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def creationForm = Form(
    mapping(
      "firstName" -> text,
      "lastName" -> text,
      "phone" -> text,
      "email" -> text,
      "password" -> text
    )(CreationRequest.apply)(CreationRequest.unapply)
  )

  def createAccount = Action.async { implicit request =>
    val creationRequest = creationForm.bindFromRequest.get

    val user = User(None, creationRequest.firstName, creationRequest.lastName, Some(creationRequest.phone), creationRequest.email, creationRequest.password, 2)
    val createdUser = userDAO.insert(user)

    createdUser.map(u =>
      Ok(views.html.tacos_user_admin_connection(title, u.firstName, u.lastName))
    )
  }




  /**
    * Call the "tacos_home" html template.
    */
  def tacosUserCreateAccount = Action {
    Ok(views.html.tacos_user_create_account(title))
  }
}
