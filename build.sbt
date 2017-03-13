organization := "nuboat"

name := "scala-sql"

version := "0.4"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.7"
  , "com.h2database" % "h2" % "1.4.193" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

crossScalaVersions := Seq("2.11.8", "2.12.1")

publishTo := Some("Artifactory Realm" at "https://artifact.billme.in.th/artifactory/billme-public")

credentials += Credentials("Artifactory Realm", "artifact.billme.in.th", "admin", "")
