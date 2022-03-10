package alvicenna

object jason {
  import java.util.Date
  import scala.Array.ofDim
  import scala.collection.mutable.ArrayBuffer
  import scala.io.Source.fromFile
  val filePath = "/Users/jay85mag/Library/CloudStorage/OneDrive-Personal/00Revature/projects/PHM_CLI/documents/patient.json"
  //val json_toString = fromFile(filePath).mkString
  val source = scala.io.Source.fromFile(filePath)
  val data = source.getLines().map(_.split("\t")).toArray
  source.close
  var obj = new ArrayBuffer[String]
  var flatt = data.flatten
  flatt.foreach(x => if(x.trim() != ""
    && x.trim().isEmpty != true
    && x.trim() != "{"
    && x.trim() != "\"patients\": ["
    && x.trim() != "\"patient\": {"
    && x.trim() != "}}" && x.trim() !="]"
    && x.trim() !="}"
    && x.trim() != "},")obj += x)

//  obj(1).subSequence(obj(1).indexOf("\": \"")+4,obj(1).length).asInstanceOf[String]
//  obj(2).replaceFirst("[,]$","")
//  obj(3).replaceFirst("[\"]$","")

  def getValueJSON(pstr: String):  String={
    var str = pstr
    str = str.subSequence(str.indexOf("\": \"")+4,str.length).asInstanceOf[String]
    str = str.replaceFirst("[,]$","")
    str = str.replaceFirst("[\"]$","")
    str = str.replaceFirst("[\"]$","")
    return str}

  var patientJSON = Array.ofDim[String](obj.length/4,4)
    var nItr = 0
    var index = 0
  while(nItr < obj.length/4){
    patientJSON(nItr)(0) = getValueJSON(obj(index))
    index += 1
    patientJSON(nItr)(1) = getValueJSON(obj(index))
    index += 1
    patientJSON(nItr)(2) = getValueJSON(obj(index))
    index += 1
    patientJSON(nItr)(3) = getValueJSON(obj(index))
    index += 1
    nItr += 1}

  //data conversion to use just if need
  def convertToDate(pDate: String, pformat: String): Date={
    val dateJSON = new java.text.SimpleDateFormat(pformat)
    val a = dateJSON.parse(pDate)
    return a}

}
