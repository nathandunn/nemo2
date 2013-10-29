package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured


@Secured(['ROLE_ADMIN'])
class AdminController {

    def ontologyService

    def index() {}

    def updateOntologies(){
        flash.message = "Updating Ontologies if Different"
        ontologyService.updateOntologies()
        render view: "index"
    }
}
