package edu.uoregon.nic.nemo.portal

class Response {

    static belongsTo = [
            condition:Condition
    ]
    static constraints = {
        identifier nullable: false,blank: false,unique: true

        role nullable: false
        modality nullable: false
    }


    Condition condition

//    String responseRole
    ResponseRole role

//    String responseModality
    ResponseModality modality

    Float responseAccuracy
    Float responseDeadline

    Float responseTime
//    String experimentControlSoftware
    Software software

//    String responseDevice
    ResponseDevice device

    Date createdAt
    Date updatedAt
    String identifier
}
