organization := "com.lespea"

name := "challegeHelper"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-optimise", "-unchecked", "-deprecation")

libraryDependencies ++= {
  Seq(
    "com.typesafe"      %% "scalalogging-slf4j" % "1.0.1",
    "com.typesafe.akka" %% "akka-actor"         % "2.1.2",
    "org.clapper"       %% "grizzled-scala"     % "1.1.3",
    "org.scala-stm"     %% "scala-stm"          % "0.7",
    "org.scalatest"     %% "scalatest"          % "1.9.1",
    "commons-io"         % "commons-io"         % "2.4",
    "commons-codec"      % "commons-codec"      % "1.7",
    "commons-net"        % "commons-net"        % "3.2",
    "joda-time"          % "joda-time"          % "2.2"
  )
}

//"org.scalaz"         % "scalaz-core_2.9.1" % "6.0.4",
