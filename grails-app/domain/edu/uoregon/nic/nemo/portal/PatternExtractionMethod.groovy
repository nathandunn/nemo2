package edu.uoregon.nic.nemo.portal

class PatternExtractionMethod extends Ontological {

    static hasMany = [
            erpPatternExtraction: ErpPatternExtraction
    ]

    static belongsTo = [
            ErpPatternExtraction
    ]

}
