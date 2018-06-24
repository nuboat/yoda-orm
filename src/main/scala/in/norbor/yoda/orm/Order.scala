package in.norbor.yoda.orm

/**
  * @author Peerapat A on Jun 24, 2018
  */
case class Order(name: String
                 , reverse: Boolean = false) {

  lazy val sortBy: String = if (reverse) "DESC" else "ASC"

}
