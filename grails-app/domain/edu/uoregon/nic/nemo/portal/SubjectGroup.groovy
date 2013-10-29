package edu.uoregon.nic.nemo.portal

class SubjectGroup {

    static constraints = {
        experiment nullable: false
        identifier nullable: false, blank: false, unique: true
        groupSize nullable: false
        genus nullable: false
        species nullable: false
        groupAge nullable: true, min: 1, max: 99
        unverifiedCopy nullable: false
    }

    static hasMany = [
            languages: Language,
            diagnosticClassifications: DiagnosticClassification
    ]

    static belongsTo = [
            Experiment
    ]

    Experiment experiment

    String identifier
    String genus
    String species
//    String diagnosticClassification
    Integer numberMaleStudySubjectsRetainedForAnalysis
    Integer numberRightHandedStudySubjectsRetainedForAnalysis
//    String groupLanguage
    Date createdAt
    Date updatedAt
    Integer groupSize
    Integer groupAge
    Boolean unverifiedCopy = false

}
