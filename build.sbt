ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "alvicenna"
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.2"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "3.1.2"

//connect mysql
// past working libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.25"
// update
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"
