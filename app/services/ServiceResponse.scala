package services

/**
  * Created by utsav on 14/5/17.
  */

trait ServiceResponse

case class ValidationError(messages: List[String]) extends ServiceResponse
case class InternalError(messages: List[String]) extends ServiceResponse