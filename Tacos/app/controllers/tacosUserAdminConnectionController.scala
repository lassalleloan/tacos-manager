package controllers

//import dao.{CoursesDAO, StudentsDAO}
import javax.inject._

import dao.UserDAO
import models.User
import play.api.libs.json.Json
import play.api.mvc._
import dao.UserDAO
import javax.inject.{Inject, Singleton}

import models.User
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.routing.JavaScriptReverseRouter
import java.lang.Object
import play.api.data._
import play.api.data.Forms._

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
  def checkConnection = Action { implicit request =>
    val connectionRequest = connectionForm.bindFromRequest.get
    // Just display the entered values
    Ok(s"email: '${connectionRequest.email}', password: '${connectionRequest.password}'")
  }




  /**
    * Call the "tacos_user_admin_connection" html template.
    */
  def tacosUserAdminConnection = Action {
    Ok(views.html.tacos_user_admin_connection(title))
  }


}
