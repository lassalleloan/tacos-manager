package controllers

import dao.UserDAO
import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserCreateAccountController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  // Refer to the StudentsController class in order to have more explanations.
  implicit val userToJson: Writes[User] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "phone").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "userRole").write[Long]
    )(unlift(User.unapply))

  implicit val jsonToUser: Reads[User] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "firstName").read[String](minLength[String](3)) and
      (JsPath \ "lastName").read[String](minLength[String](3)) and
      (JsPath \ "phone").read[String] and
      (JsPath \ "email").read[String](minLength[String](3)) and
      (JsPath \ "password").read[String](minLength[String](3)) and
      (JsPath \ "userRole").read[Long]
    )(User.apply _)

  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * Call the "tacos_home" html template.
    */
  def tacosUserCreateAccount = Action {
    Ok(views.html.tacos_user_create_account(title))
  }

  def createUser = Action.async(validateJson[User]) { request =>
    val user = request.body
    val createdUser = userDAO.insert(user)

    createdUser.map(c =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> c.id,
          "message" -> ("User '" + c.firstName + "' saved.")
        )
      )
    )
  }
}
