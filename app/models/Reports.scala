package models

import io.ebean.annotation.NotNull
import io.ebean.{Ebean, Model, Query}
import javax.persistence.{Entity, Id, ManyToOne}
import play.api.libs.json._

import scala.collection.JavaConverters._



@Entity
case class Reports() extends Model {
  @Id val id: Long = 0
  @NotNull var reason: String = ""
  var ipAddress: String = ""
  @ManyToOne var post: Posts = _
}

object Reports {

  private def find: Query[Reports] = Ebean.find(classOf[Reports])

  def findAll: List[Reports] = find.findList().asScala.toList

  def findById(id: Long): Reports = find.where.idEq(id).findOne()

  def findByPost(post: Posts): List[Reports] =
    find.where.eq("post", post).findList().asScala.toList

  def create(report: Reports): Reports = {
    report.save()
    report
  }

  def countByPost(post: Posts): Int = find.where.eq("post", post).findCount()

  implicit object implicitReportWrite extends Format[Reports] {
    override def writes(report: Reports): JsValue = {
      val reportSeq = Seq(
        "id" -> JsString(report.id.toString),
        "reason" -> JsString(report.reason),
        "ipAddress" -> JsString(report.ipAddress)
      )
      JsObject(reportSeq)
    }
    override def reads(json: JsValue): JsResult[Reports] = {
      val report = new Reports
      val body = json.result.as[JsObject]
      for (elem <- body.fields) {
        elem._1 match {
          case "reason" => report.reason = elem._2.as[String]
          case "ipAddress" => report.ipAddress = elem._2.as[String]
          case "post_id" =>
            report.post = Posts.findById(elem._2.as[Long])
          case "" => ""
        }
      }

      JsSuccess(report)
    }
  }
}