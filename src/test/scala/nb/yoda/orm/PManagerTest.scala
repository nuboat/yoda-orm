package nb.yoda.orm

import java.sql.DriverManager

import mocks.People
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Apr 4, 2017
  */
class PManagerTest extends FunSuite {

  Class.forName("org.h2.Driver")

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("1 INSERT") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
      """.stripMargin)
      .update

    val start = System.currentTimeMillis()
    PManager.insert(People(1L, "Yo", DateTime.now))
    val end = System.currentTimeMillis()

    println(s"Run in ${end - start} ms.")

    val start1 = System.currentTimeMillis()
    PManager.insert(People(1L, "Yo", DateTime.now))
    val end1 = System.currentTimeMillis()

    println(s"Run in ${end1 - start1} ms.")
  }

  test("2 UPDATE") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
        |
        |INSERT INTO people (id, name, born) VALUES (1, 'Yo', now());
      """.stripMargin)
      .update

    PManager.update(People(1L, "Yo 2", DateTime.now))
  }

  test("3 DELETE") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
        |
        |INSERT INTO people (id, name, born) VALUES (1, 'Yo', now());
      """.stripMargin)
      .update

    PManager.delete(People(1L, "Yo", DateTime.now))
  }

  test("3 UPSERT") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
        |
        |INSERT INTO people (id, name, born) VALUES (1, 'Yo', now());
      """.stripMargin)
      .update

    PManager(People(1L, "Yo", DateTime.now))
  }

}
