package in.norbor.yoda.orm

/**
  * @author Peerapat A on April 15, 2018
  */
private[orm] object StandardTemplate {

  private[orm] val name = "SQLGenerated"

  private[orm] val jdbc: String =
    """package in.norbor.yoda.orm.generated
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
      |  protected val QUERY_ID: String = "SELECT * FROM $table WHERE $idName = ?"
      |
      |  protected val INSERT: String = "$insertStatement"
      |
      |  protected val UPDATE: String = "$updateStatement"
      |
      |  protected val DELETE: String = "DELETE FROM $table WHERE $idName = ?"
      |
      |  protected val COUNT: String = "SELECT COUNT(1) FROM $table"
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
      |            (implicit conn: Connection): Option[$entityName] = PStatement(UPDATE)
      |    $updateParams
      |    .queryOne(parse)
      |
      |  def delete(id: $idType)(implicit conn: Connection): Int = PStatement(DELETE)
      |    .set$idType(id)
      |    .update
      |
      |  def count()(implicit conn: Connection): Long = PStatement(COUNT)
      |    .queryOne(rs => rs.getLong(1))
      |    .get
      |
      |  protected def parse(rs: ResultSet) = $entityName(
      |    $bindResult
      |  )
      |
      |}
    """.stripMargin

}
