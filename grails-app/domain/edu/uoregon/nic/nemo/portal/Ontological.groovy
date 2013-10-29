package edu.uoregon.nic.nemo.portal
/**
 */
public abstract class Ontological implements Comparable {

    static constraints = {
        url nullable: true, url: true
        name nullable: true, blank: false
    }

    String name
    String url

    String getSuffix(){
        return url ? url.split("#")[1] : null
    }

    int compareTo(Object t) {

        if (t instanceof Ontological){
            return name?.toString()?.compareTo(t?.name)
        }
        else{
            return toString().compareTo(t?.toString())
        }
    }

    String getDescription(){
        return getClass().getSimpleName().replaceAll(/([A-Z])/,' $1').trim()
    }

    String getRelatedLookup(){
        return getClass().getSimpleName()+"_"+getId()
    }
}