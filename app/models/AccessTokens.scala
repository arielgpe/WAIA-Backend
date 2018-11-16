package models

import java.util.{Calendar, UUID}

import io.ebean.{Ebean, Model, Query}
import io.ebean.annotation.{CreatedTimestamp, NotNull}
import javax.persistence.{Entity, Id, ManyToOne}
import play.api.libs.json._
import utils.Status

import scala.collection.JavaConverters._


@Entity
case class AccessTokens() extends Model{
  @Id val id: Long = 0
  @NotNull val token: String = UUID.randomUUID.toString
  @NotNull val tls: Double = Calendar.getInstance.getTimeInMillis + 1.21e+9
  @ManyToOne var user: Users = _
  @CreatedTimestamp val createdTimestamp: Long = Calendar.getInstance.getTimeInMillis
}

object AccessTokens {

  private def find: Query[AccessTokens] = Ebean.find(classOf[AccessTokens])

  def findAll: List[AccessTokens] = find.findList.asScala.toList

  def findById(id: Long): AccessTokens = find.where.idEq(id).findOne

  def findByToken(token: String): AccessTokens = find.where.eq("token", token).findOne()

  def findByUser(user: Users): List[AccessTokens] =
    find.where.eq("user", user).findList.asScala.toList

  def create(user: Users): AccessTokens = {
    val accessToken = new AccessTokens
    accessToken.user = user
    accessToken.save()
    accessToken
  }

  def delete(token: String): Status = {
    val accessToken = findByToken(token)
    accessToken.delete()
    Status(message = "Token deleted")
  }

  implicit object implicitTokenWrite extends Format[AccessTokens] {
    override def writes(token: AccessTokens): JsValue = {
      val tokenSeq = Seq(
        "id" -> JsNumber(token.id),
        "token" -> JsString(token.token),
        "tls" -> JsNumber(token.tls),
        "created_at" -> JsNumber(token.createdTimestamp)
      )
      JsObject(tokenSeq)
    }

    override def reads(json: JsValue): JsResult[AccessTokens] = JsSuccess(AccessTokens())
  }
}