package mocks

import java.sql.{Connection, ResultSet}

import in.norbor.yoda.implicits.JavaSqlImprovement._
import in.norbor.yoda.orm.PStatement

/**
  * @author Yoda B
  */
trait jtypeSQLGenerated {

  protected val QUERY_ID: String = "SELECT * FROM jtype WHERE id = ?"

  protected val INSERT: String = "INSERT INTO jtype (id, int, tf, dou) VALUES (?, ?, ?, ?)"

  protected val UPDATE: String = "UPDATE jtype SET int = ?, tf = ?, dou = ? WHERE id = ?"

  protected val DELETE: String = "DELETE FROM jtype WHERE id = ?"

  def insert(e: JType)
            (implicit conn: Connection): Int = PStatement(INSERT)
    .setJLong(e.id)
    .setJInt(e.int)
    .setJBoolean(e.tf)
    .setJDouble(e.dou)
    .update

  def get(id: Long)
         (implicit conn: Connection): Option[JType] = PStatement(QUERY_ID)
    .setLong(id)
    .queryOne(parse)

  def update(e: JType)
            (implicit conn: Connection): Option[JType] = PStatement(UPDATE)
    .setJInt(e.int)
    .setJBoolean(e.tf)
    .setJDouble(e.dou)
    .setJLong(e.id)
    .queryOne(parse)

  def delete(id: Long)(implicit conn: Connection): Int = PStatement(DELETE)
    .setLong(id)
    .update

  protected def parse(rs: ResultSet) = JType(
    id = rs.getJLong("id")
    , int = rs.getJInt("int")
    , tf = rs.getJBoolean("tf")
    , dou = rs.getJDouble("dou")
  )

}
    