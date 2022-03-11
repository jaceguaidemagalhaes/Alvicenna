package alvicenna

import scala.io.StdIn.readLine

class Patient {
  //properties
  var patientId:Int = 0
  var firstName:String = ""
  var lastName:String = ""
  var birthDate:String = ""
  var gender:String = ""
  var patientEmail:String = ""
  var userId = 1

  //variables
  var query = ""
  var executeQuery:ExecuteQuery = null

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
        s""" VALUES("$firstName", "$lastName", "$birthDate", "$gender", "$patientEmail", $userId)""")
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


  def reverseDate(pdate: String): String={
    var dateString = ""
    var startPosition = 0
    val MM = pdate.subSequence(startPosition,2)
    startPosition += 3
    val dd = pdate.subSequence(startPosition,5)
    startPosition += 3
    val yyyy = pdate.subSequence(6,pdate.length)
    dateString=(s"$yyyy-$MM-$dd")
    return dateString
  //end reverseDate
  }


//end class
}
