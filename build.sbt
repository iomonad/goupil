import Dependencies._

/*
 * Core project configuration
 */

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.trosa",
      scalaVersion := "2.12.6",
      version      := "0.1.0"
    )),
    name := "snat",
    libraryDependencies += scalaTest % Test
  )

/*
 * Dependencies
 */

resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
)
libraryDependencies += "com.github.Ullink" % "simple-slack-api" % "1.2.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
