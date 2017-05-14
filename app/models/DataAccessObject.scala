package models

import scala.concurrent.Future

/**
  * Created by utsav on 13/5/17.
  */
trait DataAccessObject[T] {
  def findById(id: String): Future[Option[T]]
  def all(): Future[List[T]]
  def insert(elem: T): Future[T]
  def update(elem: T): Future[Boolean]
  def delete(id: String): Future[Boolean]
}
