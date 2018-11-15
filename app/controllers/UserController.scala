package controllers

import com.google.inject.Inject
import models.Users
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, ControllerComponents}

class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = Action(Ok(Json.toJson(Users.findAll)))

  def findById(id: Long) = Action {
    val user = Users.findById(id)
    if (user == null) {
      NotFound(Json.toJson(utils.Status(success = false, message = "Not found")))
    } else {
      Ok(Json.toJson(user))
    }
  }

  def findByUserName(username: String) = Action {
    val user = Users.findByUserName(username)
    if (user == null) {
      NotFound(Json.toJson(utils.Status(success = false, message = "Not found")))
    } else {
      Ok(Json.toJson(user))
    }
  }

  def create: Action[Users] = Action(parse.json[Users]) { request =>
    val user = request.body
    val newUser = Users.create(user)
    if (newUser != null) {
      Ok(Json.toJson(newUser))
    } else {
      BadRequest(Json.toJson(utils.Status(success = false, message = "User already exist")))
    }
  }

  def login: Action[Users] = Action(parse.json[Users]) { request =>
    val body = request.body
    val user = Users.login(body)
    if (user != null) {
      Ok(Json.toJson(user))
    } else {
      val status = utils.Status(success = false, message = "Unauthorized")
      Unauthorized(Json.toJson(status))
    }
  }

}
