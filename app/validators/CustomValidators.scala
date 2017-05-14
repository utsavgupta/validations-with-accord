package validators

import com.wix.accord._
import ViolationBuilder._
import java.util.Locale

import scala.util.{Success, Failure, Try}

/**
  * Created by utsav on 14/5/17.
  */
object CustomValidators {
  /*
    Custom validator for validating locale ids
   */
  def locale: Validator[String] =
    new NullSafeValidator[String](
      test = isValidLocale,
      failure = _ -> "is invalid"
    )

  /*
    Custom validator for validating email addresses
   */
  def mailAddress: Validator[String] =
    new NullSafeValidator[String](
      test = (s: String) => """\A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z""".r findFirstIn(s) nonEmpty,
      failure = _ -> "is invalid"
    )

  /*
    Helper function for validating the locale id.
    Uses the locale class provided in java.util
   */
  private def isValidLocale(str: String) : Boolean = {
    val tokens = "(_|-)".r.split(str)

    if (tokens.size != 2) false
    else {
      Try {
        val locale = new Locale(tokens(0), tokens(1))

        // the locale id provided can't be mapped to ISO codes
        locale.getISO3Country() != null && locale.getISO3Language() != null

      } match {
        case Success(_) => true
        case Failure(_) => false
      }
    }
  }
}
