package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.NemoFileHandler
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.apache.commons.lang.RandomStringUtils


import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

//@CompileStatic
class ErpAnalysisService {

    def ontologyService
    def springSecurityService


    def handleNemoFile(ErpAnalysisResult erpAnalysisResult, CommonsMultipartFile commonsMultipartFile) {
        byte[] zipData = commonsMultipartFile.bytes;
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipData))
        ZipEntry zipEntry = zipInputStream.getNextEntry()

        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            println "handling zip file ${fileName}"

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            if (fileName.endsWith("rdf")) {
                println "start handling rdf ${fileName}"
                erpAnalysisResult.rdfContent = byteArrayOutputStream.toString()
                erpAnalysisResult.lastUploaded = new Date()
                erpAnalysisResult.inferredOntology = null
                erpAnalysisResult.setInProcess()
                erpAnalysisResult.save(flush: true, failOnError: true)
                ontologyService.inferRdfInstanceAsync(erpAnalysisResult, springSecurityService.currentUser.username)
                println "stop handling rdf ${fileName}"

            } else {
                println "start handling image ${fileName}"

                boolean isRaw = NemoFileHandler.isImageRaw(fileName)
                fileName = NemoFileHandler.convertImageName(fileName)

                PatternImage patternImage = PatternImage.findByPatternName(fileName)
                if (patternImage == null) {
                    println "image was null creating ${fileName}"
                    patternImage = new PatternImage()
                    patternImage.erpAnalysisResult = erpAnalysisResult
                }
                patternImage.patternName = fileName
                println "final name ${patternImage.patternName}"
                if(isRaw){
                    patternImage.rawImage = byteArrayOutputStream.toByteArray()
                }
                else{
                    patternImage.image = byteArrayOutputStream.toByteArray()
                }
                patternImage.save(flush: true, failOnError: true)
            }

            zipEntry = zipInputStream.getNextEntry()
        }

        zipInputStream.closeEntry()
        zipInputStream.close()

//        File.metaClass.unzip = { String dest ->
//            //in metaclass added methods, 'delegate' is the object on which
//            //the method is called. Here it's the file to unzip
//            def result = new ZipInputStream(new FileInputStream(delegate))
//            def destFile = new File(dest)
//            if(!destFile.exists()){
//                destFile.mkdir();
//            }
//            result.withStream{
//                def entry
//                while(entry = result.nextEntry){
//                    if (!entry.isDirectory()){
//                        new File(dest + File.separator + entry.name).parentFile?.mkdirs()
//                        def output = new FileOutputStream(dest + File.separator
//                                + entry.name)
//                        output.withStream{
//                            int len = 0;
//                            byte[] buffer = new byte[4096]
//                            while ((len = result.read(buffer)) > 0){
//                                output.write(buffer, 0, len);
//                            }
//                        }
//                    }
//                    else {
//                        new File(dest + File.separator + entry.name).mkdir()
//                    }
//                }
//            }
//        }
    }

    String generateFileName(Experiment experiment) {
        String charset = (('A'..'Z') + ('0'..'9')).join()
        Integer length = 7
        String randomString = RandomStringUtils.random(length, charset.toCharArray())
        return experiment?.identifier + randomString
    }
}
