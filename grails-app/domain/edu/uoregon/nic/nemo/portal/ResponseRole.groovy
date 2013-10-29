package edu.uoregon.nic.nemo.portal

class ResponseRole extends Ontological{

    static hasMany = [
            responses: Response
    ]

    static belongsTo = [
            Response
    ]
}
