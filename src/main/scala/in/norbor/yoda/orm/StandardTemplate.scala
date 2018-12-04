package in.norbor.yoda.orm

/**
  * @author Peerapat A on April 15, 2018
  */
private[orm] object StandardTemplate {

  private[orm] val name = "SQLGenerated"

  private[orm] val jdbc: String =
    """package $packageName
      |
      |import java.sql.{Connection, ResultSet}
      |
      |import $entityFullName
      |import in.norbor.yoda.implicits.JavaSqlImprovement._
      |import in.norbor.yoda.jtype._
      |import in.norbor.yoda.orm.PStatement
      |
      |/**
      |  * @author Yoda B
      |  */
      |trait $simpleNameGenerated {
      |
      |  private val QUERY_ID: String = "SELECT * FROM $table WHERE $idName = ?"
      |
      |  private val INSERT: String = "$insertStatement"
      |
      |  private val UPDATE: String = "$updateStatement"
      |
      |  private val DELETE: String = "DELETE FROM $table WHERE $idName = ?"
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
      |  def get(id: $idType)
      |         (implicit conn: Connection): Option[$entityName] = PStatement(QUERY_ID)
      |    .set$idType(id)
      |    .queryOne(parse)
      |
      |  def update(e: $entityName)
      |            (implicit conn: Connection): Int = PStatement(UPDATE)
      |    $updateParams
      |    .update
      |
      |  def delete(id: $idType)(implicit conn: Connection): Int = PStatement(DELETE)
      |    .set$idType(id)
      |    .update
      |
      |  def count()(implicit conn: Connection): Long = PStatement(COUNT)
      |    .queryOne(rs => rs.getLong(1))
      |    .get
      |
      |  protected def verifyName(p: String): Unit = if (!COLUMNS.contains(p)) throw new IllegalArgumentException(s"$p has problem.")
      |
      |  protected def parse(rs: ResultSet) = $entityName(
      |    $bindResult
      |  )
      |
      |}
    """.stripMargin

}
