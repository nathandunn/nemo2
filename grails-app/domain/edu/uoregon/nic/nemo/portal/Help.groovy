package edu.uoregon.nic.nemo.portal

class Help {

    static constraints = {
        emailFrom email: true, blank: false
        subject blank:  false
        message blank:  false
    }

    String emailFrom
    String subject
    String message
}
