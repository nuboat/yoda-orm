package yoda.orm.generator

/**
  * @author Peerapat A on April 15, 2018
  */
private[generator] object StandardTemplate {

  private[generator] val name = "SQLGenerated"

  private[generator] val jdbc: String =
    """package $packageName
      |
      |import java.sql.{Connection, ResultSet}
      |
      |import $entityFullName
      |import yoda.orm.implicits.JavaSqlImprovement._
      |import yoda.orm.jtype._
      |import yoda.orm.PStatement
      |
      |/**
      |  * @author Master Norbor
      |  */
      |trait $simpleNameGenerated {
      |
      |  private val QUERY_ID: String = "SELECT * FROM $table WHERE $pkCondition"
      |
      |  private val INSERT: String = "$insertStatement"
      |
      |  private val UPDATE: String = "$updateStatement"
      |
      |  private val DELETE: String = "DELETE FROM $table WHERE $pkCondition"
      |
      |  private val COUNT: String = "SELECT COUNT(1) FROM $table"
      |
      |  private val COLUMNS: Set[String] = Set($setColumnName)
      |
      |  def insert(e: $entityName)
      |            (implicit conn: Connection): Int = PStatement(INSERT)
      |    $insertParams
      |    .update
      |
      |  def update(e: $entityName)
      |            (implicit conn: Connection): Int = PStatement(UPDATE)
      |    $updateParams
      |    .update
      |
      |  def get($pkParams)
      |         (implicit conn: Connection): Option[$entityName] = PStatement(QUERY_ID)
      |    $setPkparams
      |    .queryOne(parse)
      |
      |  def delete($pkParams)(implicit conn: Connection): Int = PStatement(DELETE)
      |    $setPkparams
      |    .update
      |
      |  def count()(implicit conn: Connection): Long = PStatement(COUNT)
      |    .queryOne(rs => rs.getLong(1))
      |    .get
      |
      |  protected def verifyName(p: String): Unit = if (!COLUMNS.contains(p))
      |    throw new IllegalArgumentException(s"$p has problem.")
      |
      |  protected def parse(rs: ResultSet): $entityName = $entityName(
      |    $bindResult
      |  )
      |
      |}
    """.stripMargin

}
