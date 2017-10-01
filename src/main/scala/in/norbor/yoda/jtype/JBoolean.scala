package in.norbor.yoda.jtype

/**
  * Peerapat A, Sep 23, 2017
  */
object JBoolean {

  type JBoolean = java.lang.Boolean

  def apply(v: Boolean): JBoolean = java.lang.Boolean.valueOf(v)

}
