package actions

import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParser, Request, Result}
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

case class Authorization[A](action: Action[A]) extends Action[A]{

  override def apply(request: Request[A]): Future[Result] = {
    request.headers.get(HeaderNames.AUTHORIZATION) match {
      case None =>
        Future.successful(
          Forbidden(Json.toJson(utils.Status(success = false, message = "Unauthorized")))
        )
      case Some(value) =>  action(request)

    }

  }

  override def parser: BodyParser[A] = action.parser

  override def executionContext: ExecutionContext = action.executionContext
}
