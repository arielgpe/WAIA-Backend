package models

import java.util
import java.util.Calendar

import io.ebean.annotation.{CreatedTimestamp, NotNull}
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Entity, Id, ManyToOne}

@Entity
case class Posts() extends Model {
  @Id val id: Long = 0
  var author: String = ""
  @NotNull var content: String = ""
  var ipAddress: String = ""
  @ManyToOne var user: Users = _
  @CreatedTimestamp var createdTimestamp: Long = Calendar.getInstance.getTimeInMillis
}

object Posts {

  private def find: Query[Posts] = Ebean.find(classOf[Posts])

  def findAll: util.List[Posts] = find.findList

  def findById(id: Long): Posts = find.where.idEq(id).findOne

  def create(content: String, ipAddress: String = "0.0.0.0", userId: Long = 0): Posts = {
    val author = "Anon"
    val post = new Posts()
    post.author = author
    post.content = content
    post.ipAddress = ipAddress
    if (userId != 0 && Users.findById(userId) != null) {
      post.user = Users.findById(userId)
    }
    post
  }
}