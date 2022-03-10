package alvicenna

import java.sql.SQLException

object Main extends App {
    var helloword = new HelloWord()
    helloword
    var data = new ExecuteQuery("SELECT id, first, last, age FROM Registration")
    try while ( {
        data.resultSet.next
    }) { //Display values
        System.out.print("ID: " + data.resultSet.getInt("id"))
        System.out.print(", Age: " + data.resultSet.getInt("age"))
        System.out.print(", First: " + data.resultSet.getString("first"))
        System.out.println(", Last: " + data.resultSet.getString("last"))
    }
    catch {
        case e: SQLException =>
            e.printStackTrace()
    }finally {
        if(data.connection != null)data.connection.close()
    }
}
