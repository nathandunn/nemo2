package edu.uoregon.nic.nemo.portal

class DiagnosticClassification extends Ontological{

    static hasMany = [
            subjectGroups: SubjectGroup
    ]

    static belongsTo = [
            SubjectGroup
    ]

}
