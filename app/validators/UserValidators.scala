package validators

import models.User

/**
  * Created by utsav on 14/5/17.
  */
object UserValidators {

  import com.wix.accord.dsl._
  import CustomValidators._

  implicit val userValidator = validator[User] { user =>
    (user.firstName is notEmpty) and (user.firstName has size <= 20)
    (user.lastName is notEmpty) and (user.lastName has size <= 20)
    user.email is mailAddress
    user.localeId is locale
  }
}
