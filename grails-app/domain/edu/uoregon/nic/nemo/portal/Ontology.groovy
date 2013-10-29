package edu.uoregon.nic.nemo.portal

class Ontology {

    static constraints = {
        name nullable: false
//        date default: new Date()
    }
    static mapping = {
        xmlContent  type: "text"
    }

    String name   // ontology name
    String description // any processing that was done
    String xmlContent
//    Date date
}
