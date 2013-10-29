package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class PublicationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: null
        def experiments = model.publication?.experiments
        if(!model.experimentHeader && experiments?.size()==1){
            model.experimentHeader  = experiments.iterator().next()
            return
        }
        experiments = model.publicationInstance?.experiments
        if(!model.experimentHeader && experiments?.size()==1){
            model.experimentHeader  = experiments.iterator().next()
        }
    }

    def index(Integer id) {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def model
        if (id && Experiment.findById(id)) {
            Experiment experiment = Experiment.findById(id)
            model = [publicationInstanceList: experiment.publications, publicationInstanceTotal: experiment?.publications?.size(), experimentHeader: experiment]
        } else {
            model = [publicationInstanceList: Publication.list(params), publicationInstanceTotal: Publication.count()]
        }

        if(model.publicationInstanceList.size()==1) {
            redirect(action:'show',id:model.publicationInstanceList.iterator().next().id)
            return
        }
        else{
            render(view:"list",model: model)
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        if (id) {
            Experiment experiment = Experiment.findById(id)
            params.experiments = [experiment]
        }
        id = null
        [publicationInstance: new Publication(params)]
    }

    @Secured(['ROLE_VERIFIED'])
    def save(Integer id) {
        def publicationInstance = new Publication(params)
        if (!publicationInstance.save(flush: true)) {
            render(view: "create", model: [publicationInstance: publicationInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'publication.label', default: 'Publication'), publicationInstance.titlePaper])
        redirect(action: "show", id: publicationInstance.id)
    }

    def show(Integer id) {
        def publicationInstance = Publication.get(id)
        if (!publicationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'publication.label', default: 'Publication'), id])
            redirect(action: "list")
            return
        }

        [publicationInstance: publicationInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def publicationInstance = Publication.get(id)
        if (!publicationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'publication.label', default: 'Publication'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(publicationInstance.experiments?.laboratory?.users?.flatten(), (SecUser) springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        [publicationInstance: publicationInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {

        def publicationInstance = Publication.get(id)
        if (!publicationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'publication.label', default: 'Publication'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(publicationInstance.experiments?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (publicationInstance.version > version) {
                publicationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'publication.label', default: 'Publication')] as Object[],
                        "Another user has updated this Publication while you were editing")
                render(view: "edit", model: [publicationInstance: publicationInstance])
                return
            }
        }

        // have to manually remove them
        List<Experiment> experiments = []
        experiments += publicationInstance.experiments
        experiments.each { it ->
            publicationInstance.removeFromExperiments(it)
            it.removeFromPublications(publicationInstance)
        }

        List<SecUser> authors = []
        authors += publicationInstance.authors
        authors.each { it ->
            publicationInstance.removeFromAuthors(it)
            it.removeFromPublications(publicationInstance)
        }

        if (!params.authors) {
            publicationInstance.authors = null
        }

        publicationInstance.properties = params

        if (!publicationInstance.save(flush: true)) {
            render(view: "edit", model: [publicationInstance: publicationInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'publication.label', default: 'Publication'), publicationInstance.titlePaper])
        redirect(action: "show", id: publicationInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def publicationInstance = Publication.get(id)


        if (!publicationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'publication.label', default: 'Publication'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(publicationInstance.experiments?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }
        if (publicationInstance.experiments) {
            flash.message = "Could not delete '${publicationInstance.titlePaper}'.  Disassociate experiments, first."
            redirect(action: "show", id: id)
            return
        }
        if (publicationInstance.authors) {
            flash.message = "Could not delete '${publicationInstance.titlePaper}'.  Disassociate authors, first."
            redirect(action: "show", id: id)
            return
        }

        try {
            publicationInstance.type = null
            publicationInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'publication.label', default: 'Publication'), publicationInstance.titlePaper])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'publication.label', default: 'Publication'), id])
            redirect(action: "show", id: id)
        }
    }

}
