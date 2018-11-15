package models

import java.util.Calendar

import io.ebean.annotation.{CreatedTimestamp, NotNull}
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Entity, Id, ManyToOne}
import play.api.libs.json
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
    override def writes(posts: Posts): JsValue = {
      var postsSeq = Seq(
        "id" -> JsNumber(posts.id),
        "author" -> JsString(posts.author),
        "content" -> JsString(posts.content)
      )

      if (posts.user != null) {
        postsSeq = postsSeq :+ ("user" -> json.JsObject(Seq(
          "id" -> JsNumber(posts.user.id),
          "name" -> JsString(posts.user.username)
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