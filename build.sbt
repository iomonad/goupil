import Dependencies._

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
