package in.norbor.yoda.orm

import in.norbor.yoda.utilities.Json
import org.scalatest.FunSuite

class FilterItemTest extends FunSuite {

  test("1") {
    val s =
      """
        |{
        | "name" : "id"
        | , "operator" : "<>"
        | , "value" : 11231232132312312
        |}
      """.stripMargin

    val o = Json.toObject[FilterItem](s)

    println(o)
  }

}
