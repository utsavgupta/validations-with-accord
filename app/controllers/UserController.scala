package controllers

/**
  * Created by utsav on 13/5/17.
  */

import akka.actor.{ActorSystem, Props}
import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import akka.pattern.ask
import akka.util.Timeout
import services.{ServiceResponse, UserServices, ValidationError, InternalError}
import services.UserServices._
import jsonrw.JsUser._
import models.User

import scala.concurrent.duration._

@Singleton
class UserController @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  val userServices = actorSystem.actorOf(Props[UserServices], "user-services")
  implicit val timeout: Timeout = 1.second

  def getUsers() = Action.async { request =>
    (userServices ? GetUsersRequest()).mapTo[GetUsersResponse].map { response =>
      Ok(Json.toJson(response.users))
    }
  }

  def getUser(userId: String) = Action.async { request =>
    (userServices ? GetUserRequest(userId)).mapTo[GetUserResponse].map { response =>
      Ok(Json.toJson(response.user))
    }
  }

  def createUser() = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest(Json.toJson("bad request"))),
      user => (userServices ? CreateUserRequest(user)).mapTo[ServiceResponse].map {
        case createUserResponse : CreateUserResponse => Created(Json.toJson(createUserResponse.user))
        case validationError : ValidationError => Conflict(Json.toJson(validationError.messages))
        case internalError : InternalError => InternalServerError(Json.toJson(internalError.messages))
      }
    )
  }

  def updateUser(userId: String) = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest(Json.toJson("bad request"))),
      user => (userServices ? UpdateUserRequest(user copy (id = Some(userId))))
        .mapTo[ServiceResponse].map {
        case updateUserResponse : UpdateUserResponse => Ok(Json.toJson(updateUserResponse.status))
        case validationError : ValidationError => Conflict(Json.toJson(validationError.messages))
        case internalError : InternalError => InternalServerError(Json.toJson(internalError.messages))
      }
    )
  }

  def deleteUser(userId: String) = Action.async { request =>
    (userServices ? DeleteUserRequest(userId)).mapTo[DeleteUserResponse].map { response =>
      Ok(Json.toJson(response.status))
    }
  }
}
