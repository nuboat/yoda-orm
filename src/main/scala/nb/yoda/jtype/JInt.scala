package nb.yoda.jtype

/**
  * Peerapat A, Sep 23, 2017
  */
object JInt {

  type JInt = java.lang.Integer

  def apply(v: Integer): JInt = java.lang.Integer.valueOf(v)

}
