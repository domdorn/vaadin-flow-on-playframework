import Vaadin.Dependencies.org
import Vaadin.Versions._
import sbt.librarymanagement.ModuleID

name := """play-vaadin-10-poc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
  .settings(
  publishArtifact in (Compile, packageDoc) := false,
  publishArtifact in packageDoc := false,
  sources in (Compile,doc) := Seq.empty
)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

resolvers += "vaadin-prereleases" at "https://maven.vaadin.com/vaadin-prereleases"
resolvers += "vaadin-addons" at "http://maven.vaadin.com/vaadin-addons"

libraryDependencies += guice
libraryDependencies += ehcache

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

libraryDependencies +=  Vaadin.Dependencies.vaadinCore
libraryDependencies += Vaadin.Dependencies.slf4jSimple
libraryDependencies += "commons-beanutils" % "commons-beanutils" % "1.9.2"
libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0"

// https://mvnrepository.com/artifact/org.apache.geronimo.specs/geronimo-servlet_3.0_spec
libraryDependencies += "org.apache.geronimo.specs" % "geronimo-servlet_3.0_spec" % "1.0" % "provided"

  // Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))


// hmm... this is supposed to prevent eviction (e.g. pulling newer versions than those that are
// supported... I'm not 100% sure it works like it should) // TODO !
libraryDependencies ++=  {
  import Vaadin.Dependencies._
  Seq(flowHtmlComponents,
    vaadinButtonFlow,
    vaadinCheckboxFlow,
    vaadinComboBoxFlow,
    vaadinDatePickerFlow,
    vaadinDialogFlow,
    vaadinFormLayoutFlow,
    vaadinGridFlow,
    vaadinIconsFlow,
    vaadinIronListFlow,
    vaadinListBoxFlow,
    vaadinNotificationFlow,
    vaadinOrderedLayoutFlow,
    vaadinProgressBarFlow,
    vaadinRadioButtonFlow,
    vaadinSplitLayoutFlow,
    vaadinTabsFlow,
    vaadinTextFieldFlow,
    vaadinUploadFlow )
}
