/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.orm

import java.sql.{Connection, DriverManager, ResultSet, Timestamp}

import mocks.People
import org.joda.time.DateTime
import org.scalatest.funsuite.AnyFunSuite
import yoda.orm.implicits.JavaSqlImprovement._

/**
  * Created by Peerapat A on Feb 5, 2017
  */
class PStatementTest extends AnyFunSuite {

  Class.forName("org.h2.Driver")

  private implicit val conn: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("0) apply") {

    val ps = PStatement("SELECT 1")(conn)
    assert(ps !== null)

    ps.equals(null)
    ps.canEqual(null)
    ps.hashCode
    ps.toString
    ps.productPrefix
    ps.productArity
    ps.productElement(0)
    ps.productIterator
    ps.copy()
  }

  test("0) query") {

    PStatement("DROP TABLE IF EXISTS yoda_sql; CREATE TABLE yoda_sql (id INTEGER);")
      .update
  }

  test("0) update") {

    val rs = PStatement("""select 1""")
      .query

    assert(rs !== null)
  }

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

  test("3) queryList with parse method") {

    val peoples = PStatement("""select 1 as id, 'Peerapat' as name, now() as born;""")
      .queryList(parsePeople)

    assert(peoples.head.id === 1)
    assert(peoples.head.name === "Peerapat")
    assert(peoples.head.born.getMillis <= DateTime.now.getMillis)
  }

  test("5) batch") {

    val insert = PStatement("INSERT INTO yoda_sql VALUES(?)")
      .setInt(1)
      .addBatch()
      .setInt(2)
      .addBatch()
      .executeBatch

    assert(insert.length === 2)
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
