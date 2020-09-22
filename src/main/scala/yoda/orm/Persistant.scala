package yoda.orm

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

case class Persistant(config: String = "hikari.properties") {

  private val ds = new HikariDataSource(new HikariConfig(config))

  def get: Connection = ds.getConnection

}
