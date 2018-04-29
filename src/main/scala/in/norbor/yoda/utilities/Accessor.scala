package in.norbor.yoda.utilities

import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 20, 2017
  */
object Accessor {

  def methods[T: TypeTag]: List[MethodSymbol] = {
    val x = typeOf[T]
      .members
    x
      .collect {
        case m: MethodSymbol if m.isCaseAccessor => m
      }.toList.reverse
  }

  def toMap[A](cc: A): Map[String, Any] =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {
      (a, f) =>
        f.setAccessible(true)
        a + (f.getName -> f.get(cc))
    }

}
