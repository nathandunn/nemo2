package edu.uoregon.nic.nemo.portal

import org.springframework.dao.DataIntegrityViolationException

class HelpController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [helpInstanceList: Help.list(params), helpInstanceTotal: Help.count()]
    }

    def create(Integer id) {
        [helpInstance: new Help(params)]
    }

    def save() {
        def helpInstance = new Help(params)
        if (!helpInstance.save(flush: true)) {
            render(view: "create", model: [helpInstance: helpInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'help.label', default: 'Help'), helpInstance.id])
        try {
            sendMail {
                to helpInstance.emailFrom
                subject helpInstance.subject
                body helpInstance.message
            }
        } catch (e) {
            log.error("unable to send mail ${e}")
        }
        redirect(action: "show", id: helpInstance.id)
    }

    def show(Integer id) {
        def helpInstance = Help.get(id)
        if (!helpInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "list")
            return
        }

        [helpInstance: helpInstance]
    }

    def edit(Integer id) {
        def helpInstance = Help.get(id)
        if (!helpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "list")
            return
        }

        [helpInstance: helpInstance]
    }

    def update(Integer id) {
        def helpInstance = Help.get(id)
        if (!helpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (helpInstance.version > version) {
                helpInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'help.label', default: 'Help')] as Object[],
                          "Another user has updated this Help while you were editing")
                render(view: "edit", model: [helpInstance: helpInstance])
                return
            }
        }

        helpInstance.properties = params

        if (!helpInstance.save(flush: true)) {
            render(view: "edit", model: [helpInstance: helpInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'help.label', default: 'Help'), helpInstance.id])
        redirect(action: "show", id: helpInstance.id)
    }

    def delete(Integer id) {
        def helpInstance = Help.get(id)
        if (!helpInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "list")
            return
        }

        try {
            helpInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'help.label', default: 'Help'), id])
            redirect(action: "show", id: id)
        }
    }
}
