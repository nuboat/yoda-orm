package in.norbor.yoda.generator

import in.norbor.yoda.definitions.NamingConvention
import in.norbor.yoda.generator.Generator
import mocks.{Customer, JType}
import org.scalatest.FunSuite

class GeneratorTest extends FunSuite {

  private val g = Generator(NamingConvention.CamelToSnakecase)

  implicit val target: String = "target"

  test("1") {
    g.gen[Customer](table = "customer", idName = "id", idType = "String")
  }

  test("2") {
    g.gen[JType](table = "jtype", idName = "id", idType = "Long")
  }

}
