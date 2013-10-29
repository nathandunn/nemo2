package edu.uoregon.nic.nemo.portal

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class NemoMailService {

    def mailService
    def springSecurityService
    def grailsLinkGenerator

    def adminUsers = [
            'ndunn@cas.uoregon.edu',
            'gfrishkoff@gsu.edu'
    ]

    def emailComplete(ErpAnalysisResult erpAnalysisResult, String userEmail) {
//        def userEmail = springSecurityService?.currentUser?.username
        if (userEmail) {
            mailService.sendMail {
                log.debug "sending mail"
                log.debug "sending email to: " + userEmail
                from 'ndunn@cas.uoregon.edu'
                to userEmail
                cc 'ndunn@cas.uoregon.edu'
                subject "Finished inferring datafile ${erpAnalysisResult.artifactFileName} "
//                    htseq-grails/run/show/73
                def bodyString = "Finished inferring the RDF file "
                bodyString += "<a href='" + grailsLinkGenerator.link([absolute: true, action: 'show', id: erpAnalysisResult.id, controller: 'erpAnalysisResult']) + "'>${erpAnalysisResult.artifactFileName}</a> "
                bodyString += "at ${erpAnalysisResult.endClassification}.  It took ${erpAnalysisResult.processingMinutes()} minutes. "
                html bodyString
            }

        }
    }

    def emailError(ErpAnalysisResult erpAnalysisResult, String userEmail) {
        if (userEmail) {
            mailService.sendMail {
                log.debug "sending error mail"
                log.debug "sending error email to: " + userEmail
                from 'ndunn@cas.uoregon.edu'
                to userEmail
                subject "There was an error inferring the datafile ${erpAnalysisResult.artifactFileName} "
//                    htseq-grails/run/show/73
                def bodyString = "Finished inferring the RDF file "
                bodyString += "<a href='" + grailsLinkGenerator.link([absolute: true, action: 'show', id: erpAnalysisResult.id, controller: 'erpAnalysisResult']) + "'>${erpAnalysisResult.artifactFileName}</a> "
                bodyString += "at ${erpAnalysisResult.endClassification}.  It took ${erpAnalysisResult.processingMinutes()} minutes. "
                html bodyString
            }

        }
    }

    def emailOtherInstitution(Laboratory laboratory) {
        mailService.sendMail {
            log.info "sending new institution mail"
            from 'ndunn@cas.uoregon.edu'
            to 'ndunn@cas.uoregon.edu'
            subject "Request for new Institution ${laboratory.institution} for Lab - ${laboratory.identifier}"
            def bodyString = "Request for new Institution \"${laboratory.institution}\" for Lab - "
            bodyString += "<a href='" + grailsLinkGenerator.link([absolute: true, action: 'show', id: laboratory.id, controller: 'laboratory']) + "'>${laboratory.identifier}</a> "
            html bodyString
        }
    }

    def emailOtherPi(Laboratory laboratory) {
        mailService.sendMail {
            log.info "sending new institution mail"
            from 'ndunn@cas.uoregon.edu'
            to 'ndunn@cas.uoregon.edu'
            subject "Request for new PI ${laboratory.principalInvestigatorRole} for Lab - ${laboratory.identifier}"
//                    htseq-grails/run/show/73
            def bodyString = "Request for new PI \"${laboratory.principalInvestigatorRole}\" for Lab - "
            bodyString += "<a href='" + grailsLinkGenerator.link([absolute: true, action: 'show', id: laboratory.id, controller: 'laboratory']) + "'>${laboratory.identifier}</a> "
            html bodyString
        }

    }

    def emailAdministratorsNewUser(SecUser newUser){
        def conf = SpringSecurityUtils.securityConfig
        mailService.sendMail {
            to 'ndunn@cas.uoregon.edu'
            from conf.ui.register.emailFrom
            subject "Registered new user ${newUser.fullName} - ${newUser.username}"
            def bodyString = "Registered new user [${newUser.username}] - "
            bodyString += "  <a href='" + grailsLinkGenerator.link([absolute: true, action: 'show', id: newUser.id, controller: 'secUser']) + "'>${newUser.fullName}</a> "
            html bodyString
        }

    }


}
