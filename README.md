# Alvicenna
Personal Health Manger CLI APP (Scala)

This a console based app built as part of the training program in Big-Data, Scala/Sparks given by Revature.

The system was built using scala 2.12.15, using JDBC to connect with a MYSQL 8 database.

The system controls basic health and prescription information. The user can insert values direct in the console or reading a json file located in the JSONFiles directory.

More information about the classes and the data moidel are located in the documents directory in a Class Diagram and a ERD.

Follow the below step to replicate it:
- download/clone the repository
- create a new database called alvicenna
- create a user to acces the new schema
- create the schema using the database.sql script located in the sqlScripts directory
- update the ExecuteQuery class with the username and password to acces the new database
- import the drugs name from drugcodes.csv into the column name in table Drug
- run the object Main.
