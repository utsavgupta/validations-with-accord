package filters

import akka.actor.{Actor, ActorRef}

import com.wix.accord._
import services.ValidationError

import scala.concurrent.Future

/**
  * Created by utsav on 15/5/17.
  */
abstract class ActorWithFilter extends Actor  {
  protected def beforeFilter[T](obj : T)(onSuccess: => Any)(implicit v: Validator[T]) = {
    validate(obj) match {
      case Success =>
        onSuccess
      case Failure(e) =>
        Future.successful(sender ! ValidationError(e.map(v => Descriptions.render(v.description) + " " + v.constraint).toList))
    }
  }
}