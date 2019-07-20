organization := "in.norbor"

name := "yoda-orm"

version := "4.0.5"

scalaVersion := "2.13.0"

updateOptions := updateOptions.value.withGigahorse(false)

scalacOptions := Seq("-feature", "-deprecation", "-unchecked", "-Xlint"
  , "-Ywarn-dead-code"
  , "-Ywarn-numeric-widen"
  , "-Ywarn-value-discard"
  , "-Ywarn-unused")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8", "-g:none")

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "3.3.1"
  , "com.typesafe" % "config" % "1.3.4"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  , "joda-time" % "joda-time" % "2.10.2"
  , "org.apache.velocity" % "velocity" % "1.7" % "compile"
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.199" % "test"
  , "org.scalatest" %% "scalatest" % "3.1.0-SNAP13" % "test"
  , "org.postgresql" % "postgresql" % "42.2.5" % "test"
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
