package nb.yoda.orm

import java.sql.{DriverManager, ResultSet, Timestamp}

import mocks.{Foo, People}
import nb.yoda.orm.JavaSqlImprovement._
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Apr 4, 2017
  */
class PManagerTest extends FunSuite {

  Class.forName("org.h2.Driver")

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("0) apply") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
      """.stripMargin)
      .update

    PManager().save(People(1L, "Yo", DateTime.now))

  }

}
