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

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("0) queryOne with non index parameter") {

    val result = PStatement("""select ?, ?, ?, ?, ?, ?, ?, ?""")
      .setBoolean(true)
      .setInt(1)
      .setLong(1L)
      .setDouble(1)
      .setString("YO")
      .setDateTime(DateTime.now)
      .setTimestamp(new Timestamp(System.currentTimeMillis))
      .setTimestamp(null)
      .queryOne(parse)

    assert(result.head._1 === true)
  }

  test("1) PStatement set with index parameter") {

    val result = PStatement("""select ?, ?, ?, ?, ?, ?, ?, ?, ?""")
      .setBoolean(true)
      .setInt(2, 1)
      .setLong(3, 1L)
      .setDouble(4, 1)
      .setString(5, "YO")
      .setDateTime(6, DateTime.now)
      .setTimestamp(7, new Timestamp(System.currentTimeMillis))
      .setDateTime(8, null)
      .setTimestamp(9, null)
      .queryOne(parse)

    assert(result.head._1 === true)
  }


  test("2) queryOne with auto Parser") {

    val people = PStatement("""select 1 as id, 'Peerapat' as name, now() as born;""")
      .queryOne[People]

    assert(people.head.id === 1)
    assert(people.head.name === "Peerapat")
    assert(people.head.born.getMillis <= DateTime.now.getMillis)
  }

  test("3) queryList with parse method") {

    val peoples = PStatement("""select 1 as id, 'Peerapat' as name, now() as born;""")
      .queryList(parsePeople)

    assert(peoples.head.id === 1)
    assert(peoples.head.name === "Peerapat")
    assert(peoples.head.born.getMillis <= DateTime.now.getMillis)
  }

  test("4) queryList with auto parse") {

    val peoples = PStatement("""select 1 as id, 'Peerapat' as name, now() as born;""")
      .queryList[People]

    assert(peoples.head.id === 1)
    assert(peoples.head.name === "Peerapat")
    assert(peoples.head.born.getMillis <= DateTime.now.getMillis)
  }

  private def parse(rs: ResultSet): (Boolean, Int, Long, Double, String, DateTime, Timestamp) = (rs.getBoolean(1)
    , rs.getInt(2)
    , rs.getLong(3)
    , rs.getDouble(4)
    , rs.getString(5)
    , rs.getDateTime(6)
    , rs.getTimestamp(7)
  )

  private def parsePeople(rs: ResultSet): People = People(id = rs.getLong("id")
    , name = rs.getString("name")
    , born = rs.getDateTime("born")
  )

}
