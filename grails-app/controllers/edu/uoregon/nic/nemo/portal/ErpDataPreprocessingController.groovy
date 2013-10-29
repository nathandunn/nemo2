package edu.uoregon.nic.nemo.portal

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class ErpDataPreprocessingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.erpDataPreprocessing?.experiment
        model.experimentHeader = model.experimentHeader ?: model.erpDataPreprocessingInstance?.experiment
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
		Integer maxInt = (params.max ?: 10) as Integer
	    if(!maxInt) {
			maxInt = 10 
		}
        params.max = Math.min(maxInt ?: 10, 100)

        params.sort = (params.sort) ? params.sort : "identifier"
        params.order = (params.order) ? params.order : "asc"

        if (params.related && !params.related.contains("_")) {
            log.error "Related term exists, but does not contain an underscore [${params.related}]"
            params.related = null
        }

        def model

        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("ErpEvent")) {
                ErpEvent erpEvent = ErpEvent.findById(relatedID)
                List<ErpDataPreprocessing> erpDataPreprocessingList = ErpDataPreprocessing.findAllByEvent(erpEvent,params)
                def count  = ErpDataPreprocessing.countByEvent(erpEvent)
                model = [erpDataPreprocessingInstanceList: erpDataPreprocessingList
                        , erpDataPreprocessingInstanceTotal:  count
                        , related: erpEvent]
            }
            else
            if (relatedClass.contains("OfflineReference")) {
                OfflineReference offlineReference = OfflineReference.findById(relatedID)
                List<ErpDataPreprocessing> erpDataPreprocessingList = ErpDataPreprocessing.findAllByReference(offlineReference,params)
                def count  = ErpDataPreprocessing.countByReference(offlineReference)
                model = [erpDataPreprocessingInstanceList: erpDataPreprocessingList
                        , erpDataPreprocessingInstanceTotal:  count
                        , related: offlineReference]
            }
            else
            if (relatedClass.contains("CleaningTransformation")) {
                CleaningTransformation cleaningTransformation = CleaningTransformation.findById(relatedID)

                // for some reason fails to sort
                List<ErpDataPreprocessing> erpDataPreprocessingList
                    erpDataPreprocessingList =
                        ErpDataPreprocessing.executeQuery(" select erp from CleaningTransformation ct join ct.erpDataPreprocessings erp where ct=:cleaningTransform ",[cleaningTransform:cleaningTransformation],params)

//                def count  = ErpDataPreprocessing.countByCleaningTransformations([cleaningTransformation] as Set)
                def count =  cleaningTransformation?.erpDataPreprocessings?.size()
                model = [erpDataPreprocessingInstanceList: erpDataPreprocessingList
                        , erpDataPreprocessingInstanceTotal:  count
                        , related: cleaningTransformation]
            }
            else{
                model = getErpDataPreprocessings(id)
            }

        } else {
            model =  getErpDataPreprocessings(id)
        }


        if(model.erpDataPreprocessingInstanceList.size()==1) {
            redirect(action:'show',id:model.erpDataPreprocessingInstanceList[0].id)
            return
        }
        else{
            render(view:"list",model: model)
        }

    }

    private def getErpDataPreprocessings(id) {
//        def id = params.id
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            [erpDataPreprocessingInstanceList: ErpDataPreprocessing.findAllByExperiment(experiment, params), erpDataPreprocessingInstanceTotal: ErpDataPreprocessing.countByExperiment(experiment), experimentHeader: experiment]
        } else {
            [erpDataPreprocessingInstanceList: ErpDataPreprocessing.list(params), erpDataPreprocessingInstanceTotal: ErpDataPreprocessing.count()]
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def experimentInstance = Experiment.get(id)
        [erpDataPreprocessingInstance: new ErpDataPreprocessing(params), experimentInstance: experimentInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def erpDataPreprocessingInstance = new ErpDataPreprocessing(params)
        if (!erpDataPreprocessingInstance.save(flush: true)) {
            render(view: "create", model: [erpDataPreprocessingInstance: erpDataPreprocessingInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), erpDataPreprocessingInstance.identifier])
        redirect(action: "show", id: erpDataPreprocessingInstance.id)
    }

    def show(Integer id) {
        def erpDataPreprocessingInstance = ErpDataPreprocessing.get(id)
        if (!erpDataPreprocessingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), id])
            redirect(action: "list")
            return
        }

        [erpDataPreprocessingInstance: erpDataPreprocessingInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def erpDataPreprocessingInstance = ErpDataPreprocessing.get(id)
        if (!erpDataPreprocessingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), id])
            redirect(action: "list")
            return
        }
        Boolean editable = userService.isAdminOrCurrent(erpDataPreprocessingInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        [erpDataPreprocessingInstance: erpDataPreprocessingInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def erpDataPreprocessingInstance = ErpDataPreprocessing.get(id)
        if (!erpDataPreprocessingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (erpDataPreprocessingInstance.version > version) {
                erpDataPreprocessingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings')] as Object[],
                        "Another user has updated this ErpDataPreprocessing while you were editing")
                render(view: "edit", model: [erpDataPreprocessingInstance: erpDataPreprocessingInstance])
                return
            }
        }
        Boolean editable = userService.isAdminOrCurrent(erpDataPreprocessingInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show", id: erpDataPreprocessingInstance.id)
            return
        }

        erpDataPreprocessingInstance.properties = params

        if (!erpDataPreprocessingInstance.save(flush: true)) {
            render(view: "edit", model: [erpDataPreprocessingInstance: erpDataPreprocessingInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), erpDataPreprocessingInstance.identifier])
        redirect(action: "show", id: erpDataPreprocessingInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def erpDataPreprocessingInstance = ErpDataPreprocessing.get(id)
        if (!erpDataPreprocessingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), id])
            redirect(action: "list")
            return
        }
        Boolean editable = userService.isAdminOrCurrent(erpDataPreprocessingInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        if(erpDataPreprocessingInstance.dataFiles){
            flash.message = "Disassociate ${erpDataPreprocessingInstance.dataFiles.size()} data file before deleting."
            redirect(action: "show", id: id)
            return
        }

        try {
            erpDataPreprocessingInstance.cleaningTransformations?.clear()
            erpDataPreprocessingInstance.event = null
            erpDataPreprocessingInstance.reference = null

            erpDataPreprocessingInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), erpDataPreprocessingInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings'), erpDataPreprocessingInstance.identifier])
            redirect(action: "show", id: id)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def addTransformation(Integer id) {
        ErpDataPreprocessing erpDataPreprocessing = ErpDataPreprocessing.get(id)
        if (!userService.isAdminOrCurrent(erpDataPreprocessing?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        CleaningTransformation cleaningTransformation = CleaningTransformation.findById(params.transformId)
        if (cleaningTransformation && erpDataPreprocessing) {
            cleaningTransformation.addToErpDataPreprocessings(erpDataPreprocessing)
            erpDataPreprocessing.addToCleaningTransformations(cleaningTransformation)
        }

        response.status = 200
        render new Object() as JSON
    }


    @Secured(['ROLE_VERIFIED'])
    def removeTransformation(Integer id) {
        ErpDataPreprocessing erpDataPreprocessing = ErpDataPreprocessing.get(id)
        if (!userService.isAdminOrCurrent(erpDataPreprocessing?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        CleaningTransformation cleaningTransformation = CleaningTransformation.findById(params.transformId)
        if (cleaningTransformation && erpDataPreprocessing) {
            cleaningTransformation.removeFromErpDataPreprocessings(erpDataPreprocessing)
            erpDataPreprocessing.removeFromCleaningTransformations(cleaningTransformation)
        }

        redirect([action: "edit", id: erpDataPreprocessing.id, controller: "erpDataPreprocessing"])
    }
}
