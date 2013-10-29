package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class PatchController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [patchInstanceList: Patch.list(params), patchInstanceTotal: Patch.count()]
    }

    def create() {
        [patchInstance: new Patch(params)]
    }

    def save() {
        def patchInstance = new Patch(params)
        if (!patchInstance.save(flush: true)) {
            render(view: "create", model: [patchInstance: patchInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'patch.label', default: 'Patch'), patchInstance.id])
        redirect(action: "show", id: patchInstance.id)
    }

    def show() {
        def patchInstance = Patch.get(params.id)
        if (!patchInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "list")
            return
        }

        [patchInstance: patchInstance]
    }

    def edit() {
        def patchInstance = Patch.get(params.id)
        if (!patchInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "list")
            return
        }

        [patchInstance: patchInstance]
    }

    def update() {
        def patchInstance = Patch.get(params.id)
        if (!patchInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (patchInstance.version > version) {
                patchInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'patch.label', default: 'Patch')] as Object[],
                          "Another user has updated this Patch while you were editing")
                render(view: "edit", model: [patchInstance: patchInstance])
                return
            }
        }

        patchInstance.properties = params

        if (!patchInstance.save(flush: true)) {
            render(view: "edit", model: [patchInstance: patchInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'patch.label', default: 'Patch'), patchInstance.id])
        redirect(action: "show", id: patchInstance.id)
    }

    def delete() {
        def patchInstance = Patch.get(params.id)
        if (!patchInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "list")
            return
        }

        try {
            patchInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'patch.label', default: 'Patch'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
