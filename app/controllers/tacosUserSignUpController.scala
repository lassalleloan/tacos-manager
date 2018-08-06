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
  * application's sign up page.
  */
@Singleton
class tacosUserSignUpController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"
  val regexAlpha = "[a-zA-Z]{3,}"
  val regexPhoneNumber = "([(+]*[0-9]+[()+. -]*)"
  val regexEmailAddress = "([a-z0-9][-a-z0-9_\\+\\.]*[a-z0-9])@([a-z0-9][-a-z0-9\\.]*[a-z0-9])\\.[a-z]{2,4}"
  val regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}"
  val roleUserClient = 2

  // Declare a case class that will be used in the new sign up's form
  case class SignUpRequest(firstName: String, lastName: String, phone: String, email: String, password: String, passwordConfirmation: String)

  // Create a new connection form mapping, in order to map the values of the HTML form with a Scala Form
  // Need to import "play.api.data._" and "play.api.data.Forms._"
  def signUpForm = Form(
    mapping(
      "firstName" -> text
        .verifying("Votre prénom doit être d'au moins 3 caractères et ils doivent être alphabétiques", s => s.matches(regexAlpha) && s.length <= 20),
      "lastName" -> text
        .verifying("Votre nom doit être d'au moins 3 caractères et ils doivent être alphabétiques", s => s.matches(regexAlpha) && s.length <= 20),
      "phone" -> text
        .verifying("Votre numéro de téléphone doit correspondre à un numéro de téléphone", s => s.isEmpty || s.matches(regexPhoneNumber)),
      "email" -> text
        .verifying("Votre adresse email est obligatoire et doit correspondre à une adresse email", _.matches(regexEmailAddress)),
      "password" -> text
        .verifying("Un mot de passe est obligatoire et doit correspondre aux recommandations", _.matches(regexPassword)),
      "passwordConfirmation" -> text
        .verifying("La confirmation du mot de passe est obligatoire et doit correspondre aux recommandations", _.matches(regexPassword))
    )(SignUpRequest.apply)(SignUpRequest.unapply)
  )

  def signUp = Action.async { implicit request =>
    signUpForm.bindFromRequest.fold(
      errorForm => {
        val errorList = errorForm.errors.map(f => f.message).toList
        Future.successful(Ok(views.html.tacos_user_signUp(title, errorList)))
      },
      u => {
        if (u.password != u.passwordConfirmation) {
          val error = "La confirmation du mot de passe ne correspond pas au mot de passe original"
          Future.successful(Ok(views.html.tacos_user_signUp(title, List(error))))
        } else {
          userDAO.findByEmail(u.email).map {
            case Some(_) =>
              val error = "L'adresse email est déjà associé à un compte"
              Ok(views.html.tacos_user_signUp(title, List(error)))
            case None =>
              userDAO.insert(User(None, u.firstName, u.lastName, Some(u.phone), u.email, u.password, roleUserClient))
              Redirect("/tacosUserAdminConnection")
          }
        }
      })
  }

  /**
    * Call the "tacos_user_signUp" html template.
    */
  def tacosUserSignUp = Action {
    Ok(views.html.tacos_user_signUp(title))
  }

}
