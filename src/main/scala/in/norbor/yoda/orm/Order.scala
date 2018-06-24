package in.norbor.yoda.orm

/**
  * @author Peerapat A on Jun 24, 2018
  */
private[orm] case class Order(name: String
                              , reverse: Boolean = false) {

  private[orm] lazy val sortBy: String = if (reverse) "DESC" else "ASC"

}
