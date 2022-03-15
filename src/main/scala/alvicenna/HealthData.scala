package alvicenna

import scala.io.StdIn.readLine
import java.nio.file.{Files, Paths}
import scala.Console.{BLINK, RED_B, RESET}

class HealthData {
  //class configuration
  def className = this.getClass.getSimpleName
  println("class: "+ className)
  var tag = Array("healthdata", className.toLowerCase())
  var elements = 7


  //class properties section
  var healthDataId:Int = 0

  var temperature: Float = 0
  var pulse: Int = 0
  var readingTime:String = ""
  var observations:String = ""
  var systolic: Int = 0
  var diastolic: Int = 0
  var oxygenLevel : Float = 0

  //map
  var dataMap:Map[Int,(Float, Int, String ,String,Int,Int,Float)] = null

  //private variables section
  var query = ""
  var executeQuery:ExecuteQuery = null
  var path = ""
  var rowValue = ""
  var DMLValues = "temperature, pulse, readingtime, observations, systolic, diastolic, oxygenlevel, patientId"



  //class methods section

  //Delete patient
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
      println(path)
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
          jasonreader.JSONData(i)(0) +
          "," + jasonreader.JSONData(i)(1) +
          "," + reverseDate(jasonreader.JSONData(i)(2)) +
          ",\"" + jasonreader.JSONData(i)(3) + "\"" +
          "," + jasonreader.JSONData(i)(4) +
          "," + jasonreader.JSONData(i)(5) +
          "," + jasonreader.JSONData(i)(6) +
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
      println(s"Insert Values. All fields are ${RESET}${RED_B}${BLINK}mandatory${RESET}.")
      println("Temperature?")
      print("> ")
      temperature = readLine().trim().toFloat
      println("Pulse?")
      print("> ")
      pulse = readLine().trim().toInt
      println("Date of reading? format(MM/dd/yyyy")
      print("> ")
      readingTime = readLine().trim()
      println("Systolic value? (pressure reading first value)")
      print("> ")
      systolic = readLine().trim().toInt
      println("Diastolic value? (pressure reading second value)")
      print("> ")
      diastolic = readLine().trim().toInt
      println("Oxygen level?")
      print("> ")
      oxygenLevel = readLine().trim().toFloat
      println("Observations?")
      print("> ")
      observations = readLine().trim()

      readingTime = reverseDate(readingTime)

      query = (s"INSERT INTO "+ className + " ("+ DMLValues +
        s""") VALUES("$temperature", "$pulse", $readingTime, "$observations", "$systolic", "$diastolic", "$oxygenLevel", ${Main.defaultPatient})""")
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
        query = "SELECT * FROM "+className+" where patientId = "+Main.defaultPatient
      } else{
        query = "SELECT * FROM "+className+" where patientId = "+Main.defaultPatient+ " and "+className+"Id = "+p_tableId
      }
      executeQuery = new ExecuteQuery(query, true)

      var objectMap:scala.collection.mutable.Map[Int,(Float, Int, String ,String,Int,Int,Float)]
      = scala.collection.mutable.Map(0 -> (0, 0, null ,null,0,0,0))
      objectMap -= 0
      try while ( {
        executeQuery.resultSet.next
      }) {
        //load map with values
        healthDataId = executeQuery.resultSet.getInt("healthDataId")
        temperature = executeQuery.resultSet.getFloat("temperature")
        pulse = executeQuery.resultSet.getInt("pulse")
        readingTime = executeQuery.resultSet.getString("readingTime")
        observations = executeQuery.resultSet.getString("observations")
        systolic = executeQuery.resultSet.getInt("systolic")
        diastolic = executeQuery.resultSet.getInt("diastolic")
        oxygenLevel = executeQuery.resultSet.getFloat("oxygenLevel")

        objectMap += (healthDataId -> (temperature,pulse,readingTime,observations,systolic,diastolic,oxygenLevel))

        //Display values
        println()
        System.out.println("Health Data ID: " + healthDataId)
        System.out.println("Reading Date: " + readingTime.subSequence(0,10))
        System.out.print("Temperature: " + temperature)
        System.out.println(", Pulse: " + pulse)
        System.out.print("Blood Pressure: " + systolic)
        System.out.print(" x " + diastolic)
        System.out.println(" Oxygen Level: " + oxygenLevel)
        System.out.print("Observations: " + observations)
        println()
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


  //end HealthData
}
