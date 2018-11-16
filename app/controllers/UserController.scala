package controllers

import actions.AuthAction
import com.google.inject.Inject
import models.Users
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents}

class UserController @Inject()(authAction: AuthAction, cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = authAction(Ok(Json.toJson(Users.findAll)))

  def findById(id: Long) = authAction {
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

  def findTokens(id: Long) = Action {
    val tokens = Users.findTokens(id)
    Ok(Json.toJson(tokens))
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
      val jsObject = JsObject(Seq(
        "id" -> JsNumber(user.id),
        "token" -> JsString(user.token),
        "tls" -> JsNumber(user.tls),
        "created_at" -> JsNumber(user.createdTimestamp),
        "user" -> Json.toJson(user.user)
      ))
      Ok(jsObject)
    } else {
      val status = utils.Status(success = false, message = "Unauthorized")
      Unauthorized(Json.toJson(status))
    }
  }

}
