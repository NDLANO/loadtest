import java.util.Properties

val Scalaversion = "2.11.8"

val appProperties = settingKey[Properties]("The application properties")

appProperties := {
  val prop = new Properties()
  IO.load(prop, new File("build.properties"))
  prop
}

lazy val commonSettings = Seq(
  organization := appProperties.value.getProperty("NDLAOrganization"),
  version := appProperties.value.getProperty("NDLAComponentVersion"),
  scalaVersion := Scalaversion
)

lazy val loadtest = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "loadtest",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions := Seq("-target:jvm-1.8"),
    libraryDependencies ++= Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % "test",
      "io.gatling" % "gatling-test-framework" % "2.2.2" % "test",
      "org.scalaj" %% "scalaj-http" % "2.3.0",
      "ndla" %% "network" % "0.17"
    )
  ).enablePlugins(GatlingPlugin)
