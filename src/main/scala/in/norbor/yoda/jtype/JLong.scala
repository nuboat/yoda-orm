package in.norbor.yoda.jtype

/**
  * Peerapat A, Sep 23, 2017
  */
object JLong {

  type JLong = java.lang.Long

  def apply(v: Long): JLong = java.lang.Long.valueOf(v)

}
