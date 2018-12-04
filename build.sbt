organization := "in.norbor"

name := "yoda-orm"

version := "4.0.0b"

scalaVersion := "2.12.7"

scalacOptions := Seq("-feature", "-deprecation", "-unchecked", "-Xlint"
  , "-Ywarn-dead-code"
  , "-Ywarn-inaccessible"
  , "-Ywarn-nullary-override"
  , "-Ywarn-numeric-widen"
  , "-Ywarn-value-discard"
  , "-Ywarn-unused")

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "3.2.0"
  , "com.typesafe" % "config" % "1.3.3"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
  , "joda-time" % "joda-time" % "2.9.9"
  , "org.scala-lang" % "scala-reflect" % scalaVersion.value
//  , "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0" % "compile"
  , "org.apache.velocity" % "velocity" % "1.7" % "compile"
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.197" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  , "org.postgresql" % "postgresql" % "42.2.2" % "test"
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
