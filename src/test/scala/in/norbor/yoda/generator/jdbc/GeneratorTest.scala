package in.norbor.yoda.generator.jdbc

import in.norbor.yoda.orm.Generator
import mocks.Customer
import org.scalatest.FunSuite

class GeneratorTest extends FunSuite {

  test("1") {
    val g = Generator()
    implicit val target: String = "target"

    g.gen[Customer](table = "customer", idName = "id", idType = "String")
  }

}
