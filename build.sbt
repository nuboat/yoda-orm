organization := "in.norbor"

name := "yoda-orm"

version := "3.0.4"

scalaVersion := "2.12.6"

scalacOptions := Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.5"
  , "com.typesafe" % "config" % "1.3.3"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
  , "de.svenkubiak" % "jBCrypt" % "0.4.1"
  , "joda-time" % "joda-time" % "2.9.9"
  , "org.apache.velocity" % "velocity" % "1.7"
  , "org.apache.velocity" % "velocity-tools" % "2.0"
  , "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.197" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  , "org.postgresql" % "postgresql" % "42.2.2" % "test"
)

parallelExecution in Test := false

crossScalaVersions := Seq("2.11.11", "2.12.6")

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
