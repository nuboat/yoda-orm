package in.norbor.yoda.utilities

import scala.reflect._
import scala.util.Try

/**
  * <p>This helper does not support inner class
  *
  * Created by Peerapat A on Mar 21, 2017
  */
object MapToClass {

  def apply[T](vals: Map[String, Any])(implicit cmf: ClassTag[T]): T = {
    val ctor = cmf.runtimeClass.getConstructors
      .head

    val args = cmf.runtimeClass.getDeclaredFields
      .filter(f => f.getType.getName != "in.norbor.yoda.orm.Meta")
      .map(f => Try(vals(f.getName).asInstanceOf[AnyRef]).getOrElse(null))

    ctor.newInstance(args: _*).asInstanceOf[T]
  }

}
