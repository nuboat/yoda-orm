package in.norbor.yoda.orm

import java.sql.{Blob, Connection, Timestamp}

import com.typesafe.scalalogging.LazyLogging
import in.norbor.yoda.jtype.JBcrypt
import in.norbor.yoda.utilities.{Accessor, AnnotationHelper}
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
    val meta = findMeta[A]
    val table = meta.table.getOrElse(obj.getClass.getSimpleName.toLowerCase)

    val keys = ColumnParser.colNames[A]
    val stmt = insertStatement(table, keys)

    logger.debug(s"STMT $stmt")

    val p = PStatement(stmt)

    val kvs = Accessor.toMap[A](obj).map(k => ColumnParser.namingStategy(k._1) -> k._2)

    logger.debug(s"KV $kvs")

    keys.foreach(k => set(p, kvs(k.schemaName)))

    p.update
  }

  final def update[A: TypeTag : ClassTag](obj: A)(implicit conn: Connection): Int = {
    val kv = Accessor.toMap[A](obj)

    val meta = findMeta[A]

    val pk = meta.pk

    val columns = ColumnParser.colNames[A]

    val table = meta.table.getOrElse(obj.getClass.getSimpleName.toLowerCase)

    val stmt = updateStatement(table, pk, columns)

    logger.debug(s"STMT $stmt")

    val p = PStatement(stmt)

    val kvs = kv.map(k => ColumnParser.namingStategy(k._1) -> k._2)

    columns.filter(_.schemaName != pk).foreach(k => set(p, kvs(k.schemaName)))

    logger.debug(s"KV $kvs")

    set(p, kvs(pk))

    p.update
  }

  final def delete[A: TypeTag](obj: A)(implicit conn: Connection): Int = {
    val kv = Accessor.toMap[A](obj)

    val meta = findMeta[A]

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
    case _: Float => p.setDouble(v.asInstanceOf[Double])
    case _: Double => p.setDouble(v.asInstanceOf[Double])
    case _: String => p.setString(v.asInstanceOf[String])
    case _: Timestamp => p.setTimestamp(v.asInstanceOf[Timestamp])
    case _: DateTime => p.setDateTime(v.asInstanceOf[DateTime])
    case _: JBcrypt => p.setString(v.asInstanceOf[JBcrypt].hash)
    case _: Blob => p.setBlob(v.asInstanceOf[Blob])
    case _: Array[Byte] => p.setBytes(v.asInstanceOf[Array[Byte]])
    case _ => ;
  }

  private[orm] def findMeta[T: TypeTag]: MetaSchema = {
    AnnotationHelper.classAnnotations[T].get("TableSchema")
      .map(a => MetaSchema(pk = a.get("pk").getOrElse(null)
        , table = a.get("name")))
      .getOrElse(MetaSchema())
  }

  private[orm] def insertStatement(table: String, keys: List[ColumnMeta]) =
    s"""INSERT INTO $table (${keys.map(_.schemaName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[orm] def updateStatement(table: String, pk: String, columns: List[ColumnMeta]) =
    s"""UPDATE $table SET ${updateValue(columns, pk)} = ? WHERE $pk = ?""".stripMargin

  private[orm] def updateValue(columns: List[ColumnMeta], pk: String): String = columns
    .map(_.schemaName)
    .filter(k => k != pk)
    .mkString(" = ?, ")

  private[orm] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
