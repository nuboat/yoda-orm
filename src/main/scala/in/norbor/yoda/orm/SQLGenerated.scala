package in.norbor.yoda.orm

import java.sql.Connection

trait SQLGenerated {

  def insert(e: Any)
            (implicit conn: Connection): Int

  def get(id: AnyRef)
         (implicit conn: Connection): Option[Any]

  def update(e: Any)
            (implicit conn: Connection): Option[Any]

  def delete(id: AnyRef)(implicit conn: Connection): Int

}
