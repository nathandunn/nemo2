package edu.uoregon.nic.nemo.portal

class PatternExtractionCondition extends Ontological {

    static mappedBy = [
            baselineConditionPatternExtraction: 'baselineCondition'
            ,conditionOfInterestPatternExtraction: 'conditionOfInterest'
    ]

    static hasMany = [
            baselineConditionPatternExtraction: ErpPatternExtraction
            ,conditionOfInterestPatternExtraction: ErpPatternExtraction
    ]


//    static belongsTo = [
//            ErpPatternExtraction
//    ]

}
