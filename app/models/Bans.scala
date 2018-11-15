package models

import java.util.Calendar

import io.ebean.annotation.{CreatedTimestamp, NotNull}
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Column, Entity, Id}
import play.api.libs.json._
import utils.Status

import scala.collection.JavaConverters._


@Entity
case class Bans() extends Model {
  @Id val id: Long = 0
  @NotNull @Column(unique = true) var ipAddress: String = ""
  @NotNull var fromDate: Long = 0
  @NotNull var toDate: Long = 0
  var reason: String = ""
  @CreatedTimestamp var createdTimestamp: Long = Calendar.getInstance.getTimeInMillis
}

object Bans {

  private def find: Query[Bans] = Ebean.find(classOf[Bans])

  def findAll: List[Bans] = find.findList.asScala.toList

  def findById(id: Long): Bans = find.where.idEq(id).findOne

  def findByIpAddress(ipAddress: String): Bans = find.where.eq("ipAddress", ipAddress).findOne

  def create(ban: Bans): Bans = {
    val existingBan = findByIpAddress(ban.ipAddress)
    if (existingBan != null) {
      existingBan.fromDate = ban.fromDate
      existingBan.toDate = ban.toDate
      existingBan.reason = ban.reason
      existingBan.update()
      existingBan
    } else {
      ban.save()
      ban
    }
  }


  def delete(ipAddress: String): Status = {
    val ban = findByIpAddress(ipAddress)
    val status = new Status(message = "Ban successfully deleted")
    if (ban != null) {
      ban.delete()
    } else {
      status.success = false
      status.message = "No ban was found"
    }

    status
  }

  implicit object implicitBanWrite extends Format[Bans] {
    def writes(ban: Bans): JsValue = {
      val personSeq = Seq(
        "id" -> JsString(ban.id.toString),
        "ipAddress" -> JsString(ban.ipAddress),
        "reason" -> JsString(ban.reason),
        "from" -> JsNumber(ban.fromDate),
        "to" -> JsNumber(ban.toDate)
      )
      JsObject(personSeq)
    }
    def reads(json: JsValue): JsResult[Bans] = {
      val ban = new Bans
      val body = json.result.as[JsObject]
      for (elem <- body.fields) {
        elem._1 match {
          case "ipAddress" => ban.ipAddress = elem._2.as[String]
          case "reason" => ban.reason = elem._2.as[String]
          case "from" => ban.fromDate = elem._2.as[Long]
          case "to" => ban.toDate = elem._2.as[Long]
          case _ => ""
        }
      }
      JsSuccess(ban)
    }
  }
}