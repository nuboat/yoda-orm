package yodaorm.core

import scala.reflect._
import scala.util.Try

/**
  * <p>This helper does not support inner class
  *
  * Created by Peerapat A on Mar 21, 2017
  */
private[yodaorm] object CCParser {

  private[yodaorm] def apply[T](vals: Map[String, Any])(implicit cmf: ClassTag[T]): T = {
    val ctor = cmf.runtimeClass.getConstructors
      .head

    val args = cmf.runtimeClass.getDeclaredFields
      .map(f => Try(vals(f.getName).asInstanceOf[AnyRef]).getOrElse(null))
      .filter(r => r != null)

    ctor.newInstance(args: _*).asInstanceOf[T]
  }

  private[yodaorm] def toMap(cc: AnyRef): Map[String, AnyRef] =
    (Map[String, AnyRef]() /: cc.getClass.getDeclaredFields) {
      (a, f) =>
        f.setAccessible(true)
        a + (f.getName -> f.get(cc))
    }

}
