package edu.uoregon.nic.nemo.portal

import grails.plugins.springsecurity.Secured

class ErrorController {

    def index() {}

    @Secured(['ROLE_ADMIN'])
    def test500Error() {
        response.setStatus(500)
//        render view: "error"
    }

    @Secured(['ROLE_ADMIN'])
    def testExceptionThrown() {
        if(true){
            throw new RuntimeException("The site has failed!")
        }
        return
    }

    def internalError(){
        println "coming to internalError"

    }

    def notFound(){
    }
}
