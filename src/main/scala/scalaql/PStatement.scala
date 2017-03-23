package scalaql

import java.sql.{Connection, ResultSet, Timestamp}

import org.joda.time.DateTime

import scala.reflect._
import scala.reflect.runtime.universe._
import scalaql.JavaSqlImprovement._

/**
  * Created by Peerapat A on Feb 5, 2017
  */
case class PStatement(sql: String)(implicit conn: Connection) {

  private var counter: Int = 1

  private val pstmt = conn.prepareStatement(sql)

  def setBoolean(param: Boolean): PStatement = setBoolean(counter, param)

  def setBoolean(ind: Int, param: Boolean): PStatement = {
    pstmt.setBoolean(ind, param)
    count
  }

  def setInt(param: Int): PStatement = setInt(counter, param)

  def setInt(ind: Int, param: Int): PStatement = {
    pstmt.setInt(ind, param)
    count
  }

  def setLong(param: Long): PStatement = setLong(counter, param)

  def setLong(ind: Int, param: Long): PStatement = {
    pstmt.setLong(ind, param)
    count
  }

  def setDouble(param: Double): PStatement = setDouble(counter, param)

  def setDouble(ind: Int, param: Double): PStatement = {
    pstmt.setDouble(ind, param)
    count
  }

  def setString(param: String): PStatement = setString(counter, param)

  def setString(ind: Int, param: String): PStatement = {
    pstmt.setString(ind, param)
    count
  }

  def setTimestamp(param: Timestamp): PStatement = setTimestamp(counter, param)

  def setTimestamp(ind: Int, param: Timestamp): PStatement = {
    pstmt.setTimestamp(ind, param)
    count
  }

  def setDateTime(param: DateTime): PStatement = setDateTime(counter, param)

  def setDateTime(ind: Int, param: DateTime): PStatement = {
    if (param == null) {
      pstmt.setTimestamp(ind, null)
    } else {
      pstmt.setTimestamp(ind, new Timestamp(param.getMillis))
    }
    count
  }

  def update: Int = pstmt.executeUpdate

  def query: ResultSet = pstmt.executeQuery

  def queryOne[A](block: ResultSet => A): Option[A] = {
    val rs = pstmt.executeQuery
    if (rs.next) Some(block(rs)) else None
  }

  def queryList[A](block: ResultSet => A): List[A] = {
    val rs = pstmt.executeQuery
    new Iterator[A] {
      override def hasNext: Boolean = rs.next

      override def next: A = block(rs)
    }.toList
  }


  def queryOne[A: TypeTag : ClassTag]: Option[A] = {
    val rs = pstmt.executeQuery
    if (rs.next) Some(autoparse[A](rs)) else None
  }

  def queryList[A: TypeTag : ClassTag]: List[A] = {
    val rs = pstmt.executeQuery
    new Iterator[A] {
      override def hasNext: Boolean = rs.next

      override def next: A = autoparse[A](rs)
    }.toList
  }

  def addBatch(): PStatement = {
    pstmt.addBatch()
    counter = 1
    this
  }

  def executeBatch: Array[Int] = pstmt.executeBatch

  private def count: PStatement = {
    counter = counter + 1
    this
  }

  private def autoparse[A: TypeTag : ClassTag](rs: ResultSet): A = {
    val kv = Accessor.methods[A]
      .map(sym => sym.name.toString -> lookup(rs, sym))
      .toMap

    CCParser[A](kv)
  }

  private def lookup(rs: ResultSet, sym: MethodSymbol) = sym.info.toString match {
    case "=> Boolean" => rs.getBoolean(sym.name.toString)
    case "=> Int" => rs.getInt(sym.name.toString)
    case "=> Long" => rs.getLong(sym.name.toString)
    case "=> Double" => rs.getDouble(sym.name.toString)
    case "=> String" => rs.getString(sym.name.toString)
    case "=> java.sql.Timestamp" => rs.getTimestamp(sym.name.toString)
    case "=> org.joda.time.DateTime" => rs.getDateTime(sym.name.toString)
    case _ => null
  }

}
