import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.avalander"
ThisBuild / organizationName := "avalander"

lazy val root = (project in file("."))
  .settings(
    name := "scala-disco",
    libraryDependencies ++= Seq(
      discord4j,
      logback,
      scalaTest % Test,
    )
  )
