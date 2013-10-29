package edu.uoregon.nic.nemo.portal

class ThresholdQuality extends Ontological {

    static hasMany = [
            erpAnalysisResult: ErpAnalysisResult
    ]

    static belongsTo = [
            ErpAnalysisResult
    ]

}
