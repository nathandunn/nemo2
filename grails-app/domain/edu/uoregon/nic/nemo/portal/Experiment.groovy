package edu.uoregon.nic.nemo.portal

class Experiment {

    // can have one or more, but all currently have 1 or 0!
    static hasMany = [
            publications: Publication,
            subjectGroups: SubjectGroup,
            conditions: Condition,
            eegDataCollections: EegDataCollection,
            erpDataPreprocessings: ErpDataPreprocessing,


            erpAnalysisResults: ErpAnalysisResult,
            erpPatternExtractions: ErpPatternExtraction,


            // this was a hasOne
            experimentalParadigms: ExperimentalParadigm
    ]

    static belongsTo = [
            Laboratory
    ]

//    static hasOne = [
//            EegDataCollection
//    ]

    static mapping = {
        erpPatternExtractionNarrativeSummary type: "text"
        erpAnalysisResultNarrativeSummary type: "text"
    }

    static constraints = {
        identifier nullable: false,blank: false,unique: true
//        laboratory nullable: false
        startDateForDataCollection min: 1900, max:  2100
        endDateForDataCollection min: 1900, max:  2100

        narrativeSummary nullable: true,blank: true
        subjectGroupsNarrativeSummary nullable: true,blank: true
        conditionsNarrativeSummary nullable: true,blank: true
        erpDataPreprocessingsNarrativeSummary nullable: true,blank: true
        eegDataCollectionNarrativeSummary nullable: true,blank: true
//        dataFilesNarrativeSummary nullable: true,blank: true
        erpPatternExtractionNarrativeSummary nullable: true,blank: true
        erpAnalysisResultNarrativeSummary nullable: true,blank: true
        headItLink nullable: true,blank: false, minSize: 36, maxSize: 38
    }

    def beforeValidate() {
        identifier = identifier?.toUpperCase()
    }

    Laboratory laboratory

    String identifier
    Date createdAt
    Date updatedAt
//    String experimentalParadigm
//    String experimentalParadigmLabel
    String subjectGroupsNarrativeSummary
    String conditionsNarrativeSummary
    String erpDataPreprocessingsNarrativeSummary
    String narrativeSummary
    Integer startDateForDataCollection
    Integer endDateForDataCollection
    String eegDataCollectionNarrativeSummary
//    String dataFilesNarrativeSummary
    String erpPatternExtractionNarrativeSummary
    String erpAnalysisResultNarrativeSummary
    String headItLink
//    String inferredSubClasses // yaml inferred subcalsses

    def getErpAnalysisResultsList(){
        def  erpAnalysisResultsList = []

        for(ErpAnalysisResult erpAnalysisResult in erpAnalysisResults){
//            if(erpAnalysisResult.isRdfAvailable()){
                erpAnalysisResultsList << erpAnalysisResult
//            }
        }
        return erpAnalysisResultsList
    }

//    http://headit.aciss.uoregon.edu/studies/af331b72-8526-11e2-83cc-0050563ff472/description
    String createHeadItLink(){
        if(headItLink){
            return "http://headit.aciss.uoregon.edu/studies/${headItLink}/description"
        }
        return ""
    }
}
