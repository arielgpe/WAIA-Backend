package models

import java.util.Calendar

import io.ebean.annotation.{CreatedTimestamp, NotNull}
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Entity, Id, ManyToOne}
import play.api.libs.json._

import scala.collection.JavaConverters._

@Entity
case class Posts() extends Model {
  @Id val id: Long = 0
  var author: String = "Anon"
  @NotNull var content: String = ""
  var ipAddress: String = ""
  @ManyToOne var user: Users = _
  @CreatedTimestamp var createdTimestamp: Long = Calendar.getInstance.getTimeInMillis
}

object Posts {

  private def find: Query[Posts] = Ebean.find(classOf[Posts])

  def findAll: List[Posts] = find.findList.asScala.toList

  def findById(id: Long): Posts = find.where.idEq(id).findOne

  def create(post: Posts): Posts = {
    post.save()
    post
  }

  implicit object implicitPostWrite extends Format[Posts] {
    override def writes(post: Posts): JsValue = {
      var postsSeq = Seq(
        "id" -> JsNumber(post.id),
        "author" -> JsString(post.author),
        "content" -> JsString(post.content),
        "reports" -> JsNumber(Reports.countByPost(post))
      )

      if (post.user != null) {
        postsSeq = postsSeq :+ ("user" -> JsObject(Seq(
          "id" -> JsNumber(post.user.id),
          "name" -> JsString(post.user.username)
        )))
      }

      JsObject(postsSeq)
    }

    override def reads(json: JsValue): JsResult[Posts] = {
      val post = new Posts
      val body = json.result.as[JsObject]
      for (elem <- body.fields) {
        elem._1 match {
          case "content" => post.content = elem._2.as[String]
          case "user_id" =>
            post.user = Users.findById(elem._2.as[Long])
            post.author = post.user.username
          case _ => ""
        }
      }
      JsSuccess(post)
    }

  }

}