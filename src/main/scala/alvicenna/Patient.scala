package alvicenna

import scala.io.StdIn.readLine
import java.nio.file.{Files, Paths}

class Patient {
  //class configuration
  def className = this.getClass.getSimpleName
  println("class: "+ className)
  var tag = Array("patients", className.toLowerCase())
  var elements = 5


  //class properties section
  var patientId:Int = 0
  var firstName:String = ""
  var lastName:String = ""
  var birthDate:String = ""
  var gender:String = ""
  var patientEmail:String = ""
  var dataMap:Map[Int,(String,String,String,String,String)] = null
  //private variables section
  var query = ""
  var executeQuery:ExecuteQuery = null
  var path = ""
  var rowValue = ""



  //class methods section

  //selectDefaultPatient
  def selectDefaultPatient(): Unit={
    Main.defaultPatient = 0
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
        Main.defaultPatient = rowId.toInt
        Main.defautlPatientMap = dataMap
      }
    }else{
      println()
      println(s"Table $className is empty")
    }
    //end selectDefaultPatient
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
      query = ("INSERT INTO patient (firstName, lastName, birthDate, gender, patientEmail, userId)" +
        " Values ")
      for(i <- 0 to jasonreader.JSONData.length - 1){
        var partialQuery = query
        var rowValue = ""

        rowValue += "(" +
          "\"" + jasonreader.JSONData(i)(0) + "\"" +
          ",\"" + jasonreader.JSONData(i)(1) + "\"" +
          "," + reverseDate(jasonreader.JSONData(i)(2)) +
          ",\"" + jasonreader.JSONData(i)(3) + "\"" +
          ",\"" + jasonreader.JSONData(i)(4) + "\"" +
          ", " + Main.defaultUser + ")"

        partialQuery += rowValue
        println(partialQuery)

        try{
        executeQuery = new ExecuteQuery(partialQuery, false)
        }catch {
        case e: Throwable => e.printStackTrace
          println(s"Error Creating Patient ${jasonreader.JSONData(i)(0)} ${jasonreader.JSONData(i)(1)}")
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
      println("Patient First Name?")
      print("> ")
      firstName = readLine().trim()
      println("Last Name?")
      print("> ")
      lastName = readLine().trim()
      println("Date of Birth? format(MM/dd/yyyy")
      print("> ")
      birthDate = readLine().trim()
      println("Email?")
      print("> ")
      patientEmail = readLine().trim()
      println("Gender?")
      print("> ")
      gender = readLine().trim()
      birthDate = reverseDate(birthDate)
      query = (s"INSERT INTO patient (firstName, lastName, birthDate, gender, patientEmail, userId)" +
        s""" VALUES("$firstName", "$lastName", $birthDate, "$gender", "$patientEmail", ${Main.defaultUser})""")
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
        query = "SELECT * FROM "+className+" where userId = "+Main.defaultUser
      } else{
        query = "SELECT * FROM "+className+" where userId = "+Main.defaultUser+ " and "+className+"Id = "+p_tableId
      }
      executeQuery = new ExecuteQuery(query, true)

      var objectMap:scala.collection.mutable.Map[Int,(String,String,String,String,String)]
      = scala.collection.mutable.Map(0 -> (null,null,null,null,null))
      objectMap -= 0
      try while ( {
        executeQuery.resultSet.next
      }) {
        //load map with values
        patientId = executeQuery.resultSet.getInt("patientId")
        firstName = executeQuery.resultSet.getString("firstName")
        lastName = executeQuery.resultSet.getString("lastName")
        birthDate = executeQuery.resultSet.getString("birthDate")
        gender = executeQuery.resultSet.getString("gender")
        patientEmail = executeQuery.resultSet.getString("patientEmail")

        objectMap += (patientId -> (firstName,lastName,birthDate,gender,patientEmail))

        //Display values
        System.out.print("Patient ID: " + patientId)
        System.out.print(", Name: " + firstName)
        System.out.print(" " + lastName)
        System.out.print(", Birth Date: " + birthDate)
        System.out.print(", Gender: " + gender)
        System.out.println(", Email: " + patientEmail)
      }
      println()
      if(p_tableId != 0 && objectMap.size == 0){

        println(s"There is no $className with Id = "+p_tableId)
      } else if(p_tableId == 0 && objectMap.size == 0){
        println(s"Table $className is empty. Create a $className before proceed")
      } else if(p_tableId != 0 && objectMap.size == 1){
        dataMap = objectMap.toMap
      }
      else {
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


//end Patient
}
