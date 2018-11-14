package controllers

import com.google.inject.Inject
import models.Bans
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

class BanController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def findAll = Action {
    val bans = Bans.findAll
    Ok(Json.toJson(bans))
  }

  def findById(id: Long) = Action {
    val ban = Bans.findById(id)
    if (ban == null) {
      NotFound("Not found")
    } else {
      Ok(Json.toJson(ban))
    }
  }


}
