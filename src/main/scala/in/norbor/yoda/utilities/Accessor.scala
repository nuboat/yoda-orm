package in.norbor.yoda.utilities

//import scala.collection.mutable
import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 20, 2017
  */
object Accessor {

  def methods[T: TypeTag]: List[MethodSymbol] = {
    typeOf[T]
      .members
      .collect {
        case m: MethodSymbol if m.isCaseAccessor => m
      }.toList.reverse
  }

//  def toMap[A](cc: A): Map[String, Any] =
//    cc.getClass.getDeclaredFields.foldLeft(mutable.Map[String, Any]())(
//      (a, f) => {
//        f.setAccessible(true)
//        a.addOne(f.getName -> f.get(cc))
//      }).toMap

}
