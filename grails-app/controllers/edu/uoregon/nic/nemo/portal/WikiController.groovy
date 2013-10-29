package edu.uoregon.nic.nemo.portal

/**
 * An empty class for navigation only
 */
class WikiController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def wiki(){
        redirect(url:"http://nemo.nic.uoregon.edu/wiki/NEMO")
    }

    def submitHelp(){
        println "submitHelp: " + params
        def from = params.from
        def subject = params.subject
        def message = params.message

//        // if from is valid email, subject is not blank and message is not blank
//        if (  ){
//            flash.error = "Email must be valid"
//        }
//        else
//        if ( ){
//
//            flash.error = "Subject must not be blank!"
//        }
//        else
//        if( ){
//
//            flash.message = "Body must not be blank!"
//        }
//        else{
//            sendMail {
//                to "ndunn@uoregon.edu"
//                subject subject
//                body message
//            }
//            flash.message = "message sent!"
//        }
        redirect(url:"/")
    }

    def index() {
        redirect(action:'wiki')
    }
}
