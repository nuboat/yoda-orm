package in.norbor.yoda.orm

import java.sql.Connection

import com.typesafe.scalalogging.LazyLogging
import in.norbor.yoda.utilities.{Accessor, AnnotationHelper}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Created by Peerapat A on Mar 31, 2017
  */
object PManager extends LazyLogging
  with PSetAny {

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
