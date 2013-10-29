package edu.uoregon.nic.nemo.portal

class AnalysisVariable extends Ontological {
    static mappedBy=  [
            dependentErpAnalysisResults: 'dependentVariable'
            ,independentErpAnalysisResults: 'independentVariable'
    ]


    static hasMany = [
            dependentErpAnalysisResults: ErpAnalysisResult
            ,independentErpAnalysisResults: ErpAnalysisResult
    ]

//    static belongsTo = [
//            ErpAnalysisResult
//    ]

}
