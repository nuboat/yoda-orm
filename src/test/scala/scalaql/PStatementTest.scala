package scalaql

import java.sql.{DriverManager, ResultSet, Timestamp}

import models.People
import org.joda.time.DateTime
import org.scalatest.FunSuite

import scalaql.JavaSqlImprovement._

/**
  * Created by Peerapat A on Feb 5, 2017
  */
class PStatementTest extends FunSuite {

  Class.forName("org.h2.Driver")
  implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("1) testQueryOne") {

    val result = PStatement("""select ?, ?, ?, ?, ?, ?, ?, now() as key;""")
      .setBoolean(true)
      .setLong(1L)
      .setString("YO")
      .setDateTime(DateTime.now)
      .setInt(1)
      .setTimestamp(new Timestamp(System.currentTimeMillis))
      .setTimestamp(null)
      .queryOne(parse)

    println(result)
  }

  test("2) testQueryOne with Auto Parser") {
    val people = PStatement("""select 1 as id, 'Peerapat' as name, now() as born;""")
      .queryOne[People]

    println(people)
  }

  private def parse(rs: ResultSet): (Boolean, Long, String, DateTime, Int, Timestamp, DateTime, DateTime) = (rs.getBoolean(1)
    , rs.getLong(2)
    , rs.getString(3)
    , rs.getDateTime(4)
    , rs.getInt(5)
    , rs.getTimestamp(6)
    , rs.getDateTime(7)
    , rs.getDateTime("key")
  )

}
