package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class StimulusController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.stimulus?.condition?.experiment
        model.experimentHeader = model.experimentHeader ?: model.stimulusInstance?.condition?.experiment
    }

    def index(Integer id) {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        params.sort = (params.sort) ? params.sort : "identifier"
        params.order = (params.order) ? params.order : "asc"


        if (params.related && !params.related.contains("_")) {
            log.error "Related term exists, but does not contain an underscore [${params.related}]"
            params.related = null
        }


        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("StimulusType")) {
                StimulusType stimulusType = StimulusType.findById(relatedID)
                List<Stimulus> stimulusList = Stimulus.findAllByTargetType(stimulusType,params)
                def count  = Stimulus.countByTargetType(stimulusType)
                [stimulusInstanceList: stimulusList
                        , stimulusInstanceTotal:  count
                        , related: stimulusType]
            }
            else
            if (relatedClass.contains("StimulusModality")) {
                StimulusModality stimulusModality = StimulusModality.findById(relatedID)
                List<Stimulus> stimulusList = Stimulus.findAllByTargetModality(stimulusModality,params)
                def count  = Stimulus.countByTargetModality(stimulusModality)
                [stimulusInstanceList: stimulusList
                        , stimulusInstanceTotal:  count
                        , related: stimulusModality]
            }
        }
        else{
            [stimulusInstanceList: Stimulus.list(params), stimulusInstanceTotal: Stimulus.count()]
        }

    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def conditionInstance = Condition.get(id)
        [stimulusInstance: new Stimulus(params),conditionInstance:conditionInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def save(Integer id) {
        def stimulusInstance = new Stimulus(params)
        if (!stimulusInstance.save(flush: true)) {
            render(view: "create", model: [stimulusInstance: stimulusInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), stimulusInstance.identifier])
        redirect(action: "show", id: stimulusInstance.id)
    }

    def show(Integer id) {
        def stimulusInstance = Stimulus.get(id)
        if (!stimulusInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), id])
            redirect(action: "list")
            return
        }

        [stimulusInstance: stimulusInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def stimulusInstance = Stimulus.get(id)
        if (!stimulusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), id])
            redirect(action: "list")
            return
        }
        Boolean editable = userService.isAdminOrCurrent(stimulusInstance?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        render view:"edit" , model:[stimulusInstance: stimulusInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        println "params = ${params}"
        def stimulusInstance = Stimulus.get(id)
        if (!stimulusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(stimulusInstance?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)
        println "editable ${editable}"
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show",id: id)
            return
        }

        println "params.version ${params.version}"
        if (params.version) {
            def version = params.version.toLong()
            println "evaluating ${stimulusInstance.version} > ${version} "
            if (stimulusInstance.version > version) {
                stimulusInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'stimulus.label', default: 'Stimulus')] as Object[],
                          "Another user has updated this Stimulus while you were editing")
                render(view: "edit", model: [stimulusInstance: stimulusInstance])
                return
            }
        }



        stimulusInstance.properties = params

        if (!stimulusInstance.save(flush: true)) {
            println "failed to save!" + stimulusInstance.errors
            render(view: "edit", model: [stimulusInstance: stimulusInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), stimulusInstance.identifier])
        redirect(action: "show", id: stimulusInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def stimulusInstance = Stimulus.get(id)
        if (!stimulusInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), id])
            redirect(action: "list")
            return
        }
        Boolean editable = userService.isAdminOrCurrent(stimulusInstance?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        try {
            stimulusInstance.targetType = null
            stimulusInstance.targetModality = null
            stimulusInstance.primeType = null
            stimulusInstance.primeModality = null
            stimulusInstance.primeQuality = null
            stimulusInstance.presentationDevice = null
            stimulusInstance.presentationSoftware = null
            stimulusInstance?.targetQualities?.clear()


            stimulusInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'stimulus.label', default: 'Stimulus'), stimulusInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'stimulus.label', default: 'Stimulus'),  stimulusInstance.identifier])
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def addQuality(Integer id) {
        log.debug "entering adding quality" + params
        Stimulus stimulus = Stimulus.get(id)
        if (!userService.isAdminOrCurrent( stimulus?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        StimulusQuality stimulusQuality = StimulusQuality.findById(params.qualityId)
        if (stimulusQuality && stimulus) {
            stimulusQuality.addToStimuli(stimulus)
            stimulus.addToTargetQualities(stimulusQuality)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeQuality(Integer id) {
        log.debug "entering remove quality " + params
        Stimulus stimulus = Stimulus.get(id)
        if (!userService.isAdminOrCurrent( stimulus?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        StimulusQuality stimulusQuality = StimulusQuality.findById(params.qualityId)
        if (stimulusQuality && stimulus) {
            stimulusQuality.removeFromStimuli(stimulus)
            stimulus.removeFromTargetQualities(stimulusQuality)
        }

        redirect([action: "edit",id: stimulus.id,controller: "stimulus"])
    }
}
