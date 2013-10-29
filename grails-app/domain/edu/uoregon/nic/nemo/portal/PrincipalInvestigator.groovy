package edu.uoregon.nic.nemo.portal

class PrincipalInvestigator extends Ontological{


    static belongsTo = [
        Laboratory
    ]

    static hasMany = [
            laboratories: Laboratory
    ]

    String getNameOnly(){
        if(!name && url ){
            return url.split("#")[1].replaceAll("_"," ")
        }
        return name
    }
}
