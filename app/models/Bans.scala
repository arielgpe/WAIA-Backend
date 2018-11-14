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
  @NotNull var duration: Long = 0
  @NotNull var reason: String = ""
  @CreatedTimestamp var createdTimestamp: Long = Calendar.getInstance.getTimeInMillis
}

object Bans {

  private def find: Query[Bans] = Ebean.find(classOf[Bans])

  def findAll: List[Bans] = find.findList.asScala.toList

  def findById(id: Long): Bans = find.where.idEq(id).findOne

  def findByIpAddress(ipAddress: String): Bans = find.where.eq("ipAddress", ipAddress).findOne

  def create(ipAddress: String, duration: Long, reason: String): Bans = {
    val existingBan = findByIpAddress(ipAddress)
    if (existingBan != null) {
      existingBan.duration = duration
      existingBan.reason = reason
      existingBan.update()
      existingBan
    } else {
      val ban = new Bans()
      ban.ipAddress = ipAddress
      ban.duration = duration
      ban.reason = reason
      ban.save()
      ban
    }
  }


  def delete(ipAddress: String): Status = {
    val ban = findByIpAddress(ipAddress)
    val status = new Status
    if (ban != null) {
      ban.delete()
    } else {
      status.success = false
      status.message = "No ban was found"
    }

    status
  }

  implicit object implicitBanWrite extends Format[Bans] {
    // convert from Person object to JSON (serializing to JSON)
    def writes(ban: Bans): JsValue = {
      val personSeq = Seq(
        "id" -> JsString(ban.id.toString),
        "ipAddress" -> JsString(ban.ipAddress),
        "reason" -> JsString(ban.reason),
        "duration" -> JsNumber(ban.duration)
      )
      JsObject(personSeq)
    }
    // convert from JSON string to a Person object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Bans] = {
      JsSuccess(Bans())
    }
  }
}