lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean, PlayEnhancer)
  .settings(
    name := """play-java-starter-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice,
      // Test Database
      "com.h2database" % "h2" % "1.4.199",
      javaJdbc, 
      //"mysql" % "mysql-connector-java" % "8.0.27",
      "org.postgresql" % "postgresql" % "42.1.4",
      
      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    ),
    // Make verbose tests
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

libraryDependencies += "org.flywaydb" %% "flyway-play" % "7.5.0"
libraryDependencies += "io.vavr" % "vavr" % "0.10.2"
