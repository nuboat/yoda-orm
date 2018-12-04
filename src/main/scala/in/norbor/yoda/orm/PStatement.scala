package in.norbor.yoda.orm

import java.sql.{Blob, Connection, PreparedStatement, ResultSet}

import com.typesafe.scalalogging.LazyLogging
import in.norbor.yoda.implicits.JavaSqlImprovement._
import in.norbor.yoda.implicits.MethodSymbolImprovement._
import in.norbor.yoda.orm.dbtype._

import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Feb 5, 2017
  */
case class PStatement(sql: String)(implicit conn: Connection)
  extends LazyLogging
    with BooleanType
    with IntType
    with LongType
    with DoubleType
    with StringType
    with TimestampType
    with DateTimeType
    with BinaryType {

  private var counter: Int = 1

  protected val stmt: PreparedStatement = conn.prepareStatement(sql)

  protected def pstmt: PreparedStatement = stmt

  protected def count: PStatement = {
    counter = counter + 1
    this
  }

  protected def index: Int = counter

  def createBlob: Blob = conn.createBlob

  def update: Int = {
    counter = 1
    pstmt.executeUpdate
  }

  def query: ResultSet = {
    counter = 1
    pstmt.executeQuery
  }

  def queryOne[A](block: ResultSet => A): Option[A] = {
    counter = 1

    val rs = pstmt.executeQuery
    if (rs.next) Option(block(rs)) else None
  }

  def queryList[A](block: ResultSet => A): List[A] = {
    counter = 1

    val rs = pstmt.executeQuery
    new Iterator[A] {
      override def hasNext: Boolean = rs.next

      override def next: A = block(rs)
    }.toList
  }

  def addBatch(): PStatement = {
    counter = 1

    pstmt.addBatch()
    this
  }

  def executeBatch: Array[Int] = {
    counter = 1

    pstmt.executeBatch
  }

  private def lookup(rs: ResultSet, sym: MethodSymbol, col: String) = sym.simpleName match {
    case "Boolean" => rs.getBoolean(col)
    case "JBoolean" => rs.getJBoolean(col)
    case "Int" | "Integer" => rs.getInt(col)
    case "JInt" => rs.getJInt(col)
    case "Long" => rs.getLong(col)
    case "JLong" => rs.getJLong(col)
    case "Double" => rs.getDouble(col)
    case "JDouble" => rs.getJDouble(col)
    case "Float" => rs.getDouble(col)
    case "JFloat" => rs.getJDouble(col)
    case "String" => rs.getString(col)
    case "Blob" => rs.getBlob(col)
    case "Timestamp" => rs.getTimestamp(col)
    case "DateTime" => rs.getDateTime(col)

    case _ => throw new IllegalArgumentException(s"Does not support ${sym.info.toString}")
  }

}
