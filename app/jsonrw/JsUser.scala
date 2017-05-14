package jsonrw

import models.User
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, Writes}

case class Resident(name: String, age: Int, role: Option[String])

/**
  * Created by utsav on 14/5/17.
  */
object JsUser {
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[String]  and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "email").readNullable[String] and
      (JsPath \ "localeId").readNullable[String]
  )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[String]  and
      (JsPath \ "firstName").writeNullable[String] and
      (JsPath \ "lastName").writeNullable[String] and
      (JsPath \ "email").writeNullable[String] and
      (JsPath \ "localeId").writeNullable[String]
  )(unlift(User.unapply _))
}
