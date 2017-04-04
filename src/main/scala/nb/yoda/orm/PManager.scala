package nb.yoda.orm

import java.sql.{Connection, Timestamp}

import nb.yoda.reflect.Accessor
import org.joda.time.DateTime

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 31, 2017
  */
case class PManager()(implicit conn: Connection) {

  def save[A: TypeTag : ClassTag](obj: A): Int = {
    insert(obj)
  }

  def insert[A: TypeTag : ClassTag](obj: A): Int = {
    val keys = colNames[A]
    val kv = Accessor.toMap[A](obj)
    val table = obj.getClass.getSimpleName.toLowerCase

    val stmt = insertStatement(table, keys)

    val p = PStatement(stmt)

    keys.foreach(k => set(p, kv(k)))

    p.update
  }

  //  def update[A](obj: A): Int = {
  //    ???
  //  }
  //
  //  def delete[A](obj: A): Int = {
  //    ???
  //  }

  private[orm] def set(p: PStatement, v: Any) = v match {
    case _: Boolean => p.setBoolean(v.asInstanceOf[Boolean])
    case _: Int => p.setInt(v.asInstanceOf[Int])
    case _: Long => p.setLong(v.asInstanceOf[Long])
    case _: Double => p.setDouble(v.asInstanceOf[Double])
    case _: String => p.setString(v.asInstanceOf[String])
    case _: Timestamp => p.setTimestamp(v.asInstanceOf[Timestamp])
    case _: DateTime => p.setDateTime(v.asInstanceOf[DateTime])
    case _ => p.setString(v.asInstanceOf[String])
  }

  private[orm] def insertStatement(table: String, keys: List[String]) =
    s"""
       | INSERT INTO $table (${keys.mkString(", ")}) VALUES (${params(keys.size)})
     """.stripMargin

  private[orm] def colNames[A: TypeTag]: List[String] = Accessor.methods[A]
    .map(sym => sym.name.toString)
    .map(name => name.toLowerCase)

  private[orm] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
