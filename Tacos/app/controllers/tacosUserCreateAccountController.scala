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
  case class SignUpRequest(firstName: String, lastName: String, phone: String, email: String, password: String, confirmPassword: String)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def signUpForm = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "phone" -> text,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "confirmPassword" -> nonEmptyText,
    )(SignUpRequest.apply)(SignUpRequest.unapply)
  )

  def createAccount = Action.async { implicit request =>
    signUpForm.bindFromRequest.fold(
      errorForm => {
        val error = "Un ou plusieurs champs obligatoire sont vide"
        Future.successful(Ok(views.html.tacos_user_create_account(title, error)))
      },
      u => {
        if (u.password != u.confirmPassword) {
          val error = "La confirmation du mot de passe ne correspond pas au mot de passe original"
          Future.successful(Ok(views.html.tacos_user_create_account(title, error)))
        } else {
          userDAO.findByEmail(u.email).map {
            case Some(_) =>
              val error = "L'adresse email est déjà associé à un compte"
              Ok(views.html.tacos_user_create_account(title, error))
            case None =>
              val user = User(None, u.firstName, u.lastName, Some(u.phone), u.email, u.password, roleUserClient)
              userDAO.insert(user)
              Redirect("/tacosUserAdminConnection")
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
