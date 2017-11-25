package in.norbor.yoda.orm

import java.sql.{Connection, DriverManager, ResultSet, Timestamp}

import in.norbor.yoda.orm.JavaSqlImprovement._
import mocks.{Foo, JavaTest, People, Username}
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Feb 5, 2017
  */
class PStatementTest extends FunSuite {

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

  test("4) queryOne with auto Parser case lookup unsupport") {

    try {
      PStatement("""select 1.0 as amount""")
        .queryOne[Foo]
    } catch {
      case u: IllegalArgumentException => succeed
      case _: Throwable => fail("")
    }

  }

  test("4) queryLimit with auto parse") {
    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
        |
        |INSERT INTO people (id, name, born) VALUES
        | (1, 'Yo', now()),
        | (2, 'Yo', now()),
        | (3, 'Yo', now()),
        | (4, 'Yo', now());
        |
      """.stripMargin)
      .update


    val peoples = PStatement("""SELECT * FROM people;""")
      .queryLimit[People](3)

    assert(peoples.size === 3)
  }

  test("5) batch") {

    val insert = PStatement("INSERT INTO yoda_sql VALUES(?)")
      .setInt(1, 1)
      .addBatch()
      .setInt(1, 2)
      .addBatch()
      .executeBatch

    assert(insert.length === 2)
  }

  test("6) queryRange with offset 0, length 2") {
    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
        |
        |INSERT INTO people (id, name, born) VALUES
        | (1, 'Yo', now()),
        | (2, 'Yo', now()),
        | (3, 'Yo', now()),
        | (4, 'Yo', now());
        |
      """.stripMargin)
      .update

    val peoples1 = PStatement("""SELECT * FROM people;""")
      .queryRange[People](0, 4)

    assert(peoples1.size === 4)

    val peoples2 = PStatement("""SELECT * FROM people;""")
      .queryRange[People](2, 4)

    assert(peoples2.size === 2)
  }

  test("7) query with java primitive type") {
    PStatement(
      """
        |DROP TABLE IF EXISTS javatest;
        |CREATE TABLE javatest (ida INT, idb BIGINT, idc DOUBLE);
        |
        |INSERT INTO javatest(ida, idb, idc) VALUES
        | (1, 2, 3.3);
      """.stripMargin)
      .update

    val javaTest = PStatement("select * from javatest;")
      .queryOne[JavaTest]

    assert(javaTest !== null)
  }

  test("8) query jbcrypt ") {
    PStatement(
      """
        |DROP TABLE IF EXISTS username;
        |CREATE TABLE username (username VARCHAR(128), password VARCHAR(128));
        |
        |INSERT INTO username (username, password) VALUES
        | ('yoda', '$2a$10$0F6o7qJj06WGLZcsAahBMeRvuKKSNgdDSpicwKz6oFPJKxdQhUgp2'),
      """.stripMargin)
      .update

    val re = PStatement("select * from username;")
      .queryOne[Username]

    assert(re.isDefined)
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
