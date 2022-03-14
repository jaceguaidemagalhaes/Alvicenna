package alvicenna

import java.nio.file.{Files, Paths}
import scala.io.StdIn.readLine

class Prescription {
  //class configuration
  def className = this.getClass.getSimpleName
  println("class: "+ className)
  var tag = Array("prescriptions", className.toLowerCase())
  var elements = 2


  //class properties section
  var prescriptionId:Int = 0

  var doctor: String = ""
  var prescriptionDate: String = ""
  var active: Boolean = true
  var dateLastFilling: String = ""
  var quantityLastFilling: Int = 0

  //map
  var dataMap:Map[Int,(String, String, Boolean ,String,Int)] = null

  //private variables section
  var query = ""
  var executeQuery:ExecuteQuery = null
  var path = ""
  var rowValue = ""
  var DMLValues = "doctor, prescriptionDate, active, dateLastFilling, quantityLastFilling, patientId"



  //class methods section

  def selectDefaultPrescription(): Unit={
    Main.defaultPrescription = 0
    read()
    var rowId = ""
    if(dataMap != null) {
      var isValidId = false
      do{
        println(s"Select the $className ID. (Type cancel to exit)")
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
        read(rowId.toInt)
        Main.defaultPrescription = rowId.toInt
        Main.defaultPrescriptionMap = dataMap
      }
    }else{
      println()
      println(s"Table $className is empty")
    }
    //end selectDefaultPrescription
  }

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
      path = System.getProperty("user.dir")+"/JSONFiles/+"+className.toLowerCase()+".json"
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
      query = ("INSERT INTO " + className + " (" + "doctor, prescriptionDate, patientId" +
        ") Values ")
      for(i <- 0 to jasonreader.JSONData.length - 1){
        var partialQuery = query
        var rowValue = ""

        rowValue += "(" +
          "\"" + jasonreader.JSONData(i)(0) + "\"" +
          ",\"" + jasonreader.JSONData(i)(1) + "\"" +
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
      println(s"Insert Values.")
      println("Doctor?")
      print("> ")
      doctor = readLine().trim()
      println("Prescription date? (MM/dd/yyyy)")
      print("> ")
      prescriptionDate = readLine().trim()

      prescriptionDate = reverseDate(prescriptionDate)
      dateLastFilling = reverseDate(dateLastFilling)

      query = (s"INSERT INTO "+ className + " ("+ "doctor, prescriptionDate, patientId" +
        s""") VALUES("$doctor", $prescriptionDate, ${Main.defaultPatient})""")
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
        query = "SELECT * FROM "+className+" where prescriptionId = "+p_tableId
      }
      executeQuery = new ExecuteQuery(query, true)

      var objectMap:scala.collection.mutable.Map[Int,(String, String, Boolean ,String,Int)]
      = scala.collection.mutable.Map()

      try while ( {
        executeQuery.resultSet.next
      }) {
        //load map with values
        prescriptionId = executeQuery.resultSet.getInt("prescriptionId")
        doctor = executeQuery.resultSet.getString("doctor")
        prescriptionDate = executeQuery.resultSet.getString("prescriptionDate")
        active = executeQuery.resultSet.getBoolean("active")
        dateLastFilling = executeQuery.resultSet.getString("dateLastFilling")
        quantityLastFilling = executeQuery.resultSet.getInt("quantityLastFilling")

        objectMap += (prescriptionId -> (doctor,prescriptionDate,active,dateLastFilling,quantityLastFilling))

        //Display values
        println()
        System.out.println("Prescription ID: " + prescriptionId)
        System.out.print("Doctor: " + doctor)
        System.out.print(" Prescription Date: " + prescriptionDate)

        //future implementations
//        System.out.println(", active: " + active)
//        System.out.println(" date Last Filling " + dateLastFilling)
//        System.out.println("quantityLastFilling: " + obserquantityLastFillingvations)
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


  //end prescription
}
