package edu.uoregon.nic.nemo.portal

class DataFormat extends Ontological{

    static hasMany = [
            erpAnalysisResults: ErpAnalysisResult
    ]

    static belongsTo = [
            ErpAnalysisResult
    ]

}
