organization := "in.norbor"

name := "yoda-orm"

version := "4.0.0b"

scalaVersion := "2.13.0-RC3"

scalacOptions := Seq("-feature", "-deprecation", "-unchecked", "-Xlint"
  , "-Ywarn-dead-code"
  , "-Ywarn-numeric-widen"
  , "-Ywarn-value-discard"
  , "-Ywarn-unused")

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "3.2.0"
  , "com.typesafe" % "config" % "1.3.3"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  , "joda-time" % "joda-time" % "2.9.9"
  , "org.scala-lang" % "scala-reflect" % "2.13.0-RC3"
  , "org.apache.velocity" % "velocity" % "1.7" % "compile"
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.197" % "test"
  , "org.scalatest" %% "scalatest" % "3.1.0-SNAP12" % "test"
  , "org.postgresql" % "postgresql" % "42.2.2" % "test"
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
