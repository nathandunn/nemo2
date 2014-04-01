package edu.uoregon.nic.nemo.portal

import org.codehaus.groovy.grails.plugins.springsecurity.NullSaltSource
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode

class RegisterController extends grails.plugins.springsecurity.ui.RegisterController {

    def nemoMailService
    def grailsLinkGenerator

    def index = {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        List<Laboratory> laboratories = Laboratory.findAll(sort:"identifier",order:"asc")
        edu.uoregon.nic.nemo.portal.RegisterCommand registerCommand = new edu.uoregon.nic.nemo.portal.RegisterCommand(copy)
        def model =  [command: registerCommand,laboratories: laboratories]
        render view: "index",model: model
    }

    def register = { RegisterCommand command ->

        List<Laboratory> laboratories = Laboratory.findAll(sort:"identifier",order:"asc")

        if (command.hasErrors()) {
            flash.error = message(code: 'spring.security.ui.register.miscError')
            render view: 'index', model: [command: command,laboratories: laboratories]
            return
        }

        Laboratory userLaboratory
        if(command.laboratoryId && command.laboratoryId != 'null'){
            userLaboratory = Laboratory.findById(command.laboratoryId as Integer)
        }

        println " laboratory id: ${command.laboratoryId} -> ${userLaboratory}"

        String salt = saltSource instanceof NullSaltSource ? null : command.username
        SecUser user = lookupUserClass().newInstance(
//                email: command.email
                username: command.username
                , accountLocked: true
                , enabled: true
                , fullName: command.fullName
        )



        if(userLaboratory){
            user.addToLaboratories(userLaboratory)
            userLaboratory.addToUsers(user)
        }

//        Role role = Role.findByAuthority(Role.ROLE_VERIFIED)
//        SecUserRole secUserRole = new SecUserRole(
//                secUser: user
//                ,role: role
//        )

        RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
        if (registrationCode == null || registrationCode.hasErrors()) {
            // null means problem creating the user
            flash.error = message(code: 'spring.security.ui.register.miscError')
            flash.chainedParams = params
            redirect action: 'index'
            return
        }

        String url = generateLink('verifyRegistration', [t: registrationCode.token])

        def conf = SpringSecurityUtils.securityConfig
        def body = conf.ui.register.emailBody
        if (body.contains('$')) {
            body = evaluate(body, [user: user, url: url])
        }
        mailService.sendMail {
            to command.username
            from conf.ui.register.emailFrom
            subject conf.ui.register.emailSubject
            html body.toString()
        }

        nemoMailService.emailAdministratorsNewUser(user)

        render view: 'index', model: [emailSent: true]
    }

    def verifyRegistration = {

        def conf = SpringSecurityUtils.securityConfig
//        String defaultTargetUrl = conf.successHandler.defaultTargetUrl
        String defaultTargetUrl = grailsLinkGenerator.link([action:"show",controller: "secUser",absolute: true])

        String token = params.t

        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        def user
        RegistrationCode.withTransaction { status ->
            user = lookupUserClass().findByUsername(registrationCode.username)
            if (!user) {
                return
            }
            user.accountLocked = false
            user.save(flush:true)
            def UserRole = lookupUserRoleClass()
            def Role = lookupRoleClass()
            for (roleName in conf.ui.register.defaultRoleNames) {
                UserRole.create user, Role.findByAuthority(roleName)
            }
            registrationCode.delete()
        }

        if (!user) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        springSecurityService.reauthenticate user.username

        flash.message = message(code: 'spring.security.ui.register.complete')
        redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl
    }
}

