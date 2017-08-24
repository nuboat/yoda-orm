package nb.yoda.orm

import java.sql.{Connection, Timestamp}

import com.typesafe.scalalogging.LazyLogging
import nb.yoda.reflect.Accessor
import org.joda.time.DateTime

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 31, 2017
  */
object PManager extends LazyLogging {

  def apply[A: TypeTag : ClassTag](obj: A)(implicit conn: Connection): Int = try {
    insert(obj)
  } catch {
    case _: Throwable => update(obj)
  }

  final def insert[A: TypeTag : ClassTag](obj: A)(implicit conn: Connection): Int = {
    val kv = Accessor.toMap[A](obj)

    val meta = findMeta(kv)

    val keys = ColumnParser.colNames[A]

    val table = meta.table.getOrElse(obj.getClass.getSimpleName.toLowerCase)

    val stmt = insertStatement(table, keys)

    logger.debug(s"STMT $stmt")

    val p = PStatement(stmt)

    val kvs = kv.map(k => ColumnParser.namingStategy(k._1) -> k._2)

    logger.debug(s"KV $kvs")

    keys.foreach(k => set(p, kvs(k)))

    p.update
  }

  final def update[A: TypeTag : ClassTag](obj: A)(implicit conn: Connection): Int = {
    val kv = Accessor.toMap[A](obj)

    val meta = findMeta(kv)

    val pk = meta.pk

    val columns = ColumnParser.colNames[A]
      .filter(k => k != pk)
      .filter(k => !meta.readonly.contains(k))

    val table = meta.table.getOrElse(obj.getClass.getSimpleName.toLowerCase)

    val stmt = updateStatement(table, pk, columns)

    logger.debug(s"STMT $stmt")

    val p = PStatement(stmt)

    val kvs = kv.map(k => ColumnParser.namingStategy(k._1) -> k._2)

    columns.foreach(k => set(p, kvs(k)))

    logger.debug(s"KV $kvs")

    set(p, kvs(pk))

    p.update
  }

  final def delete[A](obj: A)(implicit conn: Connection): Int = {
    val kv = Accessor.toMap[A](obj)

    val meta = findMeta(kv)

    val pk = meta.pk

    val table = obj.getClass.getSimpleName.toLowerCase

    PStatement(
      s"""
         | DELETE $table WHERE $pk = ${kv(pk)}
       """.stripMargin)
      .update
  }

  private[orm] def set(p: PStatement, v: Any) = v match {
    case _: Boolean => p.setBoolean(v.asInstanceOf[Boolean])
    case _: Int => p.setInt(v.asInstanceOf[Int])
    case _: Long => p.setLong(v.asInstanceOf[Long])
    case _: Float => p.setDouble(v.asInstanceOf[Float])
    case _: Double => p.setDouble(v.asInstanceOf[Double])
    case _: String => p.setString(v.asInstanceOf[String])
    case _: Timestamp => p.setTimestamp(v.asInstanceOf[Timestamp])
    case _: DateTime => p.setDateTime(v.asInstanceOf[DateTime])
    case _ => ;
  }

  private[orm] def findMeta(kv: Map[String, Any]): Meta = kv
    .find(kv => kv._2.isInstanceOf[Meta])
    .map(kv => kv._2.asInstanceOf[Meta])
    .getOrElse(Meta())

  private[orm] def insertStatement(table: String, keys: List[String]) =
    s"""
       | INSERT INTO $table (${keys.mkString(", ")}) VALUES (${params(keys.size)})
     """.stripMargin

  private[orm] def updateStatement(table: String, pk: String, columns: List[String]) =
    s"""
       | UPDATE $table SET ${updateValue(columns)} = ? WHERE $pk = ?
     """.stripMargin

  private[orm] def updateValue(columns: List[String]): String = columns
    .mkString(" = ?, ")

  private[orm] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
