package edu.uoregon.nic.nemo.portal

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

class ResponseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def userService
    def springSecurityService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.response?.condition?.experiment
        model.experimentHeader = model.experimentHeader ?: model.responseInstance?.condition?.experiment
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

            // responseType
            if (relatedClass.contains("Role")) {
                ResponseRole responseRole = ResponseRole.findById(relatedID)
                List<Response> responseList = Response.findAllByRole(responseRole,params)
                def count  = Response.countByRole(responseRole)
                [responseInstanceList: responseList
                        , responseInstanceTotal:  count
                        , related: responseRole]
            }
            else
            if (relatedClass.contains("Modality")) {
                ResponseModality responseModality = ResponseModality.findById(relatedID)
                List<Response> responseList = Response.findAllByModality(responseModality,params)
                def count  = Response.countByModality(responseModality)
                [responseInstanceList: responseList
                        , responseInstanceTotal:  count
                        , related: responseModality]
            }
        }
        else{
            [responseInstanceList: Response.list(params), responseInstanceTotal: Response.count()]
        }
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        def conditionInstance = Condition.get(id)
        [responseInstance: new Response(params),conditionInstance:conditionInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def save(Integer id) {
        def responseInstance = new Response(params)
        if (!responseInstance.save(flush: true)) {
            render(view: "create", model: [responseInstance: responseInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'response.label', default: 'Response'), responseInstance.identifier])
        redirect(action: "show", id: responseInstance.id)
    }

    def show(Integer id) {
        def responseInstance = Response.get(id)
        if (!responseInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'response.label', default: 'Response'), id])
            redirect(action: "list")
            return
        }

        [responseInstance: responseInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def responseInstance = Response.get(id)
        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'response.label', default: 'Response'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent( responseInstance?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        [responseInstance: responseInstance]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Integer id) {
        def responseInstance = Response.get(id)
        if (!responseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'response.label', default: 'Response'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent( responseInstance?.condition?.experiment?.laboratory?.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (responseInstance.version > version) {
                responseInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'response.label', default: 'Response')] as Object[],
                          "Another user has updated this Response while you were editing")
                render(view: "edit", model: [responseInstance: responseInstance])
                return
            }
        }

        responseInstance.properties = params

        if (!responseInstance.save(flush: true)) {
            render(view: "edit", model: [responseInstance: responseInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'response.label', default: 'Response'), responseInstance.identifier])
        redirect(action: "show", id: responseInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def responseInstance = Response.get(id)
        if (!responseInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'response.label', default: 'Response'), id])
            redirect(action: "list")
            return
        }

        try {
            responseInstance.role = null
            responseInstance.modality = null
            responseInstance.software = null
            responseInstance.device = null

            responseInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'response.label', default: 'Response'), responseInstance.identifier])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'response.label', default: 'Response'), responseInstance.identifier])
            redirect(action: "show", id: id)
        }
    }
}
