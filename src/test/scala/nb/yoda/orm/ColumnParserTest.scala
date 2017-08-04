package nb.yoda.orm

import mocks.{Account, Client}
import nb.yoda.definition.NamingConvention
import org.scalatest.FunSuite

class ColumnParserTest extends FunSuite {

  test("1) Simple") {
    ColumnParser.namingConvention = NamingConvention.Simple
    val list = ColumnParser.colNames[Account]

    assert(list.head === "accountId")
  }

  test("1) CamelToSnakecase") {
    ColumnParser.namingConvention = NamingConvention.CamelToSnakecase
    val list = ColumnParser.colNames[Account]

    assert(list.head === "account_id")
  }

  test("2) ") {
    val keys = ColumnParser.colNames[Client]

    println(keys)
  }

}
