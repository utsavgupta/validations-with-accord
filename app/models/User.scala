package models

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.UUID

/**
  * Created by utsav on 13/5/17.
  */

case class User(id: Option[String], firstName: String,
                lastName: String, email: String, localeId: String)

object User extends DataAccessObject[User] {

  private val storage: ArrayBuffer[User] = new ArrayBuffer[User]()

  override def findById(id: String): Future[Option[User]] = Future( storage.find(_.id.map(_ == id) getOrElse false) )

  override def all(): Future[List[User]] = Future( storage.toList )

  override def insert(user: User): Future[User] = Future {
    val uuid = UUID.randomUUID().toString

    val newUser = user.copy(id = Some(uuid))

    storage += newUser

    newUser
  }

  override def update(user: User): Future[Boolean] = user.id.map { id =>
    findById(id).mapTo[Option[User]].map {
      case Some(u) => { storage update (storage indexOf u, user) ; true }
      case None => false
    }
  } getOrElse Future.successful(false)

  override def delete(id: String): Future[Boolean] =
    findById(id).mapTo[Option[User]].map {
      case Some(user) => { storage -= user ; true }
      case None => false
    }
}