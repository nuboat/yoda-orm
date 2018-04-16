package in.norbor.yoda.generator.jdbc

import java.io.{File, FileWriter, StringReader}

import in.norbor.yoda.orm.{ColumnMeta, ColumnParser}
import in.norbor.yoda.utilities.Closer
import org.apache.velocity.runtime.RuntimeSingleton
import org.apache.velocity.{Template, VelocityContext}

import scala.language.postfixOps
import scala.reflect.runtime.universe._

/**
  * @author Peerapat A on April 15, 2018
  */
case class Generator() extends Closer {

  private val runtimeServices = RuntimeSingleton.getRuntimeServices
  private val tempateSQL = new Template()
  tempateSQL.setRuntimeServices(runtimeServices)
  tempateSQL.setData(runtimeServices.parse(new StringReader(StandardTemplate.jdbc), StandardTemplate.name))
  tempateSQL.initDocument()

  def gen[A: TypeTag](table: String, idName: String, idType: String)
                     (implicit target: String): Unit = {
    val symbol = typeOf[A].typeSymbol
    val entityFullName = symbol.fullName
    val entityName = symbol.toString.stripPrefix("class ")

    val keys = ColumnParser.colNames[A]

    val context = new VelocityContext()
    context.put("entityName", entityName)
    context.put("entityFullName", entityFullName)
    context.put("simpleNameGenerated", s"${table}SQLGenerated")
    context.put("table", table)
    context.put("idType", idType)
    context.put("idName", idName)
    context.put("insertStatement", insertStatement(table, keys))
    context.put("updateStatement", updateStatement(table, idName, keys))

    context.put("bindResult", bindResult(keys))
    context.put("insertParams", bindInsert(keys))
    context.put("updateParams", bindUpdate(keys, idName))

    render(fileName = s"$target/${table}SQLGenerated.scala", context)
  }

  private[jdbc] def bindInsert(keys: List[ColumnMeta]): String = keys
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")

  private[jdbc] def bindUpdate(keys: List[ColumnMeta], pk: String): String = keys
    .filter(k => k.schemaName != pk)
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")
    .concat(bindUpdateWhere(keys, pk))

  private[jdbc] def bindUpdateWhere(keys: List[ColumnMeta], pk: String): String =
    s"\n    ${keys.find(_.schemaName == pk).map(k => s".set${k.schemaType}(e.${k.valName})").get}"

  private[jdbc] def bindResult(keys: List[ColumnMeta]): String = keys
    .map(k => s""", ${k.valName} = rs.get${k.schemaType}("${k.schemaName}")""")
    .mkString("\n    ")
    .replaceFirst(", ", "")

  private[jdbc] def render(fileName: String
                           , context: VelocityContext): Unit = {
    closer(newWriter(fileName)) { writer =>
      tempateSQL.merge(context, writer)
    }
  }

  private[jdbc] def newWriter(fileName: String): FileWriter = {
    val file = new File(fileName)
    file.createNewFile

    new FileWriter(file)
  }

  private[jdbc] def insertStatement(table: String, keys: List[ColumnMeta]) =
    s"""INSERT INTO $table (${keys.map(_.schemaName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[jdbc] def updateStatement(table: String, pk: String, columns: List[ColumnMeta]) =
    s"""UPDATE $table SET ${updateValue(columns, pk)} = ? WHERE $pk = ?""".stripMargin

  private[jdbc] def updateValue(columns: List[ColumnMeta], pk: String): String = columns
    .filter(k => k.schemaName != pk)
    .map(_.schemaName)
    .mkString(" = ?, ")

  private[jdbc] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
