package in.norbor.yoda.orm

import java.sql.{Connection, ResultSet, Timestamp}

import in.norbor.yoda.orm.JavaSqlImprovement._
import in.norbor.yoda.utilities.{Accessor, MapToClass}
import org.joda.time.DateTime

import scala.collection.mutable
import scala.reflect._
import scala.reflect.runtime.universe._

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

  def queryLimit[A: TypeTag : ClassTag](max: Int): List[A] = {
    var count = 0
    val buffer = mutable.ListBuffer[A]()

    val rs = pstmt.executeQuery

    while (rs.next && count < max) {
      buffer += autoparse[A](rs)
      count = count + 1
    }

    buffer.toList
  }

  def queryLimit[A: TypeTag : ClassTag](max: Int, block: ResultSet => A): List[A] = {
    var count = 0
    val buffer = mutable.ListBuffer[A]()

    val rs = pstmt.executeQuery

    while (rs.next && count < max) {
      buffer += block(rs)
      count = count + 1
    }

    buffer.toList
  }

  /**
    *
    * @param offset is index of list start from 0
    * @param length is size of return record
    * @return
    */
  @deprecated("Performance Issues, Please aviod to use till it fixed", "1.5.0")
  def queryRange[A: TypeTag : ClassTag](offset: Int, length: Int): List[A] = {
    var count = 0
    val buffer = mutable.ListBuffer[A]()

    val rs = pstmt.executeQuery

    try {
      for (i <- 0 until offset) {
        rs.next()
        count = count + 1
      }
    } catch {
      case t: Throwable => throw new IllegalStateException(s"Total records are $count less than $offset.")
    }

    count = 0
    while (rs.next && count < length) {
      buffer += autoparse[A](rs)
      count = count + 1
    }

    buffer.toList
  }

  /**
    *
    * @param offset is index of list start from 0
    * @param length is size of return record
    * @return
    */
  @deprecated("Performance Issues, Please aviod to use till it fixed", "1.5.0")
  def queryRange[A: TypeTag : ClassTag](offset: Int, length: Int, block: ResultSet => A): List[A] = {
    var count = 0
    val buffer = mutable.ListBuffer[A]()

    val rs = pstmt.executeQuery

    try {
      for (i <- 0 until offset) {
        rs.next()
        count = count + 1
      }
    } catch {
      case t: Throwable => throw new IllegalStateException(s"Total records are $count less than $offset.")
    }

    count = 0
    while (rs.next && count < length) {
      buffer += block(rs)
      count = count + 1
    }

    buffer.toList
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
      .map(sym => sym.name.toString -> lookup(rs, sym, ColumnParser.namingStategy(sym)))
      .toMap

    MapToClass[A](kv)
  }

  private def lookup(rs: ResultSet, sym: MethodSymbol, col: String) = sym.info.toString
    .replace("scala.", "")
    .replace("java.lang.", "") match {

    case "=> Boolean" => rs.getBoolean(col)
    case "=> Int" => rs.getInt(col)
    case "=> Integer" => rs.getInt(col)
    case "=> Long" => rs.getLong(col)
    case "=> Double" => rs.getDouble(col)
    case "=> String" => rs.getString(col)
    case "=> in.norbor.yoda.jtype.Jbcrypt" => rs.getJbcrypt(col)
    case "=> java.sql.Timestamp" => rs.getTimestamp(col)
    case "=> org.joda.time.DateTime" => rs.getDateTime(col)
    case _ => rs.getString(col)
  }

}
