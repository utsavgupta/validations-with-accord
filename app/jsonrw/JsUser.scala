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
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "localeId").read[String]
  )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[String]  and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "localeId").write[String]
  )(unlift(User.unapply _))
}
