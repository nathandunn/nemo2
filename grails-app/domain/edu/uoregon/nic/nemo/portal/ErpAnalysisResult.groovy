package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.NemoFileHandler
import edu.uoregon.nemo.nic.portal.util.ProcessingStatus

class ErpAnalysisResult {

    static belongsTo = [
            experiment: Experiment
            ,erpPatternExpression: ErpPatternExtraction
    ]

    static hasMany = [
            patternImage: PatternImage
            ,individuals:Individual
    ]

    static constraints = {
        artifactFileName unique: true, nullable: false, blank: false
//        set nullable: false
//        format nullable: false
    }

    static mapping = {
        rdfContent type: "text"
        inferredOntology type: "text"
        id generator: 'identity'
    }


    DataSet set
    DataFormat format // TODO: only an RDF file for right now
    String artifactFileName

    String rdfContent // the actual rdf file, was xmlContent
    String inferredOntology // the inferred xml ontology
    Integer processing = ProcessingStatus.UNPROCESSED.value()

//    Date lastUploaded
    Date startClassification
    Date endClassification
    Date lastUploaded
//    ErpPatternExtraction erpPatternExtraction
    AnalysisVariable dependentVariable
    AnalysisVariable independentVariable
    AnalysisMethod analysisMethod
    ThresholdQuality thresholdQuality
    Float significanceThreshold



    boolean isNemo() {
        return artifactFileName.endsWith(NemoFileHandler.NEMO_SUFFIX_TYPE)
    }

    void setDoneProcessing() {
        processing = ProcessingStatus.DONE.value()
        endClassification = new Date()
    }

    void setInProcess() {
        processing = ProcessingStatus.IN_PROCESS.value()
        startClassification = new Date()
    }

    void setErrorProcessing() {
        processing = ProcessingStatus.ERROR.value()
        endClassification = new Date()
    }

    ProcessingStatus getProcessingStatus() {
        return ProcessingStatus.getTypeForValue(processing)
    }

    Boolean isRdfAvailable() {
        inferredOntology && processing == ProcessingStatus.DONE.value()
    }

    Boolean isRdfInProcess() {
        return processing == ProcessingStatus.IN_PROCESS.value()
    }

    Boolean isRdfError() {
        processing == ProcessingStatus.ERROR.value()
    }

    int processingMinutes() {
        return (endClassification.time - startClassification.time) / 1000f / 60f
    }

    Boolean isDownloadable() {
        return rdfContent != null
//        <g:if test="${(dataFileInstance.isRdf() && dataFileInstance.isRdfAvailable()) || (!dataFileInstance.isRdf() && dataFileInstance.download)}">
    }
}
