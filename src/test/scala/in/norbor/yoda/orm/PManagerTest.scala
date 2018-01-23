package in.norbor.yoda.orm

import java.sql.{Connection, DriverManager}

import in.norbor.yoda.jtype.Jbcrypt
import mocks._
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Apr 4, 2017
  */
class PManagerTest extends FunSuite {

  Class.forName("org.h2.Driver")

  private implicit val conn: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

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

  test("4 Insert class with Jbcrypt") {
    PStatement(
      """
        | DROP TABLE IF EXISTS username;
        | CREATE TABLE username (username VARCHAR(128), password VARCHAR(128));
        |
        | INSERT INTO username (username, password) VALUES
        |   ('yoda', '$2a$10$0F6o7qJj06WGLZcsAahBMeRvuKKSNgdDSpicwKz6oFPJKxdQhUgp2'),
      """.stripMargin)
      .update

    val count = PManager(Username(username = "Yo"
      , password = Jbcrypt("$2a$10$0F6o7qJj06WGLZcsAahBMeRvuKKSNgdDSpicwKz6oFPJKxdQhUgp2")))

    assert(count === 1)
  }

  test("5) insert java primitive type") {
    PStatement(
      """
        |DROP TABLE IF EXISTS javatest;
        |CREATE TABLE javatest (ida INT, idb BIGINT, idc DOUBLE);
      """.stripMargin)
      .update

    val re = PManager.insert(JavaTest(1, 2L, 3.3))

    assert(re === 1)
  }

  test("6) insert java blob type") {
    PStatement(
      """
        |DROP TABLE IF EXISTS javablob;
        |CREATE TABLE javablob (id INT, blob BLOB);
      """.stripMargin)
      .update


    val byteData = "Test Insert blob".getBytes("UTF-8")
    val blob = conn.createBlob()
    blob.setBytes(1, byteData)

    val re = PManager.insert(JavaBlob(1, blob))

    assert(re === 1)
  }

  test("7) insert array of bytes") {
    PStatement(
      """
        |DROP TABLE IF EXISTS javabytes;
        |CREATE TABLE javabytes (id INT, blob BLOB);
      """.stripMargin)
      .update


    val bytes = "Test Insert blob".getBytes("UTF-8")

    val re = PManager.insert(JavaBytes(1, bytes))

    assert(re === 1)
  }

}
