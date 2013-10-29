package edu.uoregon.nic.nemo.portal



class UpdateOntologiesJob {

    def ontologyService

    static triggers = {
//        simple repeatInterval: 5000l // execute job once in 5 seconds
        cron name: 'myTrigger', cronExpression: "0 0 2 ? * SAT"
//        cron name: 'myTrigger', cronExpression: "0 45 14 ? * *"
        // once a week
//        simple repeatInterval: 7*24*60*60*1000l // execute job once a week
    }

    def group = "Ontology"

//    String remoteURL = "http://nemo.nic.uoregon.edu/ontologies/"

    def execute() {

        ontologyService.updateOntologies()

//        File file = new File(ontologyService.getLocalNemoOntologyFile())
//        String oldText = file.exists() ? file.text : ""
//        String newText = ontologyService.NS.toURL()?.text
//
//        log.info "time to update: ${!oldText.equals(newText)} - ${oldText.size()} vs ${newText.size()}?"
//        if (!oldText.equals(newText)) {
//            log.error "ontologies have been updated . . . "
//            // download the separate files
//            downloadFile("NEMO.owl")
//            downloadFile("NEMO_annotation_properties.owl")
//            downloadFile("NEMO_deprecated.owl")
//
//            ontologyService.reloadGlobalOntology()
//            ontologyService.reInferAllOntologiesAsync()
//        } else {
//            log.error "ontologies are equivalent . . not updating!!"
//        }
    }

//    def downloadFile(String fileName) {
//        log.info "downloading " + fileName
//        File localFile = new File(ontologyService.getLocalOntologiesDirectory() + fileName)
//        if (localFile.delete()) {
//            localFile << (remoteURL + fileName).toURL().text
//            log.info "finished downloading " + fileName
//        }
//    }
}
