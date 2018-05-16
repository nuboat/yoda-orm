package in.norbor.yoda.utilities

import com.typesafe.scalalogging.LazyLogging

import scala.language.reflectiveCalls
import scala.util.Try

/**
  * Created by Peerapat A on Mar 18, 2017
  */
trait Closer extends LazyLogging {

  def closer[T <: {def close()}, R](resource: T)(block: T => R): R = try {
    block(resource)
  } finally {
    Try(resource.close()).failed foreach { t => logger.warn(t.getMessage)}
  }

}
