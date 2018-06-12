import Dependencies._

/* Global configuration */
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.trosa",
      scalaVersion := "2.12.2",
      version := "0.0.1"
    )),
    name := "goupil",
    libraryDependencies ++= Seq(
      scalaTest % Test
    )
  )

/* Boot endpoint */
mainClass in Compile := Some("io.trosa.goupil")

/* Scala lang related */
scalacOptions += "-deprecation"
scalacOptions += "-feature"