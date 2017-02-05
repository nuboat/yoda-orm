organization := "nuboat"

name := "scala-sql"

version := "0.1"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.193"
  , "joda-time" % "joda-time" % "2.9.7"
  , "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

crossScalaVersions := Seq("2.11.8", "2.12.1")
