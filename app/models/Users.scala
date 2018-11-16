package models

import java.util.Calendar

import io.ebean.annotation.{CreatedTimestamp, NotNull}
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Column, Entity, Id}
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json
import play.api.libs.json._
import utils.Status

import scala.collection.JavaConverters._

@Entity
case class Users() extends Model {
  @Id val id: Long = 0
  @Column(unique = true) var username: String = ""
  @NotNull var password: String = ""
  var active: Boolean = true
  val role: String = "ADMIN"
  @CreatedTimestamp val createdAt: Long = Calendar.getInstance.getTimeInMillis
}

object Users {

  private def find: Query[Users] = Ebean.find(classOf[Users])

  def findAll: List[Users] = find.findList.asScala.toList

  def findById(id: Long): Users = find.where.idEq(id).findOne

  def findByUserName(username: String): Users =
    find.where.eq("username", username).findOne

  def findTokens(id: Long): List[AccessTokens] = AccessTokens.findByUser(findById(id))

  def login(user: Users): AccessTokens = {
    val found = findByUserName(user.username)
    if (found != null && BCrypt.checkpw(user.password, found.password)) {
      val token = AccessTokens.create(found)
      return token
    }
    null
  }

  def create(user: Users): Users = {
    val existingUser = find.where.eq("username", user.username).findOne()
    if (existingUser != null) {
      println("what")
      return null
    }

    user.password = BCrypt.hashpw(user.password, BCrypt.gensalt(12))
    user.save()
    user
  }

  def changeStatus(id: Long, activate: Boolean): Status = {
    val user = findById(id)
    val status = new Status
    if (user != null) {
      user.active = activate
      user.update()
      status.message = if (activate) "User activated" else "User deactivated"
    } else {
      status.success = false
      status.message = "User not found"
    }

    status
  }

  def changePassword(id: Long, password: String): Status = {
    val user = findById(id)
    val status = new Status
    if (user != null) {
      user.password = BCrypt.hashpw(password, BCrypt.gensalt(12))
      user.update()
      status.message = "Password successfully changed"
    } else {
      status.success = false
      status.message = "User not found"
    }

    status
  }


  implicit object implicitUserWrite extends Format[Users] {
    def writes(users: Users): JsValue = {
      val usersSeq = Seq(
        "id" -> JsNumber(users.id),
        "username" -> JsString(users.username),
        "active" -> JsBoolean(users.active),
        "role" -> JsString(users.role),
        "created_at" -> JsNumber(users.createdAt)
      )
      JsObject(usersSeq)
    }
    def reads(json: JsValue): JsResult[Users] = {
      val user = new Users
      val body = json.result.as[JsObject]
      for (elem <- body.fields){
        elem._1 match {
          case "username" => user.username = elem._2.as[String]
          case "password" => user.password = elem._2.as[String]
          case "active" => user.active = elem._2.as[Boolean]
          case _ => ""
        }
      }
      JsSuccess(user)
    }
  }
}

