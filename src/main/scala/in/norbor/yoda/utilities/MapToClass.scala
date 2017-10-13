package in.norbor.yoda.utilities

import java.lang.reflect.Field

import com.typesafe.scalalogging.LazyLogging
import in.norbor.yoda.jtype.Jbcrypt

import scala.reflect._

/**
  * <p>This helper does not support inner class
  *
  * Created by Peerapat A on Mar 21, 2017
  */
object MapToClass extends LazyLogging {

  def apply[T](vals: Map[String, Any])(implicit cmf: ClassTag[T]): T = {
    val args = cmf.runtimeClass.getDeclaredFields
      .filter(f => f.getType.getName != "in.norbor.yoda.orm.Meta")
      .map(f => lookup(vals, f) )

    cmf.runtimeClass.getConstructors
      .head.newInstance(args: _*).asInstanceOf[T]
  }

  def lookup(vals: Map[String, Any], f: Field): AnyRef = try {
    f.getType.getName match {
      case "in.norbor.yoda.orm.Jbcrypt" => Jbcrypt(vals(f.getName).toString).asInstanceOf[AnyRef]
      case _ => vals(f.getName).asInstanceOf[AnyRef]
    }

  } catch {
    case t: Throwable =>
      logger.warn(t.getMessage)
      null
  }

}
