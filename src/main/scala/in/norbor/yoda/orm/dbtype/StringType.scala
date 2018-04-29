package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JBoolean.JBoolean
import in.norbor.yoda.orm.PStatement

trait StringType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setString(param: String): PStatement = setString(counter, param)

  private def setString(ind: Int, param: String): PStatement = {
    pstmt.setString(ind, param)
    count
  }

}
