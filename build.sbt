val Http4sVersion = "0.23.14"
val CirceVersion = "0.14.2"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.11"
val MunitCatsEffectVersion = "1.0.7"

lazy val root = (project in file("."))
  .settings(
    organization := "com.jules",
    name := "weather",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.3",
      "io.circe" %% "circe-generic" % "0.14.3",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.0",
      "com.softwaremill.sttp.client3" %% "circe" % "3.8.0",
      "com.softwaremill.sttp.client3" %% "http4s-backend" % "3.8.2",
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.1.1",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.1.1",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.1.2",
      "com.beachape" %% "enumeratum" % "1.7.0",
      "com.beachape" %% "enumeratum-circe" % "1.7.0",
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion         % Runtime,
      "org.scalameta"   %% "svm-subs"            % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
