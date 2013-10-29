package edu.uoregon.nic.nemo.portal

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class ExperimentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def ontologyService
    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.experiment
        model.experimentHeader = model.experimentHeader ?: model.experimentInstance
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)

        params.sort = (params.sort) ? params.sort : "identifier"
        params.order = (params.order) ? params.order : "asc"

        if (params.related && !params.related.contains("_")) {
            log.error "Related term exists, but does not contain an underscore [${params.related}]"
            params.related = null
        }

        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("ExperimentalParadigm")) {
                ExperimentalParadigm experimentalParadigm = ExperimentalParadigm.findById(relatedID)
                List<Experiment> experimentList =
                    Experiment.executeQuery("select e from Experiment e join e.experimentalParadigms ep where ep=:paradigm "
                            , [paradigm: experimentalParadigm], params)
                def count = experimentList?.size()
                [experimentInstanceList: experimentList
                        , experimentInstanceTotal: count
                        , related: experimentalParadigm]
            }
        } else {
            def experiments = Experiment.list(params)
//            for (experiment in experiments) {
//                if (!experiment.experimentalParadigmLabel && experiment.experimentalParadigm) {
//                    experiment.setExperimentalParadigmLabel(ontologyService.getLabelTextForYamlUrls(experiment.experimentalParadigm, " &bull; "))
//                }
//            }
            [experimentInstanceList: experiments, experimentInstanceTotal: Experiment.count()]
        }

//        def paradigms = dataFileService.getParadigms(experiments)
//        def experiments = Experiment.list(params)

    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        Experiment experiment = new Experiment(params)

        println "lab id: ${params}"


        SecUser currentUser = (SecUser) springSecurityService.currentUser
        def availableLabs = null
        if (userService.isAdmin(currentUser)) {
            availableLabs = Laboratory.listOrderByIdentifier()
        } else if (currentUser.laboratories?.size() == 1) {
            experiment.laboratory = currentUser.laboratories.iterator().next()
        } else if (currentUser.laboratories?.size() > 1) {
            availableLabs = currentUser.laboratories
        }
        [experimentInstance: experiment, availableLabs: availableLabs]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def experimentInstance = new Experiment(params)
//        experimentInstance.laboratory = springSecurityService?.currentUser?.laboratories
        if (!experimentInstance.save(flush: true)) {
            render(view: "create", model: [experimentInstance: experimentInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'experiment.label', default: 'Experiment'), experimentInstance.identifier])
        redirect(action: "show", id: experimentInstance.id)
    }

    def show(Integer id) {
        def experimentInstance = Experiment.get(id)
        if (!experimentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), id])
            redirect(action: "list")
            return
        }

        // combined set of erps from experiment and data_file processing . . . a bit confusing personally
        Set<ErpDataPreprocessing> erpDataPreprocessingSet = new HashSet<ErpDataPreprocessing>()
        if (experimentInstance.erpDataPreprocessings) {
            erpDataPreprocessingSet.addAll(experimentInstance.erpDataPreprocessings)
        }
        for (ErpDataPreprocessing erpDataPreprocessing in experimentInstance.erpDataPreprocessings) {
//            if (erpDataPreprocessing.) {
            erpDataPreprocessingSet.add(erpDataPreprocessing)
//                log.debug " adding from erp file: " + dataFile.erpDataPreprocessing.dataCleaningTransformation.substring(0, 20)
//            }
        }

//        def publications = experimentInstance.publications
//        def publications = Publication.executeQuery("from Publication p where p.experiments in :experiments",[experiments:[experimentInstance]])
//        def subjectGroups = experimentInstance.subjectGroups

        def subjectGroups
        if (userService.isAdminOrCurrent(experimentInstance?.laboratory?.users, springSecurityService.currentUser)) {
            subjectGroups = experimentInstance.subjectGroups
        } else {
            subjectGroups = SubjectGroup.findAllByExperimentAndUnverifiedCopy(experimentInstance, false)
        }

////        SubjectGroup.findAllByExperiment(experimentInstance)
        def conditions
        if (userService.isAdminOrCurrent(experimentInstance?.laboratory?.users, springSecurityService.currentUser)) {
            conditions = Condition.findAllByExperiment(experimentInstance)
        } else {
            conditions = Condition.findAllByExperimentAndUnverifiedCopy(experimentInstance, false)
        }


        def stimuli = Stimulus.findAllByConditionInList(conditions)
        def responses = Response.findAllByConditionInList(conditions)
//        def eegDataCollections = EegDataCollection.findAllByExperiment(experimentInstance)


        [
                experimentInstance: experimentInstance
                , erpDataPreprocessingSet: experimentInstance.erpDataPreprocessings
                , publications: experimentInstance.publications
                , subjectGroups: subjectGroups
                , conditions: conditions
                , stimuli: stimuli
                , responses: responses
                , eegDataCollections: experimentInstance.eegDataCollections
        ]
    }



    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def experimentInstance = Experiment.get(id)
        if (!experimentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(experimentInstance.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        SecUser currentUser = (SecUser) springSecurityService.currentUser
        def availableLabs = null
        if (userService.isAdmin(currentUser)) {
            availableLabs = Laboratory.listOrderByIdentifier()
        } else if (currentUser.laboratories?.size() == 1) {
            experimentInstance.laboratory = currentUser.laboratories.iterator().next()
        } else if (currentUser.laboratories?.size() > 1) {
            availableLabs = currentUser.laboratories
        }
        [experimentInstance: experimentInstance, availableLabs: availableLabs]
    }

    @Secured(['ROLE_VERIFIED'])
    def addExperimentalParadigm(Integer id) {
        log.debug "entering adding paradigm" + params
        Experiment experimentInstance = Experiment.get(id)
        if (!userService.isAdminOrCurrent(experimentInstance.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        ExperimentalParadigm experimentalParadigm = ExperimentalParadigm.findById(params.paradigmId)
        if (experimentalParadigm && experimentInstance) {
            experimentalParadigm.addToExperiments(experimentInstance)
            experimentInstance.addToExperimentalParadigms(experimentalParadigm)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeExperimentalParadigm(Integer id) {
        log.debug "entering remove paradigm" + params
        Experiment experimentInstance = Experiment.get(id)
        if (!userService.isAdminOrCurrent(experimentInstance.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        ExperimentalParadigm experimentalParadigm = ExperimentalParadigm.findById(params.paradigmId)
        if (experimentalParadigm && experimentInstance) {
            experimentalParadigm.removeFromExperiments(experimentInstance)
            experimentInstance.removeFromExperimentalParadigms(experimentalParadigm)
        }

        redirect([action: "edit", id: experimentInstance.id, controller: "experiment"])
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def experimentInstance = Experiment.get(id)
        if (!experimentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(experimentInstance.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (experimentInstance.version > version) {
                experimentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'experiment.label', default: 'Experiment')] as Object[],
                        "Another user has updated this Experiment while you were editing")
                render(view: "edit", model: [experimentInstance: experimentInstance])
                return
            }
        }

        experimentInstance.properties = params

        if (!experimentInstance.save(flush: true)) {
            render(view: "edit", model: [experimentInstance: experimentInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'experiment.label', default: 'Experiment'), experimentInstance.identifier])
        redirect(action: "show", id: experimentInstance.id)
    }


    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def experimentInstance = Experiment.get(id)
        if (!experimentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(experimentInstance.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        if (experimentInstance.dataFiles) {
            flash.message = "Remove associated Data Files ${experimentInstance.dataFiles.size()}"
            return redirect(action: "show", id: id)
        }
        if (experimentInstance.conditions) {
            flash.message = "Remove associated Conditions ${experimentInstance.conditions.size()}"
            return redirect(action: "show", id: id)
        }
        if (experimentInstance.erpDataPreprocessings) {
            flash.message = "Remove associated Erp Data Preprocessings ${experimentInstance.erpDataPreprocessings.size()}"
            return redirect(action: "show", id: id)
        }
        if (experimentInstance.publications) {
            flash.message = "Remove associated Publications ${experimentInstance.publications.size()}"
            return redirect(action: "show", id: id)
        }
        if (experimentInstance.subjectGroups) {
            flash.message = "Remove associated Subject Groups ${experimentInstance.subjectGroups.size()}"
            return redirect(action: "show", id: id)
        }
        if (experimentInstance.eegDataCollections) {
            flash.message = "Remove associated Eeg Data Collections ${experimentInstance.eegDataCollections.size()}"
            return redirect(action: "show", id: id)
        }


        try {
            experimentInstance.laboratory = null
            experimentInstance.experimentalParadigms = null
//            experimentInstance.publications = null

            experimentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'experiment.label', default: 'Experiment'), experimentInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'experiment.label', default: 'Experiment'), experimentInstance.identifier])
            redirect(action: "show", id: id)
        }
    }

    def generateHeadItLink(Integer id) {
        Experiment experiment = Experiment.findById(id)
        if (!experiment) render ""
//        String headItLink = experiment.createHeadItLink()
        String urlString = "http://headit.aciss.uoregon.edu/studies/${experiment.headItLink}/description"
        URL url = new URL(urlString)
        String content = url.getContent()
        if (content.size() < 10) {
            urlString = "http://headit.aciss.uoregon.edu/studies/${experiment.headItLink}"
        }
        render "<a class='external headit' href='${urlString}' target='_blank'>HeadIT Data&nbsp;&nbsp;&nbsp;</a>"

    }


}
