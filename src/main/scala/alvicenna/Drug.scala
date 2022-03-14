package alvicenna

class Drug {

  import java.nio.file.{Files, Paths}
  import scala.io.StdIn.readLine

    //class configuration
    def className = this.getClass.getSimpleName
    var tag = Array("drugs", className.toLowerCase())
    var elements = 3


    //class properties section
    var drugId:Int = 0

    var drugName: String = ""
    var typeOfdrug: String = ""
    var remarks:String = ""

    //map
    var dataMap:Map[Int,(String ,String,String)] = null

    //private variables section
    var query = ""
    var executeQuery:ExecuteQuery = null
    var path = ""
    var rowValue = ""
    var DMLValues = "drugName, typeOfdrug, remarks"



    //class methods section


    //Delete drug
    def delete(): Unit={
      read()
      var rowId = ""
      if(dataMap != null) {
        var isValidId = false
        do{
          println(s"Give the Id of the $className to delete. (Type cancel to exit)")
          print("> ")
          rowId = readLine().trim()
          if(rowId.forall(_.isDigit) != true || dataMap.isDefinedAt(rowId.toInt) != true)
          {if(rowId.toLowerCase() != "cancel"){println()
            println("Invalid Entry")
            println()
          } else {
            isValidId = true
          }
          }else {
            isValidId = true
          }
        }while(!isValidId)
        if(rowId.toLowerCase() != "cancel"){
          try{
            query = ("DELETE FROM " + className + " WHERE "+ className +"Id = "+ rowId.toInt )
            executeQuery = new ExecuteQuery(query, false)
            println(s"$className ${dataMap(rowId.toInt)._1} ${dataMap(rowId.toInt)._2} deleted")
          }catch {
            case e: Throwable => e.printStackTrace
              println(s"Error deleting $className")
          }finally {
            if(executeQuery.connection != null)executeQuery.connection.close()
          }
        }
      }else{
        println()
        println(s"Table $className is empty")
      }
      //end delete
    }



    //read JSON file
    def readJSON(): Unit={

      var defaultPath = ""
      while(defaultPath != "Y" && defaultPath != "N" && defaultPath != "CANCEL"){
        println("Read JSON file from default directory? (Choose y,  n, or cancel)")
        print("> ")
        defaultPath = readLine().trim().toUpperCase()
      }

      if(defaultPath == "Y"){
        path = System.getProperty("user.dir")+"/JSONFiles/"+className.toLowerCase()+".json"
      } else if(defaultPath == "N"){
        while(path.toLowerCase().contains(className.toLowerCase()+".json") != true && path.toLowerCase() != "cancel"){
          println("Give de location of file \""+className.toLowerCase()+".json\". (Type cancel to exit)")
          print("> ")
          path = readLine().trim()
        }
      }

      if(Files.exists(Paths.get(path))){

        var jasonreader = new JSONReader(path, tag, elements)

        // create query
        query = ("INSERT INTO " + className + " (" + DMLValues +
          ") Values ")
        for(i <- 0 to jasonreader.JSONData.length - 1){
          var partialQuery = query
          var rowValue = ""

          rowValue += "(" +
            "\"" + jasonreader.JSONData(i)(0) + "\"" +
            ",\"" + jasonreader.JSONData(i)(1) + "\"" +
            "," + reverseDate(jasonreader.JSONData(i)(2)) +
            ",\"" + jasonreader.JSONData(i)(3) + "\"" +
            ",\"" + jasonreader.JSONData(i)(4) + "\"" +
            ", " + Main.defaultPatient + ")"

          partialQuery += rowValue
          println(partialQuery)

          try{
            executeQuery = new ExecuteQuery(partialQuery, false)
          }catch {
            case e: Throwable => e.printStackTrace
              println(s"Error Creating $className ${jasonreader.JSONData(i)(0)} ${jasonreader.JSONData(i)(1)}")
          }finally {
            if(executeQuery.connection != null)executeQuery.connection.close()
          }

          //end for loop
        }
      } else {
        println("JSON file does not exists in the location.")
      }

      //end readJSON
    }


    //create
    def create(): Unit={

      try{
        println(s"Insert Values. ")
        println("Drug name?")
        print("> ")
        drugName = readLine().trim()
        println("Type of drug? (caplets, liquid, etc.)")
        print("> ")
        typeOfdrug = readLine().trim()
        println("Remarks")
        print("> ")
        remarks = readLine().trim()

        query = (s"INSERT INTO "+ className + " ("+ DMLValues +
          s""") VALUES("$drugName", "$typeOfdrug", "$remarks")""")
        println(query)
        executeQuery = new ExecuteQuery(query, false)

        println(s"$className created")
      }catch {
        case e: Throwable => e.printStackTrace
          println(s"Error Creating $className")
      }finally {
        if(executeQuery.connection != null)executeQuery.connection.close()
      }
      //end create
    }

    // Read
    def read(p_tableId: Int = 0): Unit={

      try{
        if(p_tableId == 0){
          query = "SELECT * FROM "+className
        } else{
          query = "SELECT * FROM "+className+" where " + className + "Id = "+p_tableId
        }
        executeQuery = new ExecuteQuery(query, true)

        var objectMap:scala.collection.mutable.Map[Int,(String, String ,String)]
        = scala.collection.mutable.Map()
        try while ( {
          executeQuery.resultSet.next
        }) {
          //load map with values
          drugId = executeQuery.resultSet.getInt("drugId")
          drugName = executeQuery.resultSet.getString("drugName")
          typeOfdrug = executeQuery.resultSet.getString("typeOfdrug")
          remarks = executeQuery.resultSet.getString("remarks")


          objectMap += (drugId -> (drugName,typeOfdrug,remarks))

          //Display values
          if(p_tableId == 0){
            println()
            System.out.print("Drug ID: " + drugId)
            System.out.print(" : " + drugName)
//            System.out.println(" : " + typeOfdrug)
//            System.out.println("Remarks: " + remarks)
          }

        }
        println()
        if(p_tableId != 0 && objectMap.size == 0){

          println(s"There is no $className with Id = "+p_tableId)
        } else if(p_tableId == 0 && objectMap.size == 0){
          println(s"Table $className is empty. Create a $className before proceed")
        } else {
          dataMap = objectMap.toMap
        }


      }catch {
        case e: Throwable => e.printStackTrace
          println("Error Reading All Patients")
      }finally {
        if(executeQuery.connection != null)executeQuery.connection.close()
      }
      //end read()
    }

    //private function section

    private def reverseDate(pdate: String): String={
      if(!pdate.isEmpty){
        var dateString = ""
        var startPosition = 0
        val MM = pdate.subSequence(startPosition,2)
        startPosition += 3
        val dd = pdate.subSequence(startPosition,5)
        startPosition += 3
        val yyyy = pdate.subSequence(6,pdate.length)
        dateString=(s""""$yyyy-$MM-$dd"""")
        return dateString
      } else{
        return "null"
      }

      //end reverseDate
    }


    //end Drug
}
