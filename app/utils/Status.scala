package utils

import play.api.libs.json._

case class Status(var success: Boolean = true, var message: String = "Success")

object Status{

  implicit object implicitStatusWrite extends Format[Status] {
    // convert from Person object to JSON (serializing to JSON)
    def writes(status: Status): JsValue = {
      val personSeq = Seq(
        "success" -> JsBoolean(status.success),
        "message" -> JsString(status.message)
      )
      JsObject(personSeq)
    }
    // convert from JSON string to a Person object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Status] = {
      JsSuccess(Status())
    }
  }
}