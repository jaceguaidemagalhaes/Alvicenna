ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.12.15"

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