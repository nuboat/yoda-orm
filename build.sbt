organization := "in.norbor"

name := "yoda-orm"

version := "20201022.1"

scalaVersion := "2.13.2"

updateOptions := updateOptions.value.withGigahorse(false)

scalacOptions := Seq("-feature", "-deprecation", "-unchecked", "-Xlint"
  , "-Ywarn-dead-code"
  , "-Ywarn-numeric-widen"
  , "-Ywarn-value-discard"
  , "-Ywarn-unused")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "3.4.5" % Compile
  , "com.typesafe" % "config" % "1.4.0" % Compile
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2" % Compile
  , "in.norbor" %% "yoda-common" % "20200331"
  , "joda-time" % "joda-time" % "2.10.6" % Compile
  , "org.apache.velocity" % "velocity" % "1.7" % Compile
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.200" % Test
  , "org.scalatest" %% "scalatest" % "3.1.1" % Test
  , "org.postgresql" % "postgresql" % "42.2.11" % Test
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
