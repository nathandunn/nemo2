package edu.uoregon.nic.nemo.portal

class ErpDataPreprocessing {

    static hasMany = [
            cleaningTransformations: CleaningTransformation
            ,erpPatternExtractions: ErpPatternExtraction
    ]

    static belongsTo = [
            Experiment
    ]


    static constraints = {
        experiment nullable: false
        identifier nullable: false,unique: true,blank: false

        event nullable: false
        reference nullable: false

        erpEpochLength nullable: false
        baselineLength nullable: false
//        highpassFilterAlgorithm nullable: false
//        lowpassFilterAlgorithm nullable: false
        numberGoodTrials nullable: false
    }

    static mapping = {
        dataFiles sort: 'identifier', order: 'asc'
    }

    Experiment experiment
    Float highpassFilterAlgorithm
    Float lowpassFilterAlgorithm

//    String dataCleaningTransformation
    // cleaningTransforamations



//    String erpEvent
    ErpEvent event

    Integer numberGoodTrials
    Float erpEpochLength
    Float baselineLength

//    String electrophysiologicalDataReference
    OfflineReference reference

    Date createdAt
    Date updatedAt
    String identifier
}
