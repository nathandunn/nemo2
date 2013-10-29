package edu.uoregon.nic.nemo.portal

class Laboratory {

    static constraints = {
        emailAddressPrincipalInvestigator email: true
        identifier minSize:  2, maxSize: 5,nullable: false,blank: false, unique: true
//        principalInvestigatorRole nullable: false, url: true
//        institution nullable: false ,url:  true,blank: false
    }

    static hasMany = [
            experiments: Experiment,
            users: SecUser
    ]

    static mapping = {
        experiments sort:'identifier',order: 'asc'
    }

    String identifier
    Date createdAt
    Date updatedAt
    String institution
    String principalInvestigatorRole
    String emailAddressPrincipalInvestigator
    String principalInvestigatorPostalAddress
}
