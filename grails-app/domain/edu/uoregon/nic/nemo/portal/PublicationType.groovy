package edu.uoregon.nic.nemo.portal

class PublicationType extends Ontological{

    def getNameOnly(){
        if(!name && url ){
            return url.split("#")[1].replaceAll("_"," ")
        }
        return name?.capitalize()
    }

}
