package in.norbor.yoda.orm

import java.io.{File, FileWriter, StringReader}

import in.norbor.yoda.definitions.NamingConvention.NamingConvention
import in.norbor.yoda.utilities.{Closer, Naming}
import org.apache.velocity.runtime.RuntimeSingleton
import org.apache.velocity.{Template, VelocityContext}
import scala.reflect.runtime.universe._

/**
  * @author Peerapat A on April 15, 2018
  */
case class Generator(namingConvention: NamingConvention) extends Closer {

  ColumnParser.namingConvention = namingConvention

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

    val className = s"${Naming.snakecaseToCamel(table)}SQLGenerated"

    val keys = ColumnParser.colNames[A]

    val context = new VelocityContext()
    context.put("entityName", entityName)
    context.put("entityFullName", entityFullName)
    context.put("simpleNameGenerated", className)
    context.put("table", table)
    context.put("idType", idType)
    context.put("idName", idName)
    context.put("setColumnName", setColumnName(keys))
    context.put("insertStatement", insertStatement(table, keys))
    context.put("updateStatement", updateStatement(table, idName, keys))

    context.put("bindResult", bindResult(keys))
    context.put("insertParams", bindInsert(keys))
    context.put("updateParams", bindUpdate(keys, idName))

    render(fileName = s"$target/$className.scala", context)
  }

  private[orm] def bindInsert(keys: List[ColumnMeta]): String = keys
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")

  private[orm] def bindUpdate(keys: List[ColumnMeta], pk: String): String = keys
    .filter(k => k.schemaName != pk)
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")
    .concat(bindUpdateWhere(keys, pk))

  private[orm] def bindUpdateWhere(keys: List[ColumnMeta], pk: String): String =
    s"\n    ${keys.find(_.schemaName == pk).map(k => s".set${k.schemaType}(e.${k.valName})").get}"

  private[orm] def bindResult(keys: List[ColumnMeta]): String = keys
    .map(k => s""", ${k.valName} = rs.get${k.schemaType}("${k.schemaName}")""")
    .mkString("\n    ")
    .replaceFirst(", ", "")

  private[orm] def render(fileName: String
                          , context: VelocityContext): Unit = {
    closer(newWriter(fileName)) { writer =>
      tempateSQL.merge(context, writer)
    }
  }

  private[orm] def newWriter(fileName: String): FileWriter = {
    val file = new File(fileName)
    file.createNewFile

    new FileWriter(file)
  }

  private[orm] def setColumnName(keys: List[ColumnMeta]): String = keys.map(c => {
    s""""${c.schemaName}""""
  }).mkString(", ")

  private[orm] def insertStatement(table: String, keys: List[ColumnMeta]): String =
    s"""INSERT INTO $table (${keys.map(_.schemaName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[orm] def updateStatement(table: String, pk: String, columns: List[ColumnMeta]): String =
    s"""UPDATE $table SET ${updateValue(columns, pk)} = ? WHERE $pk = ?""".stripMargin

  private[orm] def updateValue(columns: List[ColumnMeta], pk: String): String = columns
    .filter(k => k.schemaName != pk)
    .map(_.schemaName)
    .mkString(" = ?, ")

  private[orm] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
