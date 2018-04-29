package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JLong.JLong
import in.norbor.yoda.orm.PStatement

trait LongType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setLong(param: Long): PStatement = setLong(counter, param)

  private def setLong(ind: Int, param: Long): PStatement = {
    pstmt.setLong(ind, param)
    count
  }

  def setJLong(param: JLong): PStatement = setJLong(counter, param)

  private def setJLong(ind: Int, param: JLong): PStatement = {
    pstmt.setLong(ind, param)
    count
  }

}
