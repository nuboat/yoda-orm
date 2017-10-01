package in.norbor.yoda.utilities

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Created by Peerapat A on Mar 18, 2017
  */
object Conf {

  private val conf = ConfigFactory.load()

  def apply(key: String): String = conf.getString(key)
  def apply(key: String, default: String): String = Try(conf.getString(key)).getOrElse(default)

  def string(key: String): String = conf.getString(key)
  def string(key: String, default: String): String = Try(conf.getString(key)).getOrElse(default)

  def bool(key: String): Boolean = conf.getBoolean(key)
  def bool(key: String, default: Boolean): Boolean = Try(conf.getBoolean(key)).getOrElse(default)

  def int(key: String): Int = conf.getInt(key)
  def int(key: String, default: Int): Int = Try(conf.getInt(key)).getOrElse(default)

}
