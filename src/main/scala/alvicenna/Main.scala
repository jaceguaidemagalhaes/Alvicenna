package alvicenna

import scala.sys.process._

object Main extends App {
    print("\u001b[2J")
//    var helloword = new HelloWord()
//    helloword

    // Patien Class
    //var patient = new Patient()
    //patient.readAll()
    //    call create patient
    //    patient.create()

    // JSONReader class
    var path = "/Users/jay85mag/Library/CloudStorage/OneDrive-Personal/00Revature/projects/PHM_CLI/documents/patient2.json"
    var tag = Array("patients", "patient")
    var elements = 5
    var jasonreader = new JSONReader(path, tag, elements)
    println(jasonreader.JSONData.length-2)
    println(jasonreader.JSONData(0)(0))
    println(jasonreader.JSONData(0)(1))
    println(jasonreader.JSONData(0)(2))
    println(jasonreader.JSONData(0)(3))
    println(jasonreader.JSONData(0)(4))
    println(jasonreader.JSONData.length-1)
    println(jasonreader.JSONData(1)(0))
    println(jasonreader.JSONData(1)(1))
    println(jasonreader.JSONData(1)(2))
    println(jasonreader.JSONData(1)(3))
    println(jasonreader.JSONData(1)(4))
    println(jasonreader.JSONData.length-0)
    println(jasonreader.JSONData(2)(0))
    println(jasonreader.JSONData(2)(1))
    println(jasonreader.JSONData(2)(2))
    println(jasonreader.JSONData(2)(3))
    println(jasonreader.JSONData(2)(4))

}
