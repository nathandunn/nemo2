package edu.uoregon.nic.nemo.portal

class Language extends Ontological{

    static hasMany = [
            subjectGroups: SubjectGroup
    ]

    static belongsTo = [
            SubjectGroup
    ]

}
