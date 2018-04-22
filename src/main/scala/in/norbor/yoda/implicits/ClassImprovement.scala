package in.norbor.yoda.implicits

import in.norbor.yoda.utilities.Json

/**
  * @author Peerapat A on April 21, 2018
  */
object ClassImprovement {

  implicit class AnyJson(any: AnyRef) {
    def toText: String = Json.toJson(any)
  }

  implicit class ListSetter(list: List[AnyRef]) {
    def toText: String = Json.toJson(list)
  }

}
