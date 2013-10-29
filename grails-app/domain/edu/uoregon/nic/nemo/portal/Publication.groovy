package edu.uoregon.nic.nemo.portal

class Publication {

    static constraints = {
//        experiment nullable: false
        publicationDate min: 1900,max: 2100,nullable: false

//        digitalObjectIdentifier nullable: false,blank: false
        identifier nullable: false,blank: false,unique: true

        type nullable: false
    }

    static belongsTo = [
            Experiment,SecUser
    ]

    static hasMany = [
            authors:SecUser
            ,experiments: Experiment
    ]
//    Experiment experiment

    String familyName
    String titlePaper
    String titleVolume
    String digitalObjectIdentifier
    Date createdAt
    Date updatedAt
//    String publicationType
    PublicationType type

    Integer publicationDate // year
    String identifier

    def renderPubShort() {
        def returnString = titlePaper
        if(publicationDate){
            returnString += " ("
            returnString +=  publicationDate
            returnString += ")"
        }
    }
}
