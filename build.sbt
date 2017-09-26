organization := "in.norbor"

name := "yoda-orm"

version := "1.5.1"

scalaVersion := "2.12.3"

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  , "joda-time" % "joda-time" % "2.9.9"
  , "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.194" % "test"
  , "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

parallelExecution in Test := false

crossScalaVersions := Seq("2.11.11", "2.12.3")

//publishTo := Some("Artifactory Realm" at "https://artifact.billme.in.th/artifactory/billme-public")

//credentials += Credentials("Artifactory Realm", "artifact.billme.in.th", "admin", "")

publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
