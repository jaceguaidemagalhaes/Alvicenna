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
    var defaultPrescription = 0
    var defaultPrescriptionMap:Map[Int,(String, String, Boolean ,String,Int)] = null
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
        println(s"${RESET}${BLUE}1 -> Health Data${RESET}")
        println(s"${RESET}${BLUE}2 -> Prescriptions${RESET}")
        println(s"${RESET}${BLUE}3 -> Patients${RESET}")
        println(s"${RESET}${BLUE}4 -> Drugs${RESET}")
        println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
        print(s"> ")
        option = readLine().trim()
        option match {
            case "3" => patientScreen()
            case "1" => selectPatientScreen("healthDataScreen")
            case "4" => drugScreen()
            case "2" => selectPatientScreen("prescriptionScreen")
            case "0" => {
                println("Exit APP")
                exit = true
            }
            case other => message = "Wrong option!"
            // end match
        }
        //end main screen
    }

    def selectPrescriptionScreen(): Unit ={
        topScreen()
        println()
        println(s"               ${RESET}${GREEN_B}${BOLD}Select Prescription Screen ${RESET}")
        println()
        var prescription = new Prescription()
        prescription.selectDefaultPrescription()
        if(defaultPrescription != 0) prescriptionDrugScreen()

        //end selectDefaultPrescription
    }

    def prescriptionDrugScreen(): Unit ={
        var prescriptionDrug = new PrescriptionDrug()
        prescriptionDrug.read()
        var localOption = ""
        var localExit = false
        topScreen()
        do {
            println()
            println(s"               ${RESET}${GREEN_B}${BOLD}Health Data Screen${RESET}")
            println()
            println(s"${RESET}${BOLD}Doctor: ${defaultPrescriptionMap(defaultPrescription)._1} Date: ${defaultPrescriptionMap(defaultPrescription)._2}${RESET}")
            println()
            println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
            println()
            println(s"${RESET}${BLUE}1 -> Insert new Drug${RESET}")
            println(s"${RESET}${BLUE}2 -> List Drugs${RESET}")
            println(s"${RESET}${BLUE}3 -> Delete Drug${RESET}")
            println(s"${RESET}${BLUE}4 -> Return to Previous Screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => prescriptionDrug.create()
                case "2" => prescriptionDrug.read()
                case "3" => prescriptionDrug.delete()
                case "4" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end healthDateScreen
    }


    def prescriptionScreen(): Unit ={
        var prescription = new Prescription()
        var localOption = ""
        var localExit = false
        topScreen()
        do {

            println()
            println(s"               ${RESET}${GREEN_B}${BOLD}Prescription Screen${RESET}")
            println()
            println(s"${RESET}${BOLD}Patient: ${defautlPatientMap(defaultPatient)._1} ${defautlPatientMap(defaultPatient)._2}${RESET}")
            println()
            println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
            println()
            println(s"${RESET}${BLUE}1 -> Insert new Prescription${RESET}")
            println(s"${RESET}${BLUE}2 -> Manage Prescription${RESET}")
            println(s"${RESET}${BLUE}3 -> Read prescription from prescription.json file${RESET}")
            println(s"${RESET}${BLUE}4 -> Delete Prescription${RESET}")
            println(s"${RESET}${BLUE}5 -> Return to previous screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => prescription.create()
                case "2" => selectPrescriptionScreen()
                case "3" => prescription.readJSON()
                case "4" => prescription.delete()
                case "5" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end prescriptionScreen
    }

    def drugScreen(): Unit ={
        var drug = new Drug()
        var localOption = ""
        var localExit = false
        topScreen()
        do {

            println()
            println(s"               ${RESET}${GREEN_B}${BOLD}Mecine Screen${RESET}")
            println()
            println(s"${RESET}${BLUE}${BOLD}Select your option: (Type your option number)${RESET}")
            println()
            println(s"${RESET}${BLUE}1 -> Insert new Drug${RESET}")
            println(s"${RESET}${BLUE}2 -> List Drugs${RESET}")
            //println(s"${RESET}${BLUE}5 -> Read JSON file${RESET}")
            println(s"${RESET}${BLUE}3 -> Delete Drug${RESET}")
            println(s"${RESET}${BLUE}4 -> Return to previous screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => drug.create()
                case "2" => drug.read()
                //case "3" => println("Under Construction")
                case "3" => drug.delete()
                case "4" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end drugScreen
    }


    def selectPatientScreen(p_nextScreen: String): Unit ={
        topScreen()
        println()
        println(s"               ${RESET}${GREEN_B}${BOLD}Select Patient${RESET}")
        println()
        var patient = new Patient()
        patient.selectDefaultPatient()
        if(defaultPatient != 0 && p_nextScreen == "healthDataScreen") healthDataScreen()
        if(defaultPatient != 0 && p_nextScreen == "prescriptionScreen") prescriptionScreen()

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
            println(s"${RESET}${BLUE}3 -> Read Health Data from healthdata.json file${RESET}")
            println(s"${RESET}${BLUE}4 -> Delete Health Data${RESET}")
            println(s"${RESET}${BLUE}5 -> Return to previous screen${RESET}")
            println(s"${RESET}${BLUE}0 -> Exit APP${RESET}")
            print(s"> ")
            localOption = readLine().trim()
            localOption match {
                case "1" => healthData.create()
                case "2" => healthData.read()
                case "3" => healthData.readJSON()
                case "4" => healthData.delete()
                case "5" => localExit = true
                case "0" => { localExit = true
                    exit = true}
                case other => println(s"${RESET}${RED}Wrong option!${RESET}")
            }
            // end match
        }
        while (!localExit)
        //end healthDateScreen
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
            println(s"${RESET}${BLUE}3 -> Read patient from file${RESET}")
            println(s"${RESET}${BLUE}4 -> Delete Patient${RESET}")
            println(s"${RESET}${BLUE}5 -> Return to previous screen${RESET}")
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
}
