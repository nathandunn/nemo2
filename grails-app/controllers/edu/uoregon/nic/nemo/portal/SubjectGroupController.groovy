package edu.uoregon.nic.nemo.portal

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class SubjectGroupController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.subjectGroup?.experiment
        model.experimentHeader = model.experimentHeader ?: model.subjectGroupInstance?.experiment
    }

    def index(Integer id) {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        // if verified or editable experiment, then show all

        def model
        // if admin . . . show all here
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            if (userService.isAdminOrCurrent(experiment?.laboratory?.users, springSecurityService.currentUser)) {
                model = [subjectGroupInstanceList: SubjectGroup.findAllByExperiment(experiment, params), subjectGroupInstanceTotal: SubjectGroup.countByExperiment(experiment), experimentHeader: experiment]
            } else {
                model = [subjectGroupInstanceList:
                        SubjectGroup.findAllByExperimentAndUnverifiedCopy(experiment, false, params)
                        , subjectGroupInstanceTotal:
                        SubjectGroup.countByExperimentAndUnverifiedCopy(experiment, false)
                        , experimentHeader: experiment]
            }
        } else {
            if (userService.isAdmin(springSecurityService.currentUser)) {
                model = [subjectGroupInstanceList: SubjectGroup.list(params), subjectGroupInstanceTotal: SubjectGroup.count()]
            } else {
                model = [subjectGroupInstanceList: SubjectGroup.findAllByUnverifiedCopy(false,params)
                        , subjectGroupInstanceTotal: SubjectGroup.countByUnverifiedCopy(false)]
            }
        }

        if(model.subjectGroupInstanceList.size()==1) {
            redirect(action:'show',id:model.subjectGroupInstanceList[0].id)
            return
        }
        else{
            render(view:"list",model: model)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def experimentInstance = Experiment.get(id)
        [subjectGroupInstance: new SubjectGroup(params), experimentInstance: experimentInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def save(Integer id) {
        def subjectGroupInstance = new SubjectGroup(params)
        if (!subjectGroupInstance.save(flush: true)) {
            render(view: "create", model: [subjectGroupInstance: subjectGroupInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), subjectGroupInstance.identifier])
        redirect(action: "show", id: subjectGroupInstance.id)
    }

    def show(Integer id) {
        def subjectGroupInstance = SubjectGroup.get(id)
        if (!subjectGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), id])
            redirect(action: "list")
            return
        }

        [subjectGroupInstance: subjectGroupInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def copySubjectGroup(Integer id) {
        def experiment = Experiment.get(id)
        if (!experiment) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Experiment'), id])
            redirect(action: "list", controller: "experiment")
            return
        }
        def existingSubjectGroupInstance = SubjectGroup.get(params.subjectGroupId)
        if (!existingSubjectGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), params.subjectGroupId])
            redirect(action: "show", controller: "experiment", id: experiment.id)
            return
        }

        if (!userService.isAdminOrCurrent(existingSubjectGroupInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        SubjectGroup newSubjectGroupInstance = new SubjectGroup(existingSubjectGroupInstance.properties)
        newSubjectGroupInstance.id = null
        newSubjectGroupInstance.identifier += "-copy"
        newSubjectGroupInstance.experiment = experiment
        newSubjectGroupInstance.unverifiedCopy = true
        newSubjectGroupInstance.diagnosticClassifications = null
        newSubjectGroupInstance.languages = null

        existingSubjectGroupInstance.diagnosticClassifications.each {
            newSubjectGroupInstance.addToDiagnosticClassifications(it)
        }

        existingSubjectGroupInstance.languages.each {
            newSubjectGroupInstance.addToLanguages(it)
        }

        if (!newSubjectGroupInstance.save(flush: true, insert: true)) {
            redirect(action: "show", controller: "experiment", id: experiment.id)
            return
        }

        render(view: "edit", id: newSubjectGroupInstance.id, model: [subjectGroupInstance: newSubjectGroupInstance])
    }


    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def subjectGroupInstance = SubjectGroup.get(id)
        if (!subjectGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(subjectGroupInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        [subjectGroupInstance: subjectGroupInstance]
    }


    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def subjectGroupInstance = SubjectGroup.get(id)
        if (!subjectGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(subjectGroupInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (subjectGroupInstance.version > version) {
                subjectGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'subjectGroup.label', default: 'Subjects')] as Object[],
                        "Another user has updated this SubjectGroup while you were editing")
                render(view: "edit", model: [subjectGroupInstance: subjectGroupInstance])
                return
            }
        }

        subjectGroupInstance.properties = params

        if (!subjectGroupInstance.save(flush: true)) {
            render(view: "edit", model: [subjectGroupInstance: subjectGroupInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), subjectGroupInstance.identifier])
        redirect(action: "show", id: subjectGroupInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def subjectGroupInstance = SubjectGroup.get(id)
        if (!subjectGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(subjectGroupInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }


        try {
            subjectGroupInstance.diagnosticClassifications = null
            subjectGroupInstance.experiment = null
            subjectGroupInstance.languages = null


            subjectGroupInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'subjectGroup.label', default: 'Subject'), subjectGroupInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'subjectGroup.label', default: 'Subject'), subjectGroupInstance.identifier])
            redirect(action: "show", id: id)
        }
    }


    @Secured(['ROLE_VERIFIED'])
    def addDiagnosticClassification(Integer id) {
        log.debug "entering add classificaiotn " + params
        SubjectGroup subjectGroup = SubjectGroup.get(id)
        if (!userService.isAdminOrCurrent(subjectGroup?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        DiagnosticClassification diagnosticClassification = DiagnosticClassification.findById(params.classificationId)
        if (diagnosticClassification && subjectGroup) {
            diagnosticClassification.addToSubjectGroups(subjectGroup)
            subjectGroup.addToDiagnosticClassifications(diagnosticClassification)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeDiagnosticClassification(Integer id) {
        log.debug "entering remove classifcation" + params
        SubjectGroup subjectGroup = SubjectGroup.get(id)
        if (!userService.isAdminOrCurrent(subjectGroup?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        DiagnosticClassification diagnosticClassification = DiagnosticClassification.findById(params.classificationId)
        if (diagnosticClassification && subjectGroup) {
            diagnosticClassification.removeFromSubjectGroups(subjectGroup)
            subjectGroup.removeFromDiagnosticClassifications(diagnosticClassification)
        }

        redirect([action: "edit", id: subjectGroup.id, controller: "subjectGroup"])
    }

    @Secured(['ROLE_VERIFIED'])
    def addLanguage(Integer id) {
        log.debug "entering add language " + params
        SubjectGroup subjectGroup = SubjectGroup.get(id)
        if (!userService.isAdminOrCurrent(subjectGroup?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        Language language = Language.findById(params.languageId)
        if (language && subjectGroup) {
            language.addToSubjectGroups(subjectGroup)
            subjectGroup.addToLanguages(language)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeLanguage(Integer id) {
        log.debug "entering remove language " + params
        SubjectGroup subjectGroup = SubjectGroup.get(id)
        if (!userService.isAdminOrCurrent(subjectGroup?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        Language language = Language.findById(params.languageId)
        if (language && subjectGroup) {
            language.removeFromSubjectGroups(subjectGroup)
            subjectGroup.removeFromLanguages(language)
        }


        redirect([action: "edit", id: subjectGroup.id, controller: "subjectGroup"])
    }


    @Secured(['ROLE_VERIFIED'])
    def verify(Integer id) {
        params.unverifiedCopy = false
        update()
        response.reset()
        render "Verified!"
    }

    @Secured(['ROLE_VERIFIED'])
    def unverify(Integer id) {
        params.unverifiedCopy = true
        update()
        response.reset()
        render "Un-Verified!"
    }

}
