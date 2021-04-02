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

  def gen[A: TypeTag](table: String, pks: Seq[String])
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
    context.put("setPkparams", setPkparams(columns, pks))
    context.put("pkParams", pkParams(columns, pks))
    context.put("updateParams", bindUpdate(columns, pks))

    val fileName = s"${target.directoryName}/$className.scala"
    logger.info(s"Save data to $fileName")
    render(fileName = fileName, context)
  }

  private[generator] def pkCondition(pks: Seq[String]): String = pks
    .map(pk => s"$pk = ?")
    .mkString(" AND ")

  private[generator] def pkParams(columns: List[ColumnMeta], pks: Seq[String]): String = pks
    .map(pk => s"${colMeta(columns, pk).valName}: ${colMeta(columns, pk).schemaType}")
    .mkString(", ")

  private[generator] def setPkparams(columns: List[ColumnMeta], pks: Seq[ String]): String = pks
    .map(pk => s".set${colMeta(columns,pk).schemaType}(${colMeta(columns,pk).valName})")
    .mkString("\n    ")

  private[generator] def bindInsert(columns: List[ColumnMeta]): String = columns
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")

  private[generator] def bindUpdate(columns: List[ColumnMeta], pks: Seq[String]): String = columns
    .filter(k => !pks.contains(k.schemaName))
    .map(k => s".set${k.schemaType}(e.${k.valName})")
    .mkString("\n    ")
    .concat("\n    ")
    .concat(bindWherePk(columns, pks))

  private[generator] def bindWherePk(columns: List[ColumnMeta], pks: Seq[String]): String = pks
    .map(pk => s".set${colMeta(columns,pk).schemaType}(e.${colMeta(columns,pk).valName})")
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
    s"""INSERT INTO $table (${keys.map(_.schemaName).mkString(", ")}) VALUES (${params(keys.size)})""".stripMargin

  private[generator] def updateStatement(table: String
                                         , pks: Seq[String]
                                         , columns: List[ColumnMeta]): String =
    s"""UPDATE $table SET ${updateValue(columns, pks)} = ? WHERE ${pkCondition(pks)}""".stripMargin

  private[generator] def updateValue(columns: List[ColumnMeta], pks: Seq[String]): String = columns
    .filter(k => !pks.contains(k.schemaName))
    .map(_.schemaName)
    .mkString(" = ?, ")

  private[generator] def colMeta(columns: List[ColumnMeta], pk: String): ColumnMeta = {
    columns.find(_.schemaName == pk).get
  }

  private[generator] def params(count: Int): String = List.fill(count)("?")
    .mkString(", ")

}
