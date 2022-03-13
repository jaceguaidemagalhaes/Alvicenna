package alvicenna

//import scala.Console.{BLUE, BOLD, RED, RESET}
import scala.io.AnsiColor._
import scala.io.StdIn.readLine
import scala.sys.process._
object Main extends App {
    // clear screen and goes to the end of screen
    //print("\u001b[2J")

    // system configuration properties
    var defaultUser = 1
    var defaultPatient = 0
    var defautlPatientMap:Map[Int,(String,String,String,String,String)] = null
    var activity = ""
    var option = ""
    var exit = false
    var message = ""

    // main screen manager
    topScreen()
    do{
        message = ""
        mainScreen()
        topScreen()
        println(s"${RESET}${RED}$message${RESET}")
    }
    while (!exit)

    // screen formating
    def mainScreen(): Unit ={
        println(s"               ${RESET}${GREEN_B}${BOLD}Main Screen${RESET}")
        println()
        println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
        println()
        println(s"${RESET}${BLUE}1 -> Patient${RESET}")
        println(s"${RESET}${BLUE}2 -> Health Data${RESET}")
        println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
        print(s"> ")
        option = readLine().trim()
        option match {
            case "1" => patientScreen()
            case "2" => selectPatientScreen()
            case "0" => {
                println("Exit APP")
                exit = true
            }
            case other => message = "Wrong option!"
            // end match
        }
        //end main screen
    }

    def selectPatientScreen(): Unit ={
        topScreen()
        println()
        println(s"               ${RESET}${GREEN_B}${BOLD}Select Patient${RESET}")
        println()
        var patient = new Patient()
        patient.selectDefaultPatient()
        if(defaultPatient != 0) healthDataScreen()

        //end selectPatientScreen
    }

    def healthDataScreen(): Unit ={
        var healthData = new HealthData()
        var localOption = ""
        var localExit = false
        topScreen()
        do {
            println()
            println(s"               ${RESET}${GREEN_B}${BOLD}Health Data Screen${RESET}")
            println()
            println(s"${RESET}${BOLD}Patient: ${defautlPatientMap(defaultPatient)._1} ${defautlPatientMap(defaultPatient)._2}${RESET}")
            println()
            println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
            println()
            println(s"${RESET}${BLUE}1 -> Insert new Health Data${RESET}")
            println(s"${RESET}${BLUE}2 -> List Health Data${RESET}")
            println(s"${RESET}${BLUE}3 -> Read JSON file${RESET}")
            println(s"${RESET}${BLUE}4 -> Delete Health Data${RESET}")
            println(s"${RESET}${BLUE}5 -> Return to Main Screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => healthData.create()
                case "2" => healthData.read()
                case "3" => println("Under Construction")
                case "4" => healthData.delete()
                case "5" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end patientScreen
    }

    def patientScreen(): Unit ={
        var patient = new Patient()
        var localOption = ""
        var localExit = false
        topScreen()
        do {
            println()
            println(s"               ${RESET}${GREEN_B}${BOLD}Patient Screen${RESET}")
            println()
            println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
            println()
            println(s"${RESET}${BLUE}1 -> Create Patient${RESET}")
            println(s"${RESET}${BLUE}2 -> List all Patients${RESET}")
            println(s"${RESET}${BLUE}3 -> Read JSON file${RESET}")
            println(s"${RESET}${BLUE}4 -> Delete Patient${RESET}")
            println(s"${RESET}${BLUE}5 -> Return to Main Screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => patient.create()
                case "2" => patient.read()
                case "3" => patient.readJSON()
                case "4" => patient.delete()
                case "5" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end patientScreen
    }

    def topScreen (): Unit = {
        print("\u001bc")
        println(s"${RESET}${RED}${BOLD}ALVICENNA - Personal Health Manager${RESET}")
        println()
        // end top screen
    }


    // calls JSONReader class to test with other files
    //var path = "/Users/jay85mag/Library/CloudStorage/OneDrive-Personal/00Revature/projects/PHM_CLI/documents/patient2.json"
//    var path = System.getProperty("user.dir")+"/JSONFiles/patient.json"
//    import java.nio.file.{Files, Paths}
//    if(Files.exists(Paths.get(path))){
//        var tag = Array("patients", "patient")
//        var elements = 5
//        var jasonreader = new JSONReader(path, tag, elements)
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
//    } else {
//        println("JSON file does not exists")
//    }


}
