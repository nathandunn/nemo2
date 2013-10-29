package edu.uoregon.nic.nemo.portal


class Patch {

    static constraints = {
        name nullable: false
        dateApplied nullable: false
    }

    String name
    Date dateApplied
    String note
}
