package nb.yoda.orm

import java.sql.DriverManager

import mocks.Citizen
import nb.yoda.definition.NamingConvention
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by Peerapat A on Aug 5, 2017
  */
class PManagerCamelTest extends FunSuite with BeforeAndAfterEach {

  Class.forName("org.h2.Driver")

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  override def beforeEach(): Unit = {
    ColumnParser.namingConvention = NamingConvention.CamelToSnakecase
  }

  test("1) Auto Parser camelcase from Class to snakecase on DB.") {

    PStatement(
      """
        |DROP TABLE IF EXISTS citizens;
        |CREATE TABLE citizens (citizen_id BIGINT);
      """.stripMargin)
      .update

    val count = PManager.insert(Citizen(1L))
    assert(count === 1)

  }

}
