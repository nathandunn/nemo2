package edu.uoregon.nic.nemo.portal

class DataSet extends Ontological{

    static hasMany = [
            erpAnalysisResults: ErpAnalysisResult
    ]

    static belongsTo = [
            ErpAnalysisResult
    ]

}
