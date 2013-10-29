package edu.uoregon.nic.nemo.portal

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured


@Secured(['ROLE_UNVERIFIED'])
class SecUserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def springSecurityService
    def userService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [secUserInstanceList: SecUser.list(params), secUserInstanceTotal: SecUser.count()]
    }

    def create() {
        redirect(action: "list")
    }

    def save() {

        def secUserInstance = new SecUser(params)

        if (!userService.isAdmin(springSecurityService.currentUser)) {
            log.warn "denied!"
            render(view: "/login/denied")
            return
        }

        if (!secUserInstance.save(flush: true)) {
            render(view: "create", model: [secUserInstance: secUserInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'secUser.label', default: 'SecUser'), secUserInstance.username])
        redirect(action: "show", id: secUserInstance.id)
    }

    def show(Integer id) {
        if (!id) {
            id = springSecurityService?.currentUser?.id
        }
        def secUserInstance = SecUser.get(id)
        if (!secUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "list")
            return
        }

        [secUserInstance: secUserInstance]
    }

    def edit(Integer id) {
        if (!id) {
            id = springSecurityService?.currentUser?.id
        }
        def secUserInstance = SecUser.get(id)
        if (!secUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent( secUserInstance, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        render view:"edit",model:[secUserInstance: secUserInstance]
    }

    def update(Integer id) {
        SecUser secUserInstance = SecUser.get(id)
        if (!secUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "list")
            return
        }
        if (!userService.isAdminOrCurrent(secUserInstance, springSecurityService.currentUser)) {
            render(view: "/login/denied")
        }

        if (params.version) {
            def version = params.version.toLong()
            if (secUserInstance.version > version) {
                secUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'secUser.label', default: 'SecUser')] as Object[],
                        "Another user has updated this SecUser while you were editing")
                render(view: "edit", model: [secUserInstance: secUserInstance])
                return
            }
        }

        if (params.password) {
            if (params.password.equals(params.password2)) {
                secUserInstance.password = params.password
            } else {
                secUserInstance.errors.rejectValue("password", "default.password.doesnotmatch", "Passwords do not match")
                render(view: "edit", model: [userInstance: secUserInstance])
                return
            }
        }
        else{
            params.password = secUserInstance.password
        }

        List<Laboratory> laboratories = []
        laboratories += secUserInstance.laboratories
        laboratories.each { it ->
            secUserInstance.removeFromLaboratories(it)
        }

        params.laboratories?.id?.each { it ->
            if(it.toString().isLong()){
                Laboratory lab = Laboratory.get(it)
                lab.addToUsers(secUserInstance)
                secUserInstance.addToLaboratories(lab)
            }
        }


        if (!secUserInstance.save(flush: true)) {
            render(view: "edit", model: [secUserInstance: secUserInstance])
            return
        }


        flash.message = message(code: 'default.updated.message', args: [message(code: 'secUser.label', default: 'SecUser'), secUserInstance.username])
        redirect(action: "show", id: secUserInstance.id)
    }

    def delete(Long id) {
        if (!userService.isAdmin(springSecurityService.currentUser)) {
            render(view: "/login/denied")
            return
        }

        def secUserInstance = SecUser.get(id)
        if (!secUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "list")
            return
        }
        if(secUserInstance == springSecurityService.currentUser){
            flash.message = "You can't delete yourself ${secUserInstance.fullName}."
            redirect(action: "show", id: id)
            return
        }
        if(secUserInstance.laboratories){
            flash.message = "Could not delete '${secUserInstance.fullName}'.  Remove laboratories first."
            redirect(action: "show", id: id)
            return
        }
        if(secUserInstance.publications){
            flash.message = "Could not delete '${secUserInstance.fullName}'.  Remove publications first."
            redirect(action: "show", id: id)
            return
        }

        try {
            List<SecUserRole> secUserRoleList = SecUserRole.findAllBySecUser(secUserInstance)
            SecUserRole.deleteAll(secUserRoleList)
            secUserInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'secUser.label', default: 'SecUser'), id])
            redirect(action: "show", id: id)
        }
    }
}
