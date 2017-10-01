package in.norbor.yoda.jtype

/**
  * Peerapat A, Sep 23, 2017
  */
object JDouble {

  type JDouble = java.lang.Double

  def apply(v: Double): JDouble = java.lang.Double.valueOf(v)

}
