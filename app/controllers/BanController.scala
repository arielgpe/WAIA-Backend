package controllers

import com.google.inject.Inject
import models.Bans
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, ControllerComponents}

class BanController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = Action(Ok(Json.toJson(Bans.findAll)))

  def findById(id: Long) = Action {
    val ban = Bans.findById(id)
    if (ban == null) {
      NotFound(Json.toJson(utils.Status(success = false, message = "Not found")))
    } else {
      Ok(Json.toJson(ban))
    }
  }

  def deleteById(ipAddress: String) = Action {
    val ban = Bans.delete(ipAddress)
    Ok(Json.toJson(ban))
  }

  def create: Action[Bans] = Action(parse.json[Bans]) { request =>
    val body = request.body
    val ban = Bans.create(body)
    Ok(Json.toJson(ban))
  }


}
