package edu.uoregon.nic.nemo.portal

class ResponseDevice extends Ontological{

    static hasMany = [
            responses: Response
    ]

    static belongsTo = [
            Response
    ]

}
