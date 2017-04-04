package nb.yoda.reflect

import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 20, 2017
  */
private[yoda] object Accessor {

  private[yoda] def methods[T: TypeTag]: List[MethodSymbol] = typeOf[T]
    .members
    .collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }.toList.reverse

  private[yoda] def toMap[A](cc: A): Map[String, Any] =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {
      (a, f) =>
        f.setAccessible(true)
        a + (f.getName -> f.get(cc))
    }

}
