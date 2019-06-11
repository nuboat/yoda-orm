package in.norbor.yoda.generator

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

  def gen[A: TypeTag](table: String, idName: String, idType: String
                      , packageName: String = "in.norbor.yoda.orm.generated")
                     (implicit target: String): Unit = {
    val symbol = typeOf[A].typeSymbol
    val entityFullName = symbol.fullName
    val entityName = symbol.toString.stripPrefix("class ")
    val className = s"${Naming.snakecaseToCamel(table)}SQLGenerated"
    val keys = ColumnParser.colNames[A]

    val context = new VelocityContext()
    context.put("packageName", packageName)
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

  private[generator] def bindInsert(keys: List[ColumnMeta]): String = keys
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")

  private[generator] def bindUpdate(keys: List[ColumnMeta], pk: String): String = keys
    .filter(k => k.schemaName != pk)
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")
    .concat(bindUpdateWhere(keys, pk))

  private[generator] def bindUpdateWhere(keys: List[ColumnMeta], pk: String): String =
    s"\n    ${keys.find(_.schemaName == pk).map(k => s".set${k.schemaType}(e.${k.valName})").get}"

  private[generator] def bindResult(keys: List[ColumnMeta]): String = keys
    .map(k => s""", ${k.valName} = rs.get${k.schemaType}("${k.schemaName}")""")
    .mkString("\n    ")
    .replaceFirst(", ", "")

  private[generator] def render(fileName: String
                          , context: VelocityContext): Unit = {
    closer(newWriter(fileName)) { writer =>
      tempateSQL.merge(context, writer)
    }
  }

  private[generator] def newWriter(fileName: String): FileWriter = {
    val file = new File(fileName)
    file.createNewFile

    new FileWriter(file)
  }

  private[generator] def setColumnName(keys: List[ColumnMeta]): String = keys.map(c => {
    s""""${c.schemaName}""""
  }).mkString(", ")

  private[generator] def insertStatement(table: String, keys: List[ColumnMeta]): String =
    s"""INSERT INTO $table (${keys.map(_.schemaName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[generator] def updateStatement(table: String, pk: String, columns: List[ColumnMeta]): String =
    s"""UPDATE $table SET ${updateValue(columns, pk)} = ? WHERE $pk = ?""".stripMargin

  private[generator] def updateValue(columns: List[ColumnMeta], pk: String): String = columns
    .filter(k => k.schemaName != pk)
    .map(_.schemaName)
    .mkString(" = ?, ")

  private[generator] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
