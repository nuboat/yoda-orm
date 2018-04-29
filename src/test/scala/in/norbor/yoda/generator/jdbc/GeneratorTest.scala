package in.norbor.yoda.generator.jdbc

import in.norbor.yoda.orm.Generator
import mocks.{Customer, JType}
import org.scalatest.FunSuite

class GeneratorTest extends FunSuite {

  private val g = Generator()

  implicit val target: String = "target"

  test("1") {
    g.gen[Customer](table = "customer", idName = "id", idType = "String")
  }

  test("2") {
    g.gen[JType](table = "jtype", idName = "id", idType = "Long")
  }

}
