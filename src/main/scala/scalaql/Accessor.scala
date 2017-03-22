package scalaql

import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 20, 2017
  */
private[scalaql] object Accessor {

  private[scalaql] def methods[T: TypeTag]: List[MethodSymbol] = typeOf[T]
    .members
    .collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }.toList

}
