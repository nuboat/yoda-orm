package in.norbor.yoda.generator

/**
  * @author Peerapat A
  */
case class Target(target: String, packages: Array[String]) {

  def directoryName: String = s"$target/${packages.mkString("/")}"

  def packageName: String = packages.mkString(".")

}
