package controllers

import dao.UserDAO
import javax.inject.{Inject, Singleton}
import models.User
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserCreateAccountController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  // Convert a User-model object into a JsValue representation, which means that we serialize it into JSON.
  //  implicit val userToJson: Writes[User] = (
  //    (JsPath \ "id").write[Option[Long]] and
  //      (JsPath \ "firstName").write[String] and
  //      (JsPath \ "lastName").write[String] and
  //      (JsPath \ "phone").write[Option[String]] and
  //      (JsPath \ "email").write[String] and
  //      (JsPath \ "password").write[String]
  //    // Use the default 'unapply' method (which acts like a reverted constructor) of the User case class if order to get
  //    // back the User object's arguments and pass them to the JsValue.
  //    )(unlift(User.unapply))

  // Convert a JsValue representation into a User-model object, which means that we deserialize the JSON.
  //  implicit val jsonToUser: Reads[User] = (
  // In order to be valid, the user must have first and last names that are 2 characters long at least, as well as
  // an age that is greater than 0.
  //    (JsPath \ "id").readNullable[Long] and
  //      (JsPath \ "firstName").read[String]and
  //      (JsPath \ "lastName").read[String] and
  //      (JsPath \ "phone").readNullable[String] and
  //      (JsPath \ "email").read[String] and
  //      (JsPath \ "password").read[String]
  //    // Use the default 'apply' method (which acts like a constructor) of the User case class with the JsValue in order
  //    // to construct a User object from it.
  //    )(User.apply _)

  /**
    * This helper parses and validates JSON using the implicit `jsonToStudent` above, returning errors if the parsed
    * json fails validation.
    */
  //  def validateJson[A : Reads] = parse.json.validate(
  //    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  //  )


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
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> u.id,
          "message" -> ("Student '" + u.firstName + " " + u.lastName + "' saved.")
        )
      )
    )
  }

  //  def createAccount = Action.async(validateJson[User]) { implicit request =>
  //    // `request.body` contains a fully validated `User` instance, since it has been validated by the `validateJson`
  //    // helper above.
  //    val user = request.body
  //    val createdUser = userDAO.insert(user)
  //
  //    createdUser.map(u =>
  //      Ok(
  //        Json.obj(
  //          "status" -> "OK",
  //          "id" -> u.id,
  //          "message" -> ("User '" + u.firstName + " " + u.lastName + "' saved.")
  //        )
  //      )
  //    )
  //  }

  /**
    * Call the "tacos_home" html template.
    */
  def tacosUserCreateAccount = Action {
    Ok(views.html.tacos_user_create_account(title))
  }
}
