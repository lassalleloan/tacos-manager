package controllers

import dao.UserDAO
import javax.inject.{Inject, Singleton}
import models.User
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserCreateAccountController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"
  val roleUserClient = 2

  // Declare a case class that will be used in the new connection's form
  case class CreationRequest(firstName: String, lastName: String, phone: String, email: String, password: String, confirmPassword: String)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def creationForm = Form(
    mapping(
      "firstName" -> text,
      "lastName" -> text,
      "phone" -> text,
      "email" -> text,
      "password" -> text,
      "confirmPassword" -> text
    )(CreationRequest.apply)(CreationRequest.unapply)
  )

  def createAccount = Action.async { implicit request =>
    creationForm.bindFromRequest.fold(
      error => Future.successful(Ok(views.html.tacos_user_create_account(title, "Une erreur est survenue. Veuillez réessayer plus tard."))),
      u => {
        if (u.firstName.isEmpty || u.lastName.isEmpty || u.email.isEmpty || u.password.isEmpty || u.confirmPassword.isEmpty) {
          Future.successful(Ok(views.html.tacos_user_create_account(title, "Certains champs sont vides.")))
        } else if (u.password != u.confirmPassword) {
          Future.successful(Ok(views.html.tacos_user_create_account(title, "La confirmation du mot de passe ne correspond pas au mot de passe original.")))
        } else {
          userDAO.findByEmail(u.email).map {
            case Some(_) =>
              Ok(views.html.tacos_user_create_account(title, "L'adresse email est déjà associé à un compte."))
            case None =>
              val user = User(None, u.firstName, u.lastName, Some(u.phone), u.email, u.password, roleUserClient)
              userDAO.insert(user)
              Ok(views.html.tacos_user_admin_connection(title, user.firstName, user.lastName))
          }
        }
      })
  }

  /**
    * Call the "tacos_user_create_account" html template.
    */
  def tacosUserCreateAccount = Action {
    Ok(views.html.tacos_user_create_account(title))
  }

}
