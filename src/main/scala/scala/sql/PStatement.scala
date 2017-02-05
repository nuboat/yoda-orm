package scala.sql

import java.sql.{Connection, ResultSet, Timestamp}

import org.joda.time.DateTime


/**
  * Created by nuboat on 2/5/2017 AD.
  */
case class PStatement(sql: String)(implicit conn: Connection) {

  private var next: Int = 1

  private val pstmt = conn.prepareStatement(sql)

  def setBoolean(param: Boolean): PStatement = setBoolean(next, param)

  def setBoolean(ind: Int, param: Boolean): PStatement = {
    pstmt.setBoolean(ind, param)
    nextParam
  }

  def setLong(param: Long): PStatement = setLong(next, param)

  def setLong(ind: Int, param: Long): PStatement = {
    pstmt.setLong(ind, param)
    nextParam
  }

  def setString(param: String): PStatement = setString(next, param)

  def setString(ind: Int, param: String): PStatement = {
    pstmt.setString(ind, param)
    nextParam
  }

  def setTimestamp(param: Timestamp): PStatement = setTimestamp(next, param)

  def setTimestamp(ind: Int, param: Timestamp): PStatement = {
    pstmt.setTimestamp(ind, param)
    nextParam
  }

  def setDateTime(param: DateTime): PStatement = setDateTime(next, param)

  def setDateTime(ind: Int, param: DateTime): PStatement = {
    pstmt.setTimestamp(ind, new Timestamp(param.getMillis))
    nextParam
  }

  def initial: PStatement = {
    next = 1
    this
  }

  def update: Int = pstmt.executeUpdate

  def query: ResultSet = pstmt.executeQuery

  def queryOne[A](block: ResultSet => A): Option[A] = {
    val rs = pstmt.executeQuery
    if (rs.next) Some(block(rs)) else None
  }

  def queryList[A](block: ResultSet => A): Iterator[A] = {
    val rs = pstmt.executeQuery
    new Iterator[A] {
      override def hasNext: Boolean = rs.next

      override def next: A = block(rs)
    }
  }

  def close: PStatement = {
    pstmt.close()
    this
  }

  def cancel: PStatement = {
    pstmt.cancel()
    this
  }

  private def nextParam: PStatement = {
    next = next + 1
    this
  }

}
