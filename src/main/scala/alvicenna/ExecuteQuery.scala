package alvicenna
import java.sql.{Connection, DriverManager, ResultSet}

class ExecuteQuery(query: String) {

      // connect to the database named "mysql" on the localhost
      //val driver = "com.mysql.jdbc.Driver"
      val driver = "com.mysql.cj.jdbc.Driver"
      val url = "jdbc:mysql://localhost:3306/sql_homework"
      val username = "jaceguai"
      val password = "password"

      // there's probably a better way to do this
      var connection:Connection = null
      var resultSet:ResultSet = null

      val startQ = System.currentTimeMillis
      println("Executing query...")

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        resultSet = result

        val durationQ = System.currentTimeMillis - startQ
        println(s"Query executed. Execution time in miliseconds: $durationQ ms")
      } catch {
        case e: Throwable => e.printStackTrace
      } finally {
        //if(connection != null)connection.close()
      }


}
