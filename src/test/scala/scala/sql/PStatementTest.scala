package scala.sql

import java.sql.{DriverManager, ResultSet}

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.FunSuite

/**
  * Created by nuboat on 2/5/2017 AD.
  */
class PStatementTest extends FunSuite {

  test("testQueryOne") {
    Class.forName("org.h2.Driver")
    implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

    val result = PStatement("""select ?, ?, ?, ?;""")
      .setBoolean(true)
      .setLong(1L)
      .setString("YO")
      .setDateTime(DateTime.now)
      .queryOne(parse)

    println(result)

    conn.close()
  }

  private def parse(rs: ResultSet): (Boolean, Long, String, DateTime, DateTime) = (rs.getBoolean(1)
    , rs.getLong(2)
    , rs.getString(3)
    , new DateTime(rs.getTimestamp(4).getTime)
    , new DateTime(rs.getTimestamp(4).getTime, DateTimeZone.forID("Asia/Bangkok")))

}
