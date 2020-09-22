package yoda.orm.dbtype

import java.sql.{Blob, PreparedStatement}

import yoda.orm.PStatement

trait BinaryType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setBytes(param: Array[Byte]): PStatement = setBytes(index, param)

  private def setBytes(ind: Int, param: Array[Byte]): PStatement = {
    pstmt.setBytes(ind, param)
    count
  }

  def setBlob(param: Blob): PStatement = setBlob(index, param)

  private def setBlob(ind: Int, param: Blob): PStatement = {
    pstmt.setBlob(ind, param)
    count
  }

}
