package alvicenna

import scala.sys.process._

object Main extends App {
    print("\u001b[2J")
//    var helloword = new HelloWord()
//    helloword

    var patient = new Patient()
    patient.readAll()

//    call create patient
//    patient.create()
}
