package in.norbor.yoda.implicits

import scala.reflect.runtime.universe._

/**
  * @author Peerapat A on April 20, 2018
  */
object MethodSymbolImprovement {

  implicit class MethodSymbolClass(sym: MethodSymbol) {

    def simpleName: String = sym.info.toString
      .replace("=> ", "")
      .replace("scala.", "")
      .replace("java.lang.", "")
      .replace("java.sql.", "")
      .replace("org.joda.time.", "")
      .replace("in.norbor.yoda.jtype.", "")

  }

}
