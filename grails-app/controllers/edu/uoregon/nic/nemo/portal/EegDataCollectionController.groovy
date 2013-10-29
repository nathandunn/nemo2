package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class EegDataCollectionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.eegDataCollection?.experiment
        model.experimentHeader = model.experimentHeader ?: model.eegDataCollectionInstance?.experiment
    }

    def index() {
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

        def model

        if (params.related) {
            def relatedClass = params.related.split("_")[0].trim()
            def relatedID = params.related.split("_")[1].trim()

            if (relatedClass.contains("ElectrodeArrayLayout")) {
                ElectrodeArrayLayout electrodeArrayLayout = ElectrodeArrayLayout.findById(relatedID)
                List<EegDataCollection> eegDataCollections = EegDataCollection.findAllByElectrodeArrayLayout(electrodeArrayLayout, params)
                def count = EegDataCollection.countByElectrodeArrayLayout(electrodeArrayLayout)
                model = [eegDataCollectionInstanceList: eegDataCollections
                        , eegDataCollectionInstanceTotal: count
                        , related: electrodeArrayLayout]
            } else {
                model = getEegDataCollections(id)
            }
        } else {
            model = getEegDataCollections(id)
        }

        if(model.eegDataCollectionInstanceList.size()==1) {
            redirect(action:'show',id:model.eegDataCollectionInstanceList[0].id)
            return
        }
        else{
            render(view:"list",model: model)
        }

    }

    private def getEegDataCollections(id) {
//        def id = params.id
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            [eegDataCollectionInstanceList: EegDataCollection.findAllByExperiment(experiment, params), eegDataCollectionInstanceTotal: EegDataCollection.countByExperiment(experiment), experimentHeader: experiment]
        } else {
            [eegDataCollectionInstanceList: EegDataCollection.list(params), eegDataCollectionInstanceTotal: EegDataCollection.count()]
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def experimentInstance = Experiment.get(id)
        [eegDataCollectionInstance: new EegDataCollection(params), experimentInstance: experimentInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {
        def eegDataCollectionInstance = new EegDataCollection(params)
        if (!eegDataCollectionInstance.save(flush: true)) {
            render(view: "create", model: [eegDataCollectionInstance: eegDataCollectionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), eegDataCollectionInstance.identifier])
        redirect(action: "show", id: eegDataCollectionInstance.id)
    }

    def show(Integer id) {
        def eegDataCollectionInstance = EegDataCollection.get(id)
        if (!eegDataCollectionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), id])
            redirect(action: "list")
            return
        }

        [eegDataCollectionInstance: eegDataCollectionInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def eegDataCollectionInstance = EegDataCollection.get(id)
        if (!eegDataCollectionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(eegDataCollectionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        [eegDataCollectionInstance: eegDataCollectionInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def eegDataCollectionInstance = EegDataCollection.get(id)
        if (!eegDataCollectionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (eegDataCollectionInstance.version > version) {
                eegDataCollectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition')] as Object[],
                        "Another user has updated this EegDataCollection while you were editing")
                render(view: "edit", model: [eegDataCollectionInstance: eegDataCollectionInstance])
                return
            }
        }

        Boolean editable = userService.isAdminOrCurrent(eegDataCollectionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show", id: eegDataCollectionInstance.id)
            return
        }

        eegDataCollectionInstance.properties = params

        // for some reason have to hack this in
        eegDataCollectionInstance.electrodeArrayLayout = params.electrodeArrayLayout?.id ? ElectrodeArrayLayout.get(params.electrodeArrayLayout?.id) : null

        if (!eegDataCollectionInstance.save(flush: true)) {
            render(view: "edit", model: [eegDataCollectionInstance: eegDataCollectionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), eegDataCollectionInstance.identifier])
        redirect(action: "show", id: eegDataCollectionInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def eegDataCollectionInstance = EegDataCollection.get(id)
        if (!eegDataCollectionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), id])
            redirect(action: "list")
            return
        }

        Boolean editable = userService.isAdminOrCurrent(eegDataCollectionInstance?.experiment?.laboratory?.users, springSecurityService.currentUser)
        if (!editable) {
            flash.message = message(code: 'default.cant.edit', args: [message(code: 'job.condition', default: 'Unable to Edit Condition'), id])
            redirect(action: "show")
            return
        }

        try {
            eegDataCollectionInstance.manufacturer = null
            eegDataCollectionInstance.electrodeArrayLayout = null
            eegDataCollectionInstance.reference = null
            eegDataCollectionInstance.ground = null
            eegDataCollectionInstance.software = null


            eegDataCollectionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), eegDataCollectionInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition'), eegDataCollectionInstance.identifier])
            redirect(action: "show", id: id)
        }
    }
}
