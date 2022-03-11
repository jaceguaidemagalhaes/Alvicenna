package alvicenna

import scala.io.StdIn.readLine
import java.nio.file.{Files, Paths}

class Patient {
  //class properties section
  var patientId:Int = 0
  var firstName:String = ""
  var lastName:String = ""
  var birthDate:String = ""
  var gender:String = ""
  var patientEmail:String = ""
  var userId = 1

  //private variables section
  var query = ""
  var executeQuery:ExecuteQuery = null
  var path = ""
  var rowValue = ""

  //class methods section

  //read JSON file
  def readJSON(): Unit={

  var defaultPath = ""
    while(defaultPath != "Y" && defaultPath != "N" && defaultPath != "CANCEL"){
      println("Read JSON file from default directory? (Choose y,  n, or cancel)")
      print("> ")
      defaultPath = readLine().trim().toUpperCase()
    }

    if(defaultPath == "Y"){
      path = System.getProperty("user.dir")+"/JSONFiles/patient.json"
    } else if(defaultPath == "N"){
      while(path.toLowerCase().contains("patient.json") != true && path.toLowerCase() != "canceln" ){
        println("Give de location of file \"patient.json\". (Type cancel to exit)")
        print("> ")
        path = readLine().trim()
      }
    }

    if(Files.exists(Paths.get(path))){
        var tag = Array("patients", "patient")
        var elements = 5
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
          ", " + userId + ")"

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





//        println(jasonreader.JSONData.length-2)
//        println(jasonreader.JSONData(0)(0))
//        println(jasonreader.JSONData(0)(1))
//        println(jasonreader.JSONData(0)(2))
//        println(jasonreader.JSONData(0)(3))
//        println(jasonreader.JSONData(0)(4))
//        println(jasonreader.JSONData.length-1)
//        println(jasonreader.JSONData(1)(0))
//        println(jasonreader.JSONData(1)(1))
//        println(jasonreader.JSONData(1)(2))
//        println(jasonreader.JSONData(1)(3))
//        println(jasonreader.JSONData(1)(4))
//        println(jasonreader.JSONData.length-0)
//        println(jasonreader.JSONData(2)(0))
//        println(jasonreader.JSONData(2)(1))
//        println(jasonreader.JSONData(2)(2))
//        println(jasonreader.JSONData(2)(3))
//        println(jasonreader.JSONData(2)(4))
    } else {
        println("JSON file does not exists in the location.")
    }


    //end readJSON
  }


  //create patient
  def create(): Unit={

    try{
      println("Patient First Name?")
      firstName = readLine().trim()
      println("Last Name?")
      lastName = readLine().trim()
      println("Date of Birth? format(MM/dd/yyyy")
      birthDate = readLine().trim()
      println("Email?")
      patientEmail = readLine().trim()
      println("Gender?")
      gender = readLine().trim()
      birthDate = reverseDate(birthDate)
      query = (s"INSERT INTO patient (firstName, lastName, birthDate, gender, patientEmail, userId)" +
        s""" VALUES("$firstName", "$lastName", $birthDate, "$gender", "$patientEmail", $userId)""")
      executeQuery = new ExecuteQuery(query, false)
      println(s"Patient $firstName $lastName created")
    }catch {
      case e: Throwable => e.printStackTrace
      println("Error Creating Patient")
    }finally {
      if(executeQuery.connection != null)executeQuery.connection.close()
    }
    //end create patient
  }

  def readAll(): Unit={

    try{
      query = "SELECT * FROM patient"
      executeQuery = new ExecuteQuery(query, true)
      try while ( {
        executeQuery.resultSet.next
      }) {
        //Display values
        System.out.print("Patient ID: " + executeQuery.resultSet.getInt("patientId"))
        System.out.print(", Name: " + executeQuery.resultSet.getString("firstName"))
        System.out.print(" " + executeQuery.resultSet.getString("lastName"))
        System.out.print(", Birth Date: " + executeQuery.resultSet.getString("birthDate"))
        System.out.print(", Gender: " + executeQuery.resultSet.getString("gender"))
        System.out.println(", Email: " + executeQuery.resultSet.getString("patientEmail"))
      }
    }catch {
      case e: Throwable => e.printStackTrace
        println("Error Reading All Patients")
    }finally {
      if(executeQuery.connection != null)executeQuery.connection.close()
    }
    //end readAll patients
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
