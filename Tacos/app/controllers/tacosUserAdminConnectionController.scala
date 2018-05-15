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

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserAdminConnectionController @Inject()(cc: ControllerComponents, userDAO: UserDAO) extends AbstractController(cc) {

  val title = "Intergalactic TACOS Food"

  /**
    * Call the "tacos_user_admin_connection" html template.
    */
  def tacosUserAdminConnection = Action {
    Ok(views.html.tacos_user_admin_connection(title))
  }

  /**
    * Check user/admin Connection (password)
    */
  def checkConnection = Action {
    Ok(views.html.tacos_user_admin_connection(title))
  }
}
