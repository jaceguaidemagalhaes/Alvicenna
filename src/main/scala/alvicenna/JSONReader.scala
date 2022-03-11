package alvicenna

class JSONReader(p_filePath: String, p_tagToRemove: Array[String], p_numberOfObjects: Int ) {
  //p_filePath = file to read
  //p_tagToRemove = tags to remove: 0 = groups, 1 = class
  //p_numberOfObjects = number of objects in the json class

  import scala.Array.ofDim
  import scala.collection.mutable.ArrayBuffer
  import scala.io.Source.fromFile



  //variable section
  var obj = new ArrayBuffer[String]

  //define the path for the file and reads the file
  val filePath = p_filePath
  //val filePath = "/Users/jay85mag/Library/CloudStorage/OneDrive-Personal/00Revature/projects/PHM_CLI/documents/patient.json"
  val source = scala.io.Source.fromFile(filePath)
  val data = source.getLines().map(_.split("\t")).toArray
  source.close
  var flatt = data.flatten

  // remove the unwanted characters


  flatt.foreach(x => if(x.trim() != ""
    && x.trim().isEmpty != true
    && x.trim() != "{"
    && x.trim() != "\""+p_tagToRemove(0)+"\": ["
    && x.trim() != "\""+p_tagToRemove(1)+"\": {"
    && x.trim() != "}}" && x.trim() !="]"
    && x.trim() !="}"
    && x.trim() != "},"
    && x.trim() != ":"
    && x.trim() != "]"
    && x.trim() != "[")obj += x)


  // create the array with the results
  var JSONData = Array.ofDim[String](obj.length/p_numberOfObjects,p_numberOfObjects)
  var nItr = 0
  var index = 0
  while(nItr < obj.length/p_numberOfObjects){
    for(i <- 0 to p_numberOfObjects-1){
      JSONData(nItr)(i) = getValueJSON(obj(index))
      index += 1
    }
    nItr += 1
  //end while
  }

  //identify de values after ": " and remove final , " " in the string
  def getValueJSON(p_str: String):  String={
    var str = p_str
    str = str.subSequence(str.indexOf("\": \"")+4,str.length).asInstanceOf[String]
    str = str.replaceFirst("[,]$","")
    str = str.replaceFirst("[\"]$","")
    str = str.replaceFirst("[\"]$","")
    return str
  //end getValueJSON
  }
//class JSONReader
}
