organization := "com.lespea"

name := "challegeHelper"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1-1"

scalacOptions ++= Seq("-optimise", "-unchecked", "-deprecation")

crossScalaVersions := Seq("2.9.1", "2.9.1-1")


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies += "org.scalaz" % "scalaz-core_2.9.1" % "6.0.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0"