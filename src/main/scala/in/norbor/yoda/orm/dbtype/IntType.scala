package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JInt.JInt
import in.norbor.yoda.orm.PStatement

trait IntType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setInt(param: Int): PStatement = setInt(counter, param)

  private def setInt(ind: Int, param: Int): PStatement = {
    pstmt.setInt(ind, param)
    count
  }

  def setJInt(param: JInt): PStatement = setJInt(counter, param)

  private def setJInt(ind: Int, param: JInt): PStatement = {
    pstmt.setInt(ind, param)
    count
  }

}
