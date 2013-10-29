package edu.uoregon.nic.nemo.portal

class AnalysisMethod extends Ontological {

    static hasMany = [
            erpAnalysisResult: ErpAnalysisResult
    ]

    static belongsTo = [
            ErpAnalysisResult
    ]

}
