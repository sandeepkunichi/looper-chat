name := """looper-chat"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.typesafe.akka" %% "akka-remote" % "2.4.12",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.12",
  "com.hazelcast" % "hazelcast-client" % "3.8.4"
)
