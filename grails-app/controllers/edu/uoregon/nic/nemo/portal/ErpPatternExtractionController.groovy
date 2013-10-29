package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.commons.CommonsMultipartFile

class ErpPatternExtractionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def ontologyService

    def userService
    def erpAnalysisService
    def springSecurityService
    def grailsApplication

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.erpPatternExtraction?.experiment
        model.experimentHeader = model.experimentHeader ?: model.erpPatternExtractionInstance?.experiment
    }

    private def getDownloadsPath() {
//        ServletContext servletContext = ServletContextHolder.servletContext
//        return servletContext.getRealPath("/WEB-INF/files/downloads/")
        return grailsApplication.config.grails.nemo.data + "/downloads/"
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def reinferAll() {
        ontologyService.reInferAllOntologiesAsync(springSecurityService.currentUser.username)
        flash.message = "Re-inferring ALL RDF files"
        redirect(action: "index", controller: "admin")
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)

//        if (params.related && !params.related.contains("_")) {
//            log.error "Related term exists, but does not contain an underscore [${params.related}]"
//            params.related = null
//        }

        def model
        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("PatternExtractionMethod")) {
                PatternExtractionMethod patternExtractionMethod = PatternExtractionMethod.findById(relatedID)
                model = [erpPatternExtractionInstanceList: ErpPatternExtraction.findAllByMethod(patternExtractionMethod, params)
                        , erpPatternExtractionInstanceTotal: ErpPatternExtraction.countByMethod(patternExtractionMethod)
                        , related: patternExtractionMethod]
            } else if (relatedClass.contains("PatternExtractionCondition")) {
                PatternExtractionCondition patternExtractionCondition = PatternExtractionCondition.findById(relatedID)
                model = [erpPatternExtractionInstanceList: ErpPatternExtraction.findAllByConditionOfInterestOrBaselineCondition(patternExtractionCondition,patternExtractionCondition, params)
                        , erpPatternExtractionInstanceTotal: ErpPatternExtraction.countByConditionOfInterestOrBaselineCondition(patternExtractionCondition,patternExtractionCondition)
                        , related: patternExtractionCondition]
            } else {
                log.error "error handling related class: " + relatedClass + " for " + params.related
                model = getRelatedErpPatternExtractions(id)
            }
        } else {
        model = getRelatedErpPatternExtractions(id)
        }

        if (model.erpPatternExtractionInstanceList.size() == 1) {
            redirect(action: 'show', id: model.erpPatternExtractionInstanceList[0].id)
        } else {
            render(view: "list", model: model)
        }
        return model
    }

    private def getRelatedErpPatternExtractions(id) {
//        def id = params.id
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            [erpPatternExtractionInstanceList: ErpPatternExtraction.findAllByExperiment(experiment, params), erpPatternExtractionInstanceTotal: ErpPatternExtraction.countByExperiment(experiment), experimentHeader: experiment]
        } else {
            [erpPatternExtractionInstanceList: ErpPatternExtraction.list(params), erpPatternExtractionInstanceTotal: ErpPatternExtraction.count()]
        }
    }

    def erpPatterns(Integer id) {
        def returnMap = list(id)
        List<ErpPatternExtraction> erpPatternExtractions = []

        returnMap.erpPatternExtractionInstanceList.each { it ->
            if (it.isRdfAvailable()) {
                erpPatternExtractions.add(it)
            }
        }
        // if single mode . . .a redirect would have already been issued . . .

        render view: "list", model: [erpPatternExtractionInstanceList: erpPatternExtractions, erpPatternExtractionInstanceTotal: erpPatternExtractions.size(), experimentHeader: returnMap.experimentHeader]
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def experiment = Experiment.get(id)
        [erpPatternExtractionInstance: new ErpPatternExtraction(params), erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(experiment), experimentInstance: experiment]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def erpPatternExtractionInstance = new ErpPatternExtraction(params)
        erpPatternExtractionInstance.artifactFileName = erpAnalysisService.generateFileName(erpPatternExtractionInstance.experiment)


        if (erpPatternExtractionInstance.baselineCondition != null && erpPatternExtractionInstance.baselineCondition == erpPatternExtractionInstance.conditionOfInterest) {
            erpPatternExtractionInstance.errors.rejectValue("baselineCondition", "", "Baseline Condition and Condition of Interest must be different")
            erpPatternExtractionInstance.errors.rejectValue("conditionOfInterest", "", "Condition of Interest and Baseline Condition must be different")
            render(view: "create", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment),experimentInstance: erpPatternExtractionInstance.experiment])
            return
        }

        if (!erpPatternExtractionInstance.save(flush: true)) {
            erpPatternExtractionInstance.id = null
            render(view: "create", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment),experimentInstance: erpPatternExtractionInstance.experiment])
            return
        }

//        flash.message = message(code: 'default.created.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), erpPatternExtractionInstance.id])
        flash.message = 'Created datafile: ' + erpPatternExtractionInstance.artifactFileName
        redirect(action: "show", id: erpPatternExtractionInstance.id)
    }

    private void handleBinaryUpload(ErpPatternExtraction erpPatternExtraction, CommonsMultipartFile inputFile) {

        File file = new File(getDownloadsPath() + erpPatternExtraction.artifactFileName)
        if (isTextFile(erpPatternExtraction)) {
            file.text = inputFile.inputStream.text
//            inputFile.transferTo(file)
        } else {
            inputFile.transferTo(file)
        }
        erpPatternExtraction.download = null
        erpPatternExtraction.download = file.getAbsolutePath()

        erpPatternExtraction.save(flush: true, validate: true)
    }


    def show(Long id) {
        def erpPatternExtractionInstance = ErpPatternExtraction.get(id)
        if (!erpPatternExtractionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        render view: "show", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, experimentHeader: erpPatternExtractionInstance.experiment]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def erpPatternExtractionInstance = ErpPatternExtraction.get(id)
        log.debug "instance ${erpPatternExtractionInstance}"
        if (!erpPatternExtractionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(erpPatternExtractionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        log.debug "editable ${editable}"
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        render view: "edit", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment)]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def erpPatternExtractionInstance = ErpPatternExtraction.get(id)
        if (!erpPatternExtractionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (erpPatternExtractionInstance.version > version) {
                erpPatternExtractionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'erpPatternExtraction.label', default: 'File')] as Object[],
                        "Another user has updated this ErpPatternExtraction while you were editing")
                render(view: "edit", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment)])

                return
            }
        }

        Boolean editable = userService.isAdminOrCurrent(erpPatternExtractionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        if (params.erpDataPreprocessing == "") {
            erpPatternExtractionInstance.erpDataPreprocessing = null
        }

        erpPatternExtractionInstance.properties = params

        if (erpPatternExtractionInstance.baselineCondition != null && erpPatternExtractionInstance.baselineCondition == erpPatternExtractionInstance.conditionOfInterest) {
            erpPatternExtractionInstance.errors.rejectValue("baselineCondition", "", "Baseline Condition and Condition of Interest must be different")
            erpPatternExtractionInstance.errors.rejectValue("conditionOfInterest", "", "Condition of Interest and Baseline Condition must be different")
            render(view: "edit", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment)])
            return
        }


        if (!erpPatternExtractionInstance.save(flush: true)) {
            render(view: "edit", model: [erpPatternExtractionInstance: erpPatternExtractionInstance, erpDataPreprocessings: ErpDataPreprocessing.findAllByExperiment(erpPatternExtractionInstance.experiment)])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), erpPatternExtractionInstance.artifactFileName])
        redirect(action: "show", id: erpPatternExtractionInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def erpPatternExtractionInstance = ErpPatternExtraction.get(id)
        if (!erpPatternExtractionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(erpPatternExtractionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        try {
            erpPatternExtractionInstance.set = null
            erpPatternExtractionInstance.format = null
            erpPatternExtractionInstance.erpDataPreprocessing = null
//            erpPatternExtractionInstance.save(insert: false,flush: true,failOnError: true)
            erpPatternExtractionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), erpPatternExtractionInstance.artifactFileName])
//            redirect(action: "list")
            redirect(action: "list", id: erpPatternExtractionInstance?.experiment?.id, controller: "experiment")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'erpPatternExtraction.label', default: 'File'), erpPatternExtractionInstance.artifactFileName])
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def upload(Integer id) {
        def erpPatternExtractionInstance = ErpPatternExtraction.get(id)

        Boolean editable = userService.isAdminOrCurrent(erpPatternExtractionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        CommonsMultipartFile uploadedFile = request.getFile('newRdf')

        if (uploadedFile.empty) {
            flash.message = 'file cannot be empty'
            redirect(action: "show", id: id)
//            render(view: 'uploadForm')
            return
        }

        log.debug "datafile format: " + erpPatternExtractionInstance.format
        def message = validateFileName(uploadedFile.originalFilename, erpPatternExtractionInstance.format)
        if (message) {
            flash.message = 'File must end with extension ' + message
            redirect(id: erpPatternExtractionInstance.id, controller: "erpPatternExtraction", action: "edit", model: [erpPatternExtractionInstance: erpPatternExtractionInstance])
            return
        }

        log.debug 'artifact FileName: ' + erpPatternExtractionInstance.artifactFileName


//        handleErpPatternExtractionUpload(uploadedFile, erpPatternExtractionInstance)
//        handleBinaryUpload(uploadedFile, erpPatternExtractionInstance)
        handleBinaryUpload(erpPatternExtractionInstance, uploadedFile)

        redirect(action: "show",id:erpPatternExtractionInstance.id)

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
        ErpPatternExtraction erpPatternExtraction = ErpPatternExtraction.get(id)
        if (!erpPatternExtraction) {
            response.status = 404
            return
        }
        File file = new File(getDownloadsPath() + erpPatternExtraction.artifactFileName)
        response.setHeader("Content-Disposition", "attachment;filename=${erpPatternExtraction.artifactFileName}")
        log.debug "setting content type ot string "
        response.setContentType("text/plain")
        response.outputStream << file.text
    }

    private boolean isTextFile(ErpPatternExtraction erpPatternExtraction) {
        switch (erpPatternExtraction.format.name) {
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

}
