package yoda.orm.generator

import java.io.{File, FileWriter, StringReader}

import com.typesafe.scalalogging.LazyLogging
import org.apache.velocity.runtime.RuntimeSingleton
import org.apache.velocity.{Template, VelocityContext}
import yoda.commons.{Closer, Naming}
import yoda.orm.definitions.NamingConvention

import scala.reflect.runtime.universe._

/**
 * @author Peerapat A on April 15, 2018
 */
case class Generator(namingConvention: NamingConvention) extends LazyLogging
  with Closer
  with Naming {

  ColumnParser.namingConvention = namingConvention

  private val runtimeServices = RuntimeSingleton.getRuntimeServices
  private val tempateSQL = new Template()
  tempateSQL.setRuntimeServices(runtimeServices)
  tempateSQL.setData(runtimeServices.parse(new StringReader(StandardTemplate.jdbc), StandardTemplate.name))
  tempateSQL.initDocument()

  def gen[A: TypeTag](table: String, pks: Seq[(String, String)])
                     (implicit target: Target): Unit = {
    val symbol = typeOf[A].typeSymbol
    val entityFullName = symbol.fullName
    val entityName = symbol.toString.stripPrefix("class ")
    val className = s"${snakecaseToCamel(table)}SQLGenerated"
    val columns = ColumnParser.colNames[A]

    val context = new VelocityContext()
    context.put("packageName", target.packageName)
    context.put("entityName", entityName)
    context.put("entityFullName", entityFullName)
    context.put("simpleNameGenerated", className)
    context.put("table", table)
    context.put("setColumnName", setColumnName(columns))
    context.put("insertStatement", insertStatement(table, columns))
    context.put("pkCondition", pkCondition(pks))
    context.put("updateStatement", updateStatement(table, pks, columns))
    context.put("bindResult", bindResult(columns))
    context.put("insertParams", bindInsert(columns))
    context.put("setPkparams", setPkparams(pks))
    context.put("pkParams", pkParams(pks))
    context.put("updateParams", bindUpdate(columns, pks))

    val fileName = s"${target.directoryName}/$className.scala"
    logger.info(s"Save data to $fileName")
    render(fileName = fileName, context)
  }

  private[generator] def pkCondition(pks: Seq[(String, String)]): String = pks
    .map(t => s"${t._1} = ?")
    .mkString(" AND ")

  private[generator] def pkParams(pks: Seq[(String, String)]): String = pks
    .map(t => s"${t._1}: ${t._2}")
    .mkString(", ")

  private[generator] def setPkparams(pks: Seq[(String, String)]): String = pks
    .map(t => s".set${t._2}(e.${t._1})")
    .mkString("\n    ")

  private[generator] def bindInsert(columns: List[ColumnMeta]): String = columns
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")

  private[generator] def bindUpdate(columns: List[ColumnMeta], pks: Seq[(String, String)]): String = columns
    .filter(k => !pks.map(_._1).contains(k.valName))
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")
    .concat(bindUpdateWhere(pks))

  private[generator] def bindUpdateWhere(pks: Seq[(String, String)]): String = pks
    .map(t => s".set${t._2}(e.${t._1})")
    .mkString("\n    ")

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
    file.getParentFile.mkdirs
    file.createNewFile

    new FileWriter(file)
  }

  private[generator] def setColumnName(keys: List[ColumnMeta]): String = keys.map(c => {
    s""""${c.schemaName}""""
  }).mkString(", ")

  private[generator] def insertStatement(table: String
                                         , keys: List[ColumnMeta]): String =
    s"""INSERT INTO $table (${keys.map(_.valName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[generator] def updateStatement(table: String
                                         , pks: Seq[(String, String)]
                                         , columns: List[ColumnMeta]): String =
    s"""UPDATE $table SET ${updateValue(columns, pks)} = ? WHERE ${pkCondition(pks)}""".stripMargin

  private[generator] def updateValue(columns: List[ColumnMeta], pks: Seq[(String, String)]): String = columns
    .filter(k => !pks.map(_._1).contains(k.valName))
    .map(_.schemaName)
    .mkString(" = ?, ")

  private[generator] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
