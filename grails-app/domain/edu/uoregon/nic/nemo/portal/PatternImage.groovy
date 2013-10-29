package edu.uoregon.nic.nemo.portal

class PatternImage implements Comparable{

    static constraints = {
        erpAnalysisResult nullable: true
        patternName blank: false,nullable: false
    }

    static belongsTo = [
            erpAnalysisResult: ErpAnalysisResult
    ]

    String patternName
    byte[] image
    byte[] rawImage

    int compareTo(t) {
        return patternName.compareTo(t.patternName)
    }
}
