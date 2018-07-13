import Dependencies._

resolvers += "jitpack" at "https://jitpack.io"

/* Global configuration */
lazy val root = (project in file(".")).settings(
    inThisBuild(
        List(
            organization := "io.trosa",
            scalaVersion := "2.12.2",
            version := "0.0.1"
        )),
    name := "goupil",
    libraryDependencies ++= Seq(
        scalaTest % Test,
        cats,
        akka,
        slack,
        sl4j
    ),
    test in assembly := {},
    mainClass in assembly := Some("io.trosa.goupil.Kernel"),
    assemblyJarName in assembly := "goupil-latest.jar"
)

/* Boot endpoint */
mainClass in Compile := Some("io.trosa.goupil.Kernel")

/* Scala lang related */
scalacOptions += "-deprecation"
scalacOptions += "-feature"

/* Packaging & Containers */
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}
