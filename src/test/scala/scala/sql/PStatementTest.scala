package scala.sql

import java.sql.{DriverManager, ResultSet, Timestamp}

import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by nuboat on Feb 5, 2017
  */
class PStatementTest extends FunSuite {

  test("testQueryOne") {
    Class.forName("org.h2.Driver")
    implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

    val result = PStatement("""select ?, ?, ?, ?, ?, ?;""")
      .setBoolean(true)
      .setLong(1L)
      .setString("YO")
      .setDateTime(DateTime.now)
      .setInt(1)
      .setTimestamp(new Timestamp(System.currentTimeMillis))
      .queryOne(parse)

    println(result)

    conn.close()
  }

  private def parse(rs: ResultSet): (Boolean, Long, String, DateTime, Int, Timestamp) = (rs.getBoolean(1)
    , rs.getLong(2)
    , rs.getString(3)
    , rs.getDateTime(4)
    , rs.getInt(5)
    , rs.getTimestamp(6)
  )

}
