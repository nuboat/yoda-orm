organization := "in.norbor"

name := "yoda-orm"

version := "5.1.0"

scalaVersion := "2.13.5"

updateOptions := updateOptions.value.withGigahorse(false)

scalacOptions := Seq("-feature", "-deprecation", "-unchecked", "-Xlint")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "in.norbor" %% "yoda-common" % "1.0.1"
  , "com.zaxxer" % "HikariCP" % "4.0.3" % Compile
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2" % Compile
  , "javax.inject" % "javax.inject" % "1" % Compile
  , "org.apache.velocity" % "velocity" % "1.7" % Compile
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.200" % Test
  , "org.scalatest" %% "scalatest" % "3.2.6" % Test
  , "org.postgresql" % "postgresql" % "42.2.18" % Test
)

parallelExecution in Test := false

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
