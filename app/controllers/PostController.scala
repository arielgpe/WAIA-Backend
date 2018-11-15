package controllers

import javax.inject.Inject
import models.Posts
import play.api.libs.json.Json
import play.api.mvc._


class PostController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = Action(Ok(Json.toJson(Posts.findAll)))

  def findById(id: Long) = Action {
    val post = Posts.findById(id)
    if (post == null) {
      NotFound(Json.toJson(utils.Status(success = false, message = "Not found")))
    } else {
      Ok(Json.toJson(post))
    }
  }

  def create: Action[Posts] = Action(parse.json[Posts]) { request =>
    val body = request.body
    body.ipAddress = request.remoteAddress
    Ok(Json.toJson(Posts.create(body)))
  }

}
