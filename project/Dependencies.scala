import sbt._

object Dependencies {

  	/*
  	* Versions
  	* */

	lazy val testVersion = "3.0.1"
  	lazy val catsVersion = "1.0.1"
	lazy val akkaVersion = "2.5.13"
	lazy val slackVersion = "1.2.0"

  	/*
  	* Dependencies
  	* */

  	lazy val scalaTest = "org.scalatest" %% "scalatest" % testVersion
  	lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
	lazy val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
	lazy val slack = "com.github.Ullink" % "simple-slack-api" % slackVersion
}
