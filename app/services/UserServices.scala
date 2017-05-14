package services

import akka.actor.{Actor, ActorRef}
import com.wix.accord.Descriptions.Explicit
import models.User

import scala.concurrent.ExecutionContext.Implicits.global
import com.wix.accord._

import scala.concurrent.Future


/**
  * Created by utsav on 13/5/17.
  */

object UserServices {
  case class GetUserRequest(userId: String)
  case class GetUserResponse(user: Option[User]) extends ServiceResponse

  case class GetUsersRequest()
  case class GetUsersResponse(users: List[User]) extends ServiceResponse

  case class CreateUserRequest(user: User)
  case class CreateUserResponse(user: User) extends ServiceResponse

  case class UpdateUserRequest(user: User)
  case class UpdateUserResponse(status: Boolean) extends ServiceResponse

  case class DeleteUserRequest(userId: String)
  case class DeleteUserResponse(status: Boolean) extends ServiceResponse
}

class UserServices extends Actor {
  import services.UserServices._
  import validators.UserValidators._

  override def receive = {
    case getUsersRequest : GetUsersRequest => getUsers(sender)
    case getUserRequest : GetUserRequest => getUser(sender, getUserRequest)
    case createUserRequest : CreateUserRequest => createUser(sender, createUserRequest)
    case updateUserRequest : UpdateUserRequest => updateUser(sender, updateUserRequest)
    case deleteUserRequest : DeleteUserRequest => deleteUser(sender, deleteUserRequest)
  }

  private def getUser(sender: ActorRef, getUserRequest: GetUserRequest) =
    User.findById(getUserRequest.userId) map { response =>
      sender ! GetUserResponse(response)
    }

  private def getUsers(sender: ActorRef) =
    User.all().map { users =>
      sender ! GetUsersResponse(users)
    }

  private def createUser(sender: ActorRef, createUserRequest: CreateUserRequest) = {
    validate(createUserRequest.user) match {
      case Success =>
        User.insert (createUserRequest.user) map {
          user =>
            sender ! CreateUserResponse (user)
        }
      case Failure(e) => Future.successful( sender ! ValidationError(e.map(v => Descriptions.render(v.description) + " " + v.constraint).toList) )
    }
  }

  private def updateUser(sender: ActorRef, updateUserRequest: UpdateUserRequest) =
    User.update(updateUserRequest.user) map { response =>
      sender ! UpdateUserResponse(response)
    }

  private def deleteUser(sender: ActorRef, deleteUserRequest: DeleteUserRequest) =
    User.delete(deleteUserRequest.userId) map { response =>
      sender ! DeleteUserResponse(response)
    }
}
