package controllers

import com.google.inject.Inject
import models.Users
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = Action {
    val users = Users.findAll
    Ok(Json.toJson(users))
  }

  def findById(id: Long) = Action {
    val user = Users.findById(id)
    if (user == null) {
      NotFound("Not found")
    } else {
      Ok(Json.toJson(user))
    }
  }

  def findByUserName(username: String) = Action {
    val user = Users.findByUserName(username)
    if (user == null) {
      NotFound("Not found")
    } else {
      Ok(Json.toJson(user))
    }
  }

  def create = Action { request =>
    val body = request.body.asJson
    val username = body.get("username").as[String]
    val password = body.get("password").as[String]
    val user = Users.create(username, password)
    if (user != null) {
      Ok(Json.toJson(user))
    } else {
      val status = utils.Status(success = false, message = "User already exist")
      BadRequest(Json.toJson(status))
    }
  }

  def login = Action { request =>
    val body = request.body.asJson
    val username = body.get("username").as[String]
    val password = body.get("password").as[String]
    val user = Users.login(username, password)
    if (user != null) {
      Ok(Json.toJson(user))
    } else {
      val status = utils.Status(success = false, message = "Unauthorized")
      Unauthorized(Json.toJson(status))
    }
  }

}
