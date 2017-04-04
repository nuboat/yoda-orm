package nb.yoda.reflect

import scala.reflect._
import scala.util.Try

/**
  * <p>This helper does not support inner class
  *
  * Created by Peerapat A on Mar 21, 2017
  */
private[yoda] object CCParser {

  private[yoda] def apply[T](vals: Map[String, Any])(implicit cmf: ClassTag[T]): T = {
    val ctor = cmf.runtimeClass.getConstructors
      .head

    val args = cmf.runtimeClass.getDeclaredFields
      .map(f => Try(vals(f.getName).asInstanceOf[AnyRef]).getOrElse(null))
      .filter(r => r != null)

    ctor.newInstance(args: _*).asInstanceOf[T]
  }

}
