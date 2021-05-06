import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtghpackages.GitHubPackagesKeys._
import sbtghpackages.TokenSource

object Settings {
  val githubTokenSourceSetting =
    githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")

  lazy val settings = Seq(
    organization := "rat",
    githubOwner := "FCG-Tech",
    githubRepository := "play-json-optics",
    githubTokenSourceSetting,
    version := "0.2.0." + sys.props.getOrElse("buildNumber", default="0"),
    scalaVersion := "2.13.1",
    crossScalaVersions := Seq("2.12.10", scalaVersion.value),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    assemblyJarName in assembly := "playjsonoptics-" + version.value + ".jar",
    test in assembly := {},
    target in assembly := file(baseDirectory.value + "/../bin/"),
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(
      includeScala = false,
      includeDependency=true),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs@_*) => MergeStrategy.discard
      case n if n.startsWith("reference.conf") => MergeStrategy.concat
      case _ => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.github.julien-truffaut" %%  "monocle-core"  % "2.1.0",
      "org.typelevel" %% "alleycats-core" % "2.2.0",
      "org.scalatest" %% "scalatest" % "3.2.3" % Test
    ),
    scalacOptions ++= Seq("-language:higherKinds"),
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
  )

  lazy val testSettings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )
}
