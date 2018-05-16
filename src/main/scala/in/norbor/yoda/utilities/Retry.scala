package in.norbor.yoda.utilities

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

/**
  * Created by Peerapat A on June 4, 2017
  */
object Retry extends LazyLogging {

  @annotation.tailrec
  def apply[A](limit: Int, count: Int = 1, backoff: Duration = 5 seconds)(f: => A): A = Try(f) match {

    case Success(x) => x

    case Failure(e) =>
      logger.warn(s"Failed $count / $limit times")
      logger.error(e.getMessage, e)

      if (count < limit) {
        Thread.sleep(backoff.toMillis)
        Retry(limit = limit, count = count + 1, backoff = backoff) {
          f
        }
      } else {
        throw new IllegalStateException("Reached Limit Retry")
      }
  }

}
