package in.norbor.yoda.orm

import java.sql.DriverManager

import mocks.{Iden, People}
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Apr 4, 2017
  */
class PManagerTest extends FunSuite {

  Class.forName("org.h2.Driver")

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  test("0 Insert Identity Field") {

    PStatement(
      """
        |DROP TABLE IF EXISTS iden;
        |
        |CREATE TABLE iden (id BIGINT);
      """.stripMargin)
      .update

    val count = PManager.insert(Iden(1L))
    assert(count === 1)
  }

  test("1 INSERT") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT, name VARCHAR(128), born DATETIME);
      """.stripMargin)
      .update

    val count = PManager.insert(People(1L, "Yo", DateTime.now))
    assert(count === 1)
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

    val count = PManager.update(People(1L, "Yo 2", DateTime.now))
    assert(count === 1)
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

    val count = PManager.delete(People(1L, "Yo", DateTime.now))
    assert(count === 1)
  }

  test("3 UPSERT") {

    PStatement(
      """
        |DROP TABLE IF EXISTS people;
        |CREATE TABLE people (id BIGINT NOT NULL, name VARCHAR(128), born DATETIME);
        |ALTER TABLE people ADD PRIMARY KEY (id);
        |
        |INSERT INTO people (id, name, born) VALUES (1, 'Yo', now());
      """.stripMargin)
      .update

    val count = PManager(People(1L, "Yo Man", DateTime.now))
    assert(count === 1)
  }

}
