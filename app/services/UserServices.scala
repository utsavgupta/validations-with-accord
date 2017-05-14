package services

import akka.actor.{Actor, ActorRef}
import models.User

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by utsav on 13/5/17.
  */

object UserServices {
  case class GetUserRequest(userId: String)
  case class GetUserResponse(user: Option[User])

  case class GetUsersRequest()
  case class GetUsersResponse(users: List[User])

  case class CreateUserRequest(user: User)
  case class CreateUserResponse(user: User)

  case class UpdateUserRequest(user: User)
  case class UpdateUserResponse(status: Boolean)

  case class DeleteUserRequest(userId: String)
  case class DeleteUserResponse(status: Boolean)
}

class UserServices extends Actor {
  import services.UserServices._

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

  private def createUser(sender: ActorRef, createUserRequest: CreateUserRequest) =
    User.insert(createUserRequest.user) map { user =>
      sender ! CreateUserResponse(user)
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
