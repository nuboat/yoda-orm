organization := "in.norbor"

name := "yoda-orm"

version := "2.3.1"

scalaVersion := "2.12.4"

scalacOptions := Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.1"
  , "com.typesafe" % "config" % "1.3.1"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  , "de.svenkubiak" % "jBCrypt" % "0.4.1"
  , "joda-time" % "joda-time" % "2.9.9"
  , "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.194" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

parallelExecution in Test := false

crossScalaVersions := Seq("2.11.11", "2.12.4")

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
