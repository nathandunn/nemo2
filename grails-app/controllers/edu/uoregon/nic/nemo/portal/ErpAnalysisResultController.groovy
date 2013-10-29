package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.NemoFileHandler
import edu.uoregon.nemo.nic.portal.util.TermLinkContainer
import edu.uoregon.nic.nemo.portal.client.BrainLocationEnum
import grails.plugins.springsecurity.Secured
import groovy.xml.MarkupBuilder
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.uoregon.nemo.nic.QueryListEnum

class ErpAnalysisResultController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def ontologyService

    def userService
    def erpAnalysisService
    def springSecurityService
    def grailsApplication
    def searchService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.erpAnalysisResult?.experiment
        model.experimentHeader = model.experimentHeader ?: model.erpAnalysisResultInstance?.experiment
    }

    private def getDownloadsPath() {
//        ServletContext servletContext = ServletContextHolder.servletContext
//        return servletContext.getRealPath("/WEB-INF/files/downloads/")
        return grailsApplication.config.grails.nemo.data + "/downloads/"
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def reinfer(Integer id) {
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.get(id)
        if (!erpAnalysisResult) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
        }

        flash.message = "Began re-inferring datafile"
        inferErpAnalysisResult(erpAnalysisResult)
        redirect(action: "show", id: id)
    }

    def reinferAll() {
        ontologyService.reInferAllOntologiesAsync(springSecurityService.currentUser.username)
        flash.message = "Re-inferring ALL RDF files"
        redirect(action: "index", controller: "admin")
    }

//    def doSearch() {
//        def min = (params.min ?: 0) as Integer
//        def max = (params.max ?: 1000) as Integer
//        def locations = params.locations
//
//        List<BrainLocationEnum> locationList = new ArrayList<>()
//        for(location in locations?.split(",") ){
//            locationList.add(location as BrainLocationEnum)
//        }
//
////        String jsonText = searchService.searchErpsRaw(min, max, null)
//        String jsonText = searchService.searchErps(min, max, locationList)
//
//        SearchResultsDTO searchResultsDTO = new SearchResultsDTO(new JsonSlurper().parseText(jsonText))
//
//
//
////        def model = [results: searchResultsDTO]
//
//        render(view: "searchResult", model: [results:searchResultsDTO.sortedSearchResults])
//    }

    def cacheSearch() {
        searchService.cacheSearch()
        redirect(action: "search")
    }

    def search() {

    }

    def reference() {

    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)

        if (params.related && !params.related.contains("_")) {
            log.error "Related term exists, but does not contain an underscore [${params.related}]"
            params.related = null
        }

        def model
        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass?.indexOf("AnalysisVariable")>=0) {
                AnalysisVariable analysisVariable = AnalysisVariable.findById(relatedID)
                model = [erpAnalysisResultInstanceList: ErpAnalysisResult.findAllByDependentVariableOrIndependentVariable(analysisVariable, analysisVariable, params)
                        , erpAnalysisResultInstanceTotal: ErpAnalysisResult.countByDependentVariableOrIndependentVariable(analysisVariable, analysisVariable)
                        , related: analysisVariable]
            }
            else
            if (relatedClass?.indexOf("AnalysisMethod")>=0) {
                AnalysisMethod analysisMethod = AnalysisMethod.findById(relatedID)
                model = [erpAnalysisResultInstanceList: ErpAnalysisResult.findAllByAnalysisMethod(analysisMethod, params)
                        , erpAnalysisResultInstanceTotal: ErpAnalysisResult.countByAnalysisMethod(analysisMethod)
                        , related: analysisMethod]
            }
            else {
                log.error "error handling related class: " + relatedClass + " for " + params.related
				model = getRelatedErpAnalysisResults(id)
            }
        } else {
            model = getRelatedErpAnalysisResults(id)
        }

        if (model.erpAnalysisResultInstanceList.size() == 1) {
            redirect(action: 'show', id: model.erpAnalysisResultInstanceList[0].id)
        } else {
            render(view: "list", model: model)
        }
        return model
    }

    private def getRelatedErpAnalysisResults(id) {
//        def id = params.id
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            return [erpAnalysisResultInstanceList: ErpAnalysisResult.findAllByExperiment(experiment, params), erpAnalysisResultInstanceTotal: ErpAnalysisResult.countByExperiment(experiment), experimentHeader: experiment]
        } else {
            return [erpAnalysisResultInstanceList: ErpAnalysisResult.list(params), erpAnalysisResultInstanceTotal: ErpAnalysisResult.count()]
        }
    }

    def erpPatterns(Integer id) {
        def returnMap = list(id)
        List<ErpAnalysisResult> erpAnalysisResults = []

        returnMap.erpAnalysisResultInstanceList.each { it ->
            if (it.isRdfAvailable()) {
                erpAnalysisResults.add(it)
            }
        }
        // if single mode . . .a redirect would have already been issued . . .

        render view: "list", model: [erpAnalysisResultInstanceList: erpAnalysisResults, erpAnalysisResultInstanceTotal: erpAnalysisResults.size(), experimentHeader: returnMap.experimentHeader]
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def experiment = Experiment.get(id)
        [erpAnalysisResultInstance: new ErpAnalysisResult(params), erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(experiment), experimentInstance: experiment]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def erpAnalysisResultInstance = new ErpAnalysisResult(params)
        erpAnalysisResultInstance.artifactFileName = erpAnalysisService.generateFileName(erpAnalysisResultInstance.experiment)

        if (erpAnalysisResultInstance.dependentVariable != null && erpAnalysisResultInstance.dependentVariable == erpAnalysisResultInstance.independentVariable) {
            erpAnalysisResultInstance.errors.rejectValue("dependentVariable", "", "Conditions must be different")
            erpAnalysisResultInstance.errors.rejectValue("independentVariable", "", "Conditions must be different")
            render(view: "create", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpAnalysisResultInstance.experiment), experimentInstance: erpAnalysisResultInstance.experiment])
            return
        }

        if (!erpAnalysisResultInstance.save(flush: true)) {
            erpAnalysisResultInstance.id = null
            render(view: "create", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpAnalysisResultInstance.experiment), experimentInstance: erpAnalysisResultInstance.experiment])
            return
        }

        searchService.cacheSearchAsync()

//        flash.message = message(code: 'default.created.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), erpAnalysisResultInstance.id])
        flash.message = 'Created datafile: ' + erpAnalysisResultInstance.artifactFileName
        redirect(action: "show", id: erpAnalysisResultInstance.id)
    }


    @Secured(['ROLE_VERIFIED'])
    private void handleErpAnalysisResultUpload(CommonsMultipartFile inputFile, ErpAnalysisResult erpAnalysisResultInstance) {

        if (erpAnalysisResultInstance.isRdf()) {
            handleRdfUpload(erpAnalysisResultInstance, inputFile)
        } else {
            handleBinaryUpload(erpAnalysisResultInstance, inputFile)
        }

    }

    private void handleBinaryUpload(ErpAnalysisResult erpAnalysisResult, CommonsMultipartFile inputFile) {

        erpAnalysisResult.setInProcess()
        File file = new File(getDownloadsPath() + erpAnalysisResult.artifactFileName)
        if (isTextFile(erpAnalysisResult)) {
            file.text = inputFile.inputStream.text
//            inputFile.transferTo(file)
        } else {
            inputFile.transferTo(file)
        }
        erpAnalysisResult.download = null

        erpAnalysisResult.download = file.getAbsolutePath()

        erpAnalysisResult.setDoneProcessing()
        erpAnalysisResult.save(flush: true, validate: true)
    }

    private void handleRdfUpload(ErpAnalysisResult erpAnalysisResultInstance, CommonsMultipartFile inputFile) {
        erpAnalysisResultInstance.rdfContent = inputFile.inputStream.text
        inferErpAnalysisResult(erpAnalysisResultInstance)
    }

    private void inferErpAnalysisResult(ErpAnalysisResult erpAnalysisResultInstance) {
        erpAnalysisResultInstance.lastUploaded = new Date()
        erpAnalysisResultInstance.inferredOntology = null

        erpAnalysisResultInstance.setInProcess()
        erpAnalysisResultInstance.save(flush: true)
        ontologyService.inferRdfInstanceAsync(erpAnalysisResultInstance, springSecurityService.currentUser.username)
    }


    def show(Long id) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        render view: "show", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, experimentHeader: erpAnalysisResultInstance.experiment]
    }

    def showIndividuals(Long id, Integer time) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }
        List<Integer> times = (List<Integer>) Individual.executeQuery("select i.peakTime from Individual i where i.erpAnalysisResult = :erpAnalysisResult  group by i.peakTime order by i.peakTime asc"
                , ["erpAnalysisResult": erpAnalysisResultInstance])

        Integer selectedTime = times.indexOf(time)

        render view: "showIndividuals", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, times: times, currentTime: time, selectedTime: selectedTime, experimentHeader: erpAnalysisResultInstance.experiment]
    }

    def showIndividualsAtLocation(Long id, String locationName) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        if(locationName==null){
           locationName = BrainLocationEnum.MFRONT
        }
//        List<Integer> times = (List<Integer>) Individual.executeQuery("select i.peakTime from Individual i where i.erpAnalysisResult = :erpAnalysisResult  group by i.peakTime order by i.peakTime asc"
//        ,["erpAnalysisResult":erpAnalysisResultInstance])

        List<BrainLocationEnum> brainLocationEnumList = BrainLocationEnum.values() as List

        BrainLocationEnum location = BrainLocationEnum.getEnumForName(locationName)
        Integer selectedLocation = brainLocationEnumList.indexOf(location)

        render view: "showIndividualsAtLocation", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, locations: brainLocationEnumList, currentLocation: location, selectedLocation: selectedLocation]
    }

    def showIndividualTable(Long id, Integer time) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }
        List<Integer> times = (List<Integer>) Individual.executeQuery("select i.peakTime from Individual i where i.erpAnalysisResult = :erpAnalysisResult  group by i.peakTime order by i.peakTime asc"
                , ["erpAnalysisResult": erpAnalysisResultInstance])
        List<Individual> individualList = Individual.findAllByErpAnalysisResultAndPeakTime(erpAnalysisResultInstance, time)
        Map<BrainLocationEnum, Individual> individualMap = new HashMap<>()

        for (Individual individual in individualList) {
            individualMap.put(individual.location, individual)
        }

//        http://purl.bioontology.org/NEMO/data/GAF-EEGcloze_Unexpected-Expected_ERP_+196_mean_intensity_MFRONT
        String link = individualList.get(0).url
        Integer lastIndexSlash = link.lastIndexOf("/")
        Integer lastIndexIntensity = link.lastIndexOf("_mean_intensity")
        link = link.substring(lastIndexSlash + 1, lastIndexIntensity)

        render view: "showIndividualTable", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, individuals: individualMap, times: times, currentTime: time, experimentHeader: erpAnalysisResultInstance.experiment, link: link]
    }

    def showIndividualLocationTable(Long id, String locationName) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        BrainLocationEnum location = BrainLocationEnum.getEnumForName(locationName)
        List<BrainLocationEnum> brainLocationEnumList = BrainLocationEnum.values() as List
        Integer selectedLocation = brainLocationEnumList.indexOf(location)

        List<Individual> individualList = Individual.findAllByErpAnalysisResultAndLocation(erpAnalysisResultInstance, location)

        log.warn "Failed to find individuals for location ${locationName}"
        if(!individualList){
//            render "<h4>No data found for ${locationName}</h4>"
            render view:"noDataFound"
            return
        }

        List<Integer> times = (List<Integer>) Individual.executeQuery("select i.peakTime from Individual i where i.erpAnalysisResult = :erpAnalysisResult  group by i.peakTime order by i.peakTime asc"
                , ["erpAnalysisResult": erpAnalysisResultInstance])

        Map<Integer, Individual> individualMap = new HashMap<>()

        for (Individual individual in individualList) {
            individualMap.put(individual.peakTime, individual)
        }

//        http://purl.bioontology.org/NEMO/data/GAF-EEGcloze_Unexpected-Expected_ERP_+196_mean_intensity_MFRONT
        String link = ""
        if (individualList.size() > 0) {
            link = individualList.get(0).url
            Integer lastIndexSlash = link.lastIndexOf("/")
            Integer lastIndexIntensity = link.lastIndexOf("_mean_intensity")
            link = link.substring(lastIndexSlash + 1, lastIndexIntensity)

        }

        render view: "showIndividualLocationTable", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, individuals: individualMap, location: location, selectedLocation: selectedLocation, times: times, experimentHeader: erpAnalysisResultInstance.experiment, link: link]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        log.debug "instance ${erpAnalysisResultInstance}"
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(erpAnalysisResultInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        log.debug "editable ${editable}"
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }
        println "size: ${ErpPatternExtraction.findAllByExperiment(erpAnalysisResultInstance.experiment)}"

        render view: "edit", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpPatternExtractions: ErpPatternExtraction.findAllByExperiment(erpAnalysisResultInstance.experiment)]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (erpAnalysisResultInstance.version > version) {
                erpAnalysisResultInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'erpAnalysisResult.label', default: 'File')] as Object[],
                        "Another user has updated this ErpAnalysisResult while you were editing")
                render(view: "edit", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpAnalysisResultInstance.experiment)])

                return
            }
        }

        Boolean editable = userService.isAdminOrCurrent(erpAnalysisResultInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        if (params.erpDataPreprocessing == "") {
            erpAnalysisResultInstance.erpDataPreprocessing = null
        }

        erpAnalysisResultInstance.properties = params

        if (erpAnalysisResultInstance.dependentVariable != null && erpAnalysisResultInstance.dependentVariable == erpAnalysisResultInstance.independentVariable) {
            erpAnalysisResultInstance.errors.rejectValue("dependentVariable", "", "Conditions must be different")
            erpAnalysisResultInstance.errors.rejectValue("independentVariable", "", "Conditions must be different")
            render(view: "edit", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpAnalysisResultInstance.experiment)])
            return
        }


        if (!erpAnalysisResultInstance.save(flush: true)) {
            render(view: "edit", model: [erpAnalysisResultInstance: erpAnalysisResultInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpAnalysisResultInstance.experiment)])
            return
        }

        searchService.cacheSearchAsync()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), erpAnalysisResultInstance.artifactFileName])
        redirect(action: "show", id: erpAnalysisResultInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)
        if (!erpAnalysisResultInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(erpAnalysisResultInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        try {
            erpAnalysisResultInstance.set = null
            erpAnalysisResultInstance.format = null
            erpAnalysisResultInstance.erpPatternExpression = null
//            erpAnalysisResultInstance.save(insert: false,flush: true,failOnError: true)
            erpAnalysisResultInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), erpAnalysisResultInstance.artifactFileName])
//            redirect(action: "list")
            redirect(action: "list", controller: "experiment", id: erpAnalysisResultInstance.experiment.id)
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'erpAnalysisResult.label', default: 'File'), erpAnalysisResultInstance.artifactFileName])
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def upload(Integer id) {
        def erpAnalysisResultInstance = ErpAnalysisResult.get(id)

        Boolean editable = userService.isAdminOrCurrent(erpAnalysisResultInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        if (erpAnalysisResultInstance.isRdfInProcess()) {
            redirect(action: "show", id: id)
            return
        }
        CommonsMultipartFile uploadedFile = request.getFile('newRdf')

        if (uploadedFile.empty) {
            flash.message = 'file cannot be empty'
            redirect(action: "show", id: id)
//            render(view: 'uploadForm')
            return
        }

        if (uploadedFile.originalFilename.endsWith(NemoFileHandler.NEMO_SUFFIX_TYPE)) {
            // unzip file
            // get all images and upload
            erpAnalysisService.handleNemoFile(erpAnalysisResultInstance, uploadedFile)

            // finally upload the RDF (async)

        } else {

            log.debug "datafile format: " + erpAnalysisResultInstance.format
            def message = validateFileName(uploadedFile.originalFilename, erpAnalysisResultInstance.format)
            if (message) {
                flash.message = 'File must end with extension ' + message
                redirect(id: erpAnalysisResultInstance.id, controller: "erpAnalysisResult", action: "edit", model: [erpAnalysisResultInstance: erpAnalysisResultInstance])
                return
            }
//            erpAnalysisResultInstance.artifactFileName = erpAnalysisResultInstance.artifactFileName + "." + getSuffix(erpAnalysisResultInstance.format)
            erpAnalysisResultInstance.artifactFileName = uploadedFile.originalFilename
            erpAnalysisResultInstance.lastUploaded = new Date()

            log.debug 'artifact FileName: ' + erpAnalysisResultInstance.artifactFileName

//            handleErpAnalysisResultUpload(uploadedFile, erpAnalysisResultInstance)
            handleRdfUpload(erpAnalysisResultInstance, uploadedFile)

        }

        redirect(action: "show", id: id)
    }

    def validateFileName(String fileName, DataFormat dataFormat) {
        String labelType
        try {

            // all suffixes are 3 characters?
            def suffix = fileName.substring(fileName.lastIndexOf(".") + 1)
            labelType = getSuffix(dataFormat)

            log.debug "suffix ${suffix} vs labelType ${labelType}"

            if (suffix == labelType) {
                return null
            } else {
                return labelType
            }
        } catch (Exception e) {
            log.debug 'problem processing' + e
            log.error(e)
            return labelType + '.  Problem processing filename: ' + fileName + ' and dataFormat: ' + dataFormat.name
        }
    }


    private String getSuffix(DataFormat dataFormat) {
        def label = dataFormat?.name?.replaceAll("_", " ")
        def labelType = label.substring(0, 3)
        return labelType.split(" ")[0]
    }

    def download(Integer id) {
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.get(id)
        if (!erpAnalysisResult) {
            response.status = 404
            return
        }
        if (erpAnalysisResult.rdfContent) {
            response.setHeader("Content-Disposition", "attachment; filename=" + erpAnalysisResult.artifactFileName)
            render(text: erpAnalysisResult.rdfContent, contentType: "application/download", encoding: "UTF-8")
        }
    }

    private boolean isTextFile(ErpAnalysisResult erpAnalysisResult) {
        switch (erpAnalysisResult.format.name) {
            case 'm data format':
//                case 'mat data format':
            case 'owl data format':
            case 'xml data format':
            case 'rtf data format':
            case 'txt data format':
                return true
            default:
                return false
        }
    }


    def classify(Integer id) {
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.get(id)
        if (!erpAnalysisResult) {
            response.status = 404
            return
        }
        if (!erpAnalysisResult.isRdfAvailable()) {
            def writer = new StringWriter()
            MarkupBuilder builder = new MarkupBuilder(writer)
            builder.div(style: 'color:#888;margin:10px;text-align:center;width:100%;') {
                mkp.yieldUnescaped 'Currently inferring and not available'
            }
            render(text: writer.toString(), contentType: "text/html")
            return
        }
        Boolean edit = Boolean.valueOf(params.edit)
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager()
//            log.debug "is RDF!!: "  + erpAnalysisResult.artifactFileName
        // move to service
        if (erpAnalysisResult.rdfContent && !erpAnalysisResult.inferredOntology) {
            log.debug "inferring instance : " + erpAnalysisResult.artifactFileName
            ontologyService.inferRdfInstance(erpAnalysisResult, springSecurityService.currentUser.username)
        } else if (!erpAnalysisResult.rdfContent) {
            return
        }

        OWLReasoner reasoner = ontologyService.getReasonerForErpAnalysisResult(erpAnalysisResult)
        OWLOntology localOntology = ontologyService.getOntologyFromErpAnalysisResult(erpAnalysisResult)

        OWLClass parent = owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.SCALP_RECORDED_ERP_DIFFWAVE_COMPONENT.url))

        Set<OWLNamedIndividual> individuals = reasoner.getInstances(parent, false).flattened
        Set<OWLNamedIndividual> subclasses = reasoner.getSubClasses(parent, false).flattened

        // let's create a map of all the instances with a list of subclasses
        Map<String, TreeSet<TermLinkContainer>> instanceMap = new HashMap<String, TreeSet<TermLinkContainer>>()

        for (OWLNamedIndividual instance in individuals) {
            Collection<OWLClass> types = instance.getTypes(localOntology).flatten()
            types = types.intersect(subclasses)
            Set<TermLinkContainer> classTypes = new TreeSet<TermLinkContainer>()
            for (OWLClass type in types) {
                TermLinkContainer container = new TermLinkContainer()
                container.url = type.toStringID()
                container.label = ontologyService.getLabelForUrl(type.toStringID())
                classTypes.add(container)
                OWLClass owlUrl = owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(instance.decodeURL()))
                for (OWLClassExpression subclassExpression in owlUrl.getSubClasses(localOntology)) {
                    container.label = ontologyService.getLabelForUrl(subclassExpression.toStringID())
                }
            }
            instanceMap.put(instance.toStringID(), classTypes)
        }

        List<String> sortedInstances = new ArrayList<>(instanceMap.keySet())
//        TC-RGLD2_+LN0-+LW0_ERP_+1016
//        TC-RGLD2_+LN0-+LW0_ERP_+1072
//        TC-RGLD2_+LN0-+LW0_ERP_+120
//        TC-RGLD2_+LN0-+LW0_ERP_+1544
//        TC-RGLD2_+LN0-+LW0_ERP_+160
//        TC-RGLD2_+LN0-+LW0_ERP_+224
//        TC-RGLD2_+LN0-+LW0_ERP_+296
//        TC-RGLD2_+LN0-+LW0_ERP_+356
        sortedInstances.sort(true) { a, b ->
            String first = a
            String second = b
//            println "first [${first}] vs [${second}]"
            if (a.contains("_+")) {
                first = a.substring(a.lastIndexOf("_+") + 2)
            } else if (a.contains("#NEMO_")) {
                first = a.substring(a.lastIndexOf("#NEMO_") + 6)
            }

            if (b.contains("_+")) {
                second = b.substring(b.lastIndexOf("_+") + 2)
            } else if (b.contains("#NEMO_")) {
                second = b.substring(b.lastIndexOf("#NEMO_") + 6)
            }

            try {
                return first.toFloat() - second.toFloat()
            } catch (e) {
                println e
                return first.compareTo(second)
            }

        }


        Map<String, Set<TermLinkContainer>> mappedInstances = new LinkedHashMap<String, TreeSet<TermLinkContainer>>()

        for (instanceKey in sortedInstances) {
            def key = ontologyService.getLabelForUrl(instanceKey)?.replaceAll("_", " ")
            if (!key) {
                if (instanceKey.startsWith(OntologyService.DATA_URL)) {
                    key = instanceKey.substring(OntologyService.DATA_URL.length())
                } else {
                    key = instanceKey
                }
            }
            if (!key.startsWith("TESTEXPT")) {
                Set<TermLinkContainer> values = instanceMap.get(instanceKey)
//                for (TermLinkContainer instanceValue in instanceMap.get(instanceKey)) {
////                    def value = ontologyService.getLabelForUrl(instanceValue.url)?.replaceAll("_", " ")
////                    def value = instanceValue.url
//                    values.add(instanceValue.url)
////                    values.add(value ?: "empty:" + instanceValue.url)
//                }
                mappedInstances.put(key, values)
            }
        }

        return render(view: "subclass-list", model: [instances: mappedInstances, erpAnalysisResult: erpAnalysisResult, edit: edit, experimentHeader: erpAnalysisResult.experiment])
    }

}
