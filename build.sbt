organization := "nb"

name := "yoda-orm"

version := "1.2.3"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.9"
  , "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.194" % "test"
  , "net.sourceforge.jtds" % "jtds" % "1.3.1" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

crossScalaVersions := Seq("2.11.11", "2.12.1")

publishTo := Some("Artifactory Realm" at "https://artifact.billme.in.th/artifactory/billme-public")

credentials += Credentials("Artifactory Realm", "artifact.billme.in.th", "admin", "")
