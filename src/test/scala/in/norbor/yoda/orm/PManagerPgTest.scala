package in.norbor.yoda.orm

import java.sql.{Connection, DriverManager}

import mocks.JavaBytes
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Jeb 2, 2018
  */
class PManagerPgTest extends FunSuite {

  Class.forName("org.postgresql.Driver")

  private implicit val conn: Connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "nuboat", "")

  test("7) insert array of bytes") {
    PStatement(
      """
        | DROP TABLE IF EXISTS javabytes;
        | CREATE TABLE javabytes (id INT, bytes bytea);
      """.stripMargin)
      .update

    val bytes = "Test Insert blob".getBytes("UTF-8")

    val re = PManager.insert(JavaBytes(1, bytes))

    assert(re === 1)
  }

}
