package edu.uoregon.nic.nemo.portal

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

class ConditionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService
    def experimentService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.conditionInstance?.experiment
        model.experimentHeader = model.experimentHeader ?: model.condition?.experiment
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        if(params.max){
           params.max = Math.min(params.int('max') ,100)
        }
        else{
            params.max = Math.min(10 ,100)
        }

        log.debug "ConditionController list [${params.properties}]]"
//        edu.uoregon.nic.nemo.portal.TaskInstruction : 166

        if(params.related && !params.related.contains("_")){
            log.error "Related term exists, but does not contain an underscore [${params.related}]"
            params.related = null
        }

        def model

        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("TaskInstruction")) {
                TaskInstruction taskInstruction = TaskInstruction.findById(relatedID)
                model =[conditionInstanceList: Condition.findAllByTaskInstruction(taskInstruction, params)
                        , conditionInstanceTotal: Condition.countByTaskInstruction(taskInstruction)
                        , related: taskInstruction]
            } else
            if (relatedClass.contains("ConditionType")) {
                ConditionType conditionType = ConditionType.findById(relatedID)
                params.limit = params.max
//                List<Condition> conditionList = Condition.executeQuery("select c from Condition c where ? in c.types ",[conditionType],params)
                List<Condition> conditionList = Condition.executeQuery("select c from ConditionType ct join ct.conditions c where ct=:conditionType ", [conditionType: conditionType], params)

                def conditionCount = conditionType?.conditions?.size()
                model =[conditionInstanceList: conditionList
                        , conditionInstanceTotal: conditionCount
                        , related: conditionType]
            } else {
                println "error handling related class: " + relatedClass + " for " + params.related
            }

        } else if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            if (userService.isAdminOrCurrent(experiment?.laboratory?.users, springSecurityService.currentUser)) {
                model =[conditionInstanceList: Condition.findAllByExperiment(experiment, params), conditionInstanceTotal: Condition.countByExperiment(experiment), experimentHeader: experiment]
            } else {
                model =[conditionInstanceList: Condition.findAllByExperimentAndUnverifiedCopy(experiment, false, params), conditionInstanceTotal: Condition.countByExperimentAndUnverifiedCopy(experiment, false), experimentHeader: experiment]
            }
        } else {
            if (userService.isAdmin(springSecurityService.currentUser)) {
                model =[conditionInstanceList: Condition.list(params), conditionInstanceTotal: Condition.count()]
            } else {
                model =[conditionInstanceList: Condition.findAllByUnverifiedCopy(false, params), conditionInstanceTotal: Condition.countByUnverifiedCopy(false)]
            }
        }


        if(model.conditionInstanceList.size()==1) {
            redirect(action:'show',id:model.conditionInstanceList[0].id)
            return
        }
        else{
            render(view:"list",model: model)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        Experiment experiment = Experiment.findById(id)
        params.experiment = experiment
        id = null
        Condition condition = new Condition(params)
        [conditionInstance: condition]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def conditionInstance = new Condition(params)
        if (!conditionInstance.save(flush: true)) {
            render(view: "create", model: [conditionInstance: conditionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'condition.label', default: 'Condition'), conditionInstance.identifier])
        redirect(action: "show", id: conditionInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def copyCondition(Integer id) {
        def experiment = Experiment.get(id)
        if (!experiment) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Experiment'), id])
            redirect(action: "list", controller: "experiment")
            return
        }
        def oldConditionInstance = Condition.get(params.conditionId)
        if (!oldConditionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'subjectGroup.label', default: 'Subjects'), params.subjectGroupId])
            redirect(action: "show", controller: "experiment", id: experiment.id)
            return
        }

        if (!userService.isAdminOrCurrent(oldConditionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        Condition newConditionCopy = new Condition(oldConditionInstance.properties)
        newConditionCopy.id = null
        newConditionCopy.identifier += "-copy"
        newConditionCopy.experiment = experiment
        newConditionCopy.unverifiedCopy = true
        newConditionCopy.stimulus = null
        newConditionCopy.response = null
        newConditionCopy.types = null

        if (!newConditionCopy.save(flush: true, insert: true)) {
            redirect(action: "show", controller: "experiment", id: experiment.id)
            return
        }

        experimentService.replicateConditionParameters(oldConditionInstance,newConditionCopy)


        if (!newConditionCopy.save(flush: true, insert: true)) {
            redirect(action: "show", controller: "experiment", id: experiment.id)
            return
        }

        render(view: "edit", id: newConditionCopy.id, model: [conditionInstance: newConditionCopy])
    }

    def show(Integer id) {
        def conditionInstance = Condition.get(id)
        if (!conditionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'condition.label', default: 'Condition'), id])
            redirect(action: "list")
            return
        }

        List<Stimulus> stimulusList = Stimulus.findAllByCondition(conditionInstance)
        List<Response> responseList = Response.findAllByCondition(conditionInstance)

        [conditionInstance: conditionInstance, stimulusList: stimulusList, responseList: responseList]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def conditionInstance = Condition.get(id)

        if (!conditionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'condition.label', default: 'Condition'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(conditionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        render view: "edit", model: [conditionInstance: conditionInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def conditionInstance = Condition.get(id)
        if (!conditionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'condition.label', default: 'Condition'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (conditionInstance.version > version) {
                conditionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'condition.label', default: 'Condition')] as Object[],
                        "Another user has updated this Condition while you were editing")
                render(view: "edit", model: [conditionInstance: conditionInstance])
                return
            }
        }

        conditionInstance.properties = params

        if (!conditionInstance.save(flush: true)) {
            render(view: "edit", model: [conditionInstance: conditionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'condition.label', default: 'Condition'), conditionInstance.identifier])
        redirect(action: "show", id: conditionInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def conditionInstance = Condition.get(id)
        if (!conditionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'condition.label', default: 'Condition'), id])
            redirect(action: "list")
            return
        }

        try {
            //  detatch the types
            conditionInstance.types?.clear()

            conditionInstance?.stimulus?.each { stimulus ->
                stimulus.presentationSoftware?.removeFromStimuli(stimulus)
                stimulus.presentationSoftware = null
                stimulus.presentationDevice?.removeFromStimuli(stimulus)
                stimulus.presentationDevice = null

                stimulus.targetQualities?.each { quality ->
                     quality.removeFromStimuli(stimulus)
                    stimulus.removeFromTargetQualities(quality)
                }

                stimulus.targetModality?.removeFromStimuli(stimulus)
                stimulus.targetModality= null

                stimulus.targetType?.removeFromStimuli(stimulus)
                stimulus.targetType= null


                stimulus.primeQuality?.removeFromStimuli(stimulus)
                stimulus.primeQuality= null

                stimulus.primeModality?.removeFromStimuli(stimulus)
                stimulus.primeModality= null

                stimulus.primeType?.removeFromStimuli(stimulus)
                stimulus.primeType= null


                conditionInstance.removeFromStimulus(stimulus)
                stimulus.delete()
            }

            conditionInstance.response.each { response ->

                response.role?.removeFromResponses(response)
                response.role = null

                response.modality?.removeFromResponses(response)
                response.modality = null

                response.software?.removeFromResponses(response)
                response.software = null

                response.device?.removeFromResponses(response)
                response.device  = null

                conditionInstance.removeFromResponse(response)
                response.delete()
            }

            conditionInstance?.taskInstruction?.removeFromConditions(conditionInstance)
            conditionInstance.taskInstruction = null



            conditionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'condition.label', default: 'Condition'), conditionInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            log.error("Could not delete ${e}")
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'condition.label', default: 'Condition'), conditionInstance.identifier])
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def addType(Integer id) {
        log.debug "entering adding paradigm" + params
        Condition condition = Condition.get(id)
        if (!userService.isAdminOrCurrent(condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        ConditionType conditionType = ConditionType.findById(params.typeId)
        if (conditionType && condition) {
            conditionType.addToConditions(condition)
            condition.addToTypes(conditionType)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeType(Integer id) {
        log.debug "entering remove paradigm" + params
        Condition condition = Condition.get(id)
        if (!userService.isAdminOrCurrent(condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        ConditionType conditionType = ConditionType.findById(params.typeId)
        if (conditionType && condition) {
            conditionType.removeFromConditions(condition)
            condition.removeFromTypes(conditionType)
        }

        redirect([action: "edit", id: condition.id, controller: "condition"])
    }

    @Secured(['ROLE_VERIFIED'])
    def verify(Integer id) {
        params.unverifiedCopy = false
        update(id)
        response.reset()
        render "Verified!"
    }

    @Secured(['ROLE_VERIFIED'])
    def unverify(Integer id) {
        params.unverifiedCopy = true
        update(id)
        response.reset()
        render "Un-Verified!"
    }
}
