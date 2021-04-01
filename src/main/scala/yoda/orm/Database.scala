/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.orm

import java.sql.Connection

import com.typesafe.scalalogging.LazyLogging
import javax.inject.Singleton
import yoda.commons.Closer

@Singleton
class Database extends LazyLogging with Closer {

  private val p = Persistant()

  def withConnection[R](name: String)
                       (block: Connection => R): R = closer(p.get) { c =>
    val start: Long = System.nanoTime
    try {
      block(c)
    } finally {
      val executionTime = (System.nanoTime() - start) / 1000
      logger.debug(s"Execute in $executionTime ms.")
      c.close()
    }
  }

  def withTransaction[R](name: String)
                        (block: Connection => R): R = closer(p.get) { c =>
    val start: Long = System.nanoTime
    try {
      c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE)
      c.setAutoCommit(false)
      block(c)
    } catch {
      case t: Throwable => c.rollback()
        throw t
    } finally {
      c.close()
      logger.debug(s"Execute in ${(System.nanoTime() - start) / 1000} ms.")
    }
  }

}
