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
      |import in.norbor.yoda.orm.JavaSqlImprovement._
      |import in.norbor.yoda.orm.PStatement
      |import in.norbor.yoda.orm.SQLGenerated
      |
      |/**
      |  * @author Yoda B
      |  */
      |trait $simpleNameGenerated extends SQLGenerated {
      |
      |  protected val QUERY_ID: String = "SELECT * FROM $table WHERE $idName = ?"
      |
      |  protected val INSERT: String = "$insertStatement"
      |
      |  protected val UPDATE: String = "$updateStatement"
      |
      |  protected val DELETE: String = "DELETE FROM $table WHERE $idName = ?"
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
      |  protected def parse(rs: ResultSet) = $entityName(
      |    $bindResult
      |  )
      |
      |}
    """.stripMargin

}
