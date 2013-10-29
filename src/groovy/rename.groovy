/**
 */
File file = new File(args[0])
println file.absolutePath
file.eachFile { it ->
    println "file-name ${it.name}"
    String oldName = it.name
    if(oldName.startsWith("GF")){
        String newName = "GAF"+oldName.substring(2)
        it.renameTo(file.absolutePath+"/"+newName)
    }
}
