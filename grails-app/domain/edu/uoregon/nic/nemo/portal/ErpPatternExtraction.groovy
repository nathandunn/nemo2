package edu.uoregon.nic.nemo.portal

/**
 *  These are .m files.
 *
 *  https://casspr.fogbugz.com/default.asp?235#10204
 *
 *  - multiple files per experiment
 - not shared across experiments
 - one file per entry / row
 - one of each, just subclass the ones that are there
 - m file is optional
 - needs to have a Data File ID?
 - all fields are required
 */
class ErpPatternExtraction {

    static constraints = {
        artifactFileName unique: false,nullable: false,blank: false
    }

    static belongsTo = [
            experiment: Experiment
            ,erpDataPreprocessing: ErpDataPreprocessing
    ]

    static hasMany = [
            erpAnalysisResults: ErpAnalysisResult
    ]

    static mapping = {
        download type: "text"
    }

    DataSet set
    DataFormat format // TODO: only a Matlab file for right now
    String artifactFileName  // erpPatternExtractionId
//    ErpDataPreprocessing erpDataPreprocessing

    String download

    PatternExtractionMethod method
    PatternExtractionCondition baselineCondition
    PatternExtractionCondition conditionOfInterest
}
