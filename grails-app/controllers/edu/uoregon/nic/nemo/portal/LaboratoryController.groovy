package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

class LaboratoryController {

    def ontologyService
    def userService
    def springSecurityService
    def nemoMailService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer id) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [laboratoryInstanceList: Laboratory.listOrderByIdentifier(params), laboratoryInstanceTotal: Laboratory.count()]
    }

    @Secured(['ROLE_VERIFIED'])
    def create(Integer id) {
        [laboratoryInstance: new Laboratory(params), institutions: Institution.findAll(sort: "name", order: "asc")]
    }

    @Secured(['ROLE_VERIFIED'])
    def save() {

        if (params.otherInstitution) {
            params.institution = params.otherInstitution
        }

        if (params.otherPi) {
            params.principalInvestigatorRole = params.otherPi
        }

        def laboratoryInstance = new Laboratory(params)

        if (!laboratoryInstance.save(flush: true)) {
            render(view: "create", model: [laboratoryInstance: laboratoryInstance, institutions: Institution.findAll(sort: "name", order: "asc")])
            return
        }

        if (params.otherInstitution) {
            nemoMailService.emailOtherInstitution(laboratoryInstance)
        }
        if (params.otherPi) {
            nemoMailService.emailOtherPi(laboratoryInstance)
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), laboratoryInstance.identifier])
        redirect(action: "show", id: laboratoryInstance.id)
    }

    def show(Integer id) {
        def laboratoryInstance = Laboratory.get(id)
        if (!laboratoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "list")
            return
        }

        def experiments = Experiment.findAllByLaboratory(laboratoryInstance)

        [laboratoryInstance: laboratoryInstance, experiments: experiments]
    }

    @Secured(['ROLE_VERIFIED'])
    def edit(Integer id) {
        def laboratoryInstance = Laboratory.get(id)
        if (!laboratoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(laboratoryInstance.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        [laboratoryInstance: laboratoryInstance, institutions: Institution.findAll(sort: "name", order: "asc")]
    }

    @Secured(['ROLE_VERIFIED'])
    def update(Long id) {
        def laboratoryInstance = Laboratory.get(id)
        if (!laboratoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(laboratoryInstance.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (laboratoryInstance.version > version) {
                laboratoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'laboratory.label', default: 'Laboratory')] as Object[],
                        "Another user has updated this Laboratory while you were editing")
                render(view: "edit", model: [laboratoryInstance: laboratoryInstance, institutions: Institution.findAll(sort: "name", order: "asc")])
                return
            }
        }

        if (params.otherInstitution) {
            params.institution = params.otherInstitution
        }

        if (params.otherPi) {
            params.principalInvestigatorRole = params.otherPi
        }

        // have to manually remove them
        if (laboratoryInstance.experiments) {
            List<Experiment> experiments = []
            experiments += laboratoryInstance.experiments
            experiments.each { it ->
                if (it.laboratory != null && it.laboratory != laboratoryInstance) {
                    flash.message = "Experiment ${it.identifier} claimed by other lab.  Can not change."
                    render(view: "edit", model: [laboratoryInstance: laboratoryInstance, institutions: Institution.findAll(sort: "name", order: "asc")])
                    return;
                }
                it.laboratory = null
                it.save(flush: true, validate: true, failOnError: true)
            }
        }

        if (!params.users) {
            laboratoryInstance.users = null
        }

        // if an experiment already belongs to a lab
        laboratoryInstance.properties = params

        if (!laboratoryInstance.save(flush: true)) {
            render(view: "edit", model: [laboratoryInstance: laboratoryInstance, institutions: Institution.findAll(sort: "name", order: "asc")])
            return
        }


        if (params.otherInstitution) {
            nemoMailService.emailOtherInstitution(laboratoryInstance)
        }
        if (params.otherPi) {
            nemoMailService.emailOtherPi(laboratoryInstance)
        }


        flash.message = message(code: 'default.updated.message', args: ['Laboratory', laboratoryInstance.identifier])
        redirect(action: "show", id: laboratoryInstance.id)
    }

    @Secured(['ROLE_VERIFIED'])
    def delete(Integer id) {
        def laboratoryInstance = Laboratory.get(id)
        if (!laboratoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(laboratoryInstance.users, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        try {
            laboratoryInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'laboratory.label', default: 'Laboratory'), id])
            redirect(action: "show", id: id)
        }
    }
}
