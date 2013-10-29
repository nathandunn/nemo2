package edu.uoregon.nic.nemo.portal

import org.semanticweb.owlapi.model.OWLClass
import org.yaml.snakeyaml.Yaml

class DataStubService {

    def ontologyService

    def bootstrapNewTables() {
        stubErpPatternConditions()
        stubErpExtractionMethods()
        stubAnalysisVariables()
        stubAnalysisMethod()
        stubThresholdQuality()
        addDataSet1()

    }

    def addDataSet1() {
        OWLClass owlClass = ontologyService.getOwlClass("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3872000")
        fixNameWithUrl(DataSet.findOrSaveByUrl(owlClass.toStringID()))
    }

    def stubThresholdQuality() {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1176000")
        log.debug "threshold quality subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(ThresholdQuality.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }

    def stubAnalysisMethod() {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6587000")
        log.debug "analysis method subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(AnalysisMethod.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }

    def stubErpExtractionMethods() {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8429000")
        log.debug "methods subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(PatternExtractionMethod.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }

    def stubErpPatternConditions() {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382")
        log.debug "condition subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(PatternExtractionCondition.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }


    def stubAnalysisVariables() {


        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3752000")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000428")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000429")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000409")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000410")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000411")
        createAnalsisVariables("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000412")

//        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000428")
//        log.debug "analysis subclsses: " + subClassList.size()
//        for (OWLClass subClass in subClassList) {
//            log.debug "subclass url: " + subClass.toStringID()
//            if (!subClass.toStringID().contains("Nothing")) {
//                fixNameWithUrl(AnalysisVariable.findOrSaveByUrl(subClass.toStringID()))
//            }
//        }
//        subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000429")
//        log.debug "analysis subclsses: " + subClassList.size()
//        for (OWLClass subClass in subClassList) {
//            log.debug "subclass url: " + subClass.toStringID()
//            if (!subClass.toStringID().contains("Nothing")) {
//                fixNameWithUrl(AnalysisVariable.findOrSaveByUrl(subClass.toStringID()))
//            }
//        }
//        subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000409")
//        log.debug "analysis subclsses: " + subClassList.size()
//        for (OWLClass subClass in subClassList) {
//            log.debug "subclass url: " + subClass.toStringID()
//            if (!subClass.toStringID().contains("Nothing")) {
//                fixNameWithUrl(AnalysisVariable.findOrSaveByUrl(subClass.toStringID()))
//            }
//        }
    }

    def createAnalsisVariables(String s) {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl(s)
        log.debug "analysis subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(AnalysisVariable.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }


    def bootstrapData() {


        stubExperiments()
        stubDataSets()
        stubDataFormat()

        stubPrincipalInvestigators()
        stubInstitution()
        stubExperimentalParadigms()
        stubPublicationTypes()
        stubDiagnosticClassifications()
        stubLanguages()

        flattenExperimentalParadigms()


        stubSoftware()


        stubTaskInstructions()
        stubConditionTypes()
        stubPresentationDevice()
        stubPresentationSoftware()
        stubStimulusType()
        stubStimulusModality()
        stubStimulusQuality()

        stubResponseDevice()
        stubResponseModality()
        stubResponseRole()
        stubResponseSoftware()

        stubEegSoftware()
        stubElectrodeArrayManufacturer()
        stubElectrodeArrayLayout()

        stubGroundElectrode()
        stubReferenceElectrode()

        stubErpEvent()
        stubOfflineReference()
        stubDataCleaningReferences()

    }

    def fixNameWithUrl(Ontological ontological) {
        if (!ontological.name) {
            ontological.name = ontologyService.getLabelForUrl(ontological.url)?.replaceAll("_", " ")
            if (!ontological.name && ontological.url?.contains("#NEMO_")) {
                ontological.name = ontological.url.split("#")[1]
            }
            ontological.save()
        }
        return ontological
    }


    def stubSoftware() {

        // TODO use bioontoloy REST service

        // add subclasses of     - http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrlFromBlank("http://purl.obolibrary.org/obo/IAO_0000010")

//        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.obolibrary.org/obo/obi.owl#IAO_0000010")
        log.debug "software subclsses: " + subClassList.size()
//        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382")
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(Software.findOrSaveByUrl(subClass.toStringID()))
            }
        }
//        fixNameWithUrl(Software.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6345000"))
//        fixNameWithUrl(Software.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3172000"))
//        fixNameWithUrl(Software.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3252000"))
    }


    def stubExperiments() {
//        experiments.each { experiment ->
////            log.debug "inferring experiment in closure: " + experiment.identifier
//            experiment.dataFiles.each { dataFile ->
//                if (dataFile.isRdf() && dataFile.rdfContent) {
//                    // if there is an RDF and no inferred ontology then asynchronously process
//                    // at some point there will be a database lock here to indicate that it is being processed
//                    if(!dataFile.inferredOntology){
//                        log.debug "trying to do inference ontology dataFile " + dataFile.identifier
//                        ontologyService.inferRdfInstanceAsync(dataFile, WebApplicationContextUtils.getWebApplicationContext(servletContext))
//                    }
//                    else{
//                        log.debug "getting reasoner for dataFile to cache: " + dataFile.identifier
//                        ontologyService.getReasonerForDataFile(dataFile)
//                        log.debug "GOT reasoner for dataFile to cache: " + dataFile.identifier
//                    }
//
//                }
//            }
//        }
    }

    def stubDataSets() {

        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0494000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0705000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2369000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2464000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3971000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5688000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5881000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7766000"))
        fixNameWithUrl(DataSet.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9270000"))

    }

    def stubDataFormat() {

        // TODO subclass of http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1194000
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1194000")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(DataFormat.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2587000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6826000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8913000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6185000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2378000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2241000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2559000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8980000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4869000"))
//        fixNameWithUrl(DataFormat.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6200000"))

    }

    def stubPrincipalInvestigators() {
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#John_Connolly")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Dennis_Molfese")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Tim_Curran")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Don_Tucker")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Chuck_Perfetti")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Kerry_Kilborn")
        PrincipalInvestigator.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl#Gwen_Frishkoff")
    }


    def stubInstitution() {
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0017000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1106000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3080000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3506000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4796000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6235000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7062000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8045000"))
        fixNameWithUrl(Institution.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8064000"))
    }

    def stubExperimentalParadigms() {
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000390"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000392"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000393"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0563000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1563000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1611000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2682000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2887000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3302000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3675000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8253000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8267000"))
        fixNameWithUrl(ExperimentalParadigm.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9459000"))
    }

    def flattenExperimentalParadigms() {

        Yaml yaml = new Yaml()
        Experiment.findAll().each { experiment ->
            if (experiment.experimentalParadigm) {
                for (s in yaml.load(experiment.experimentalParadigm)) {
                    if (s) {
                        // an array list of
                        ExperimentalParadigm experimentalParadigm = ExperimentalParadigm.findByUrl(s)
                        experimentalParadigm.addToExperiments(experiment)

                        experiment.addToExperimentalParadigms(experimentalParadigm)
//                        experimentalParadigm.save()
                    }
                }
                // TODO: pull this to blank it out . .. or just drop the column
//                experiment.experimentalParadigm = null
//                experiment.save()
            }
        }

    }

    def stubPublicationTypes() {
        fixNameWithUrl(PublicationType.findOrSaveByUrl("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Journal_Article"))
        fixNameWithUrl(PublicationType.findOrSaveByUrl("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Thesis"))
        fixNameWithUrl(PublicationType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5688000"))
        fixNameWithUrl(PublicationType.findOrSaveByUrl("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Conference_Proceeding"))
        fixNameWithUrl(PublicationType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5795000"))

        Publication.list().each {
            if (it.publicationType) {
                PublicationType publicationType = fixNameWithUrl(PublicationType.findByUrl(it.publicationType))
                it.type = publicationType
//                it.save()
            }
        }
    }

    def stubDiagnosticClassifications() {
//        fixNameWithUrl(DiagnosticClassification.findOrSaveByUrl("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Journal_Article"))
        fixNameWithUrl(DiagnosticClassification.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3717000"))
        fixNameWithUrl(DiagnosticClassification.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4615001"))


        SubjectGroup.list().each {
            Yaml yaml = new Yaml()
            if (it.diagnosticClassification) {
                for (s in yaml.load(it.diagnosticClassification)) {
                    if (s) {
                        // an array list of
                        DiagnosticClassification diagnosticClassification = DiagnosticClassification.findByUrl(s)
                        it.addToDiagnosticClassifications(diagnosticClassification)

                        diagnosticClassification.addToSubjectGroups(it)
                        diagnosticClassification.save()
                    }
                }
            }
        }
    }

    def stubLanguages() {
//        fixNameWithUrl(Language.findOrSaveByUrl("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Journal_Article"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0632000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1023000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1582000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3946000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6265000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8086000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9160000"))
        fixNameWithUrl(Language.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5035000"))

        SubjectGroup.list().each {
            if (it.groupLanguage) {
                Yaml yaml = new Yaml()
                Iterable<Object> objects = yaml.loadAll(it.groupLanguage)
                for (Object s in objects) {
                    if (s) {
                        if (!s.toString().startsWith("http") && it?.groupLanguage?.contains("http")) {
                            s = it.groupLanguage.substring(it.groupLanguage.indexOf("http"))?.trim()
                        }
                        // an array list of
//                    Language language = fixNameWithUrl(Language.findOrSaveByUrl(s))
                        Language language = Language.findByUrl(s)
                        it.addToLanguages(language)

                        language.addToSubjectGroups(it)
                        language.save()
                    }
                }
            }
        }
    }


    def stubTaskInstructions() {
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000399"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000400"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000401"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000402"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000403"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0471000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3837000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3985000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5594000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5758000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5926000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6898000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8208001"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0507000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1787000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2646000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3388000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6048000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8891000"))
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9887000"))

        // in there, but not with original
        fixNameWithUrl(TaskInstruction.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0537000"))


        Condition.list().each {
            if (it.experimentInstruction) {
                it.taskInstruction = fixNameWithUrl(TaskInstruction.findOrSaveByUrl(it?.experimentInstruction?.trim()))
                it.save()
            }
        }
    }

    def stubConditionTypes() {

        // TODO get subclasses of
//        - http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(ConditionType.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0163000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0590000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2255000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3122000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3173000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3460000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3542000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3800000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5527000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5930000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5932000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5936000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6294000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6508000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8411000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8611000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8752000"))
//        fixNameWithUrl(ConditionType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9936000"))

        Condition.list().each {
            if (it.experimentConditionType) {
                Yaml yaml = new Yaml()
                for (Object s in yaml.load(it.experimentConditionType)) {
                    if (s) {
                        if (!s.toString().startsWith("http") && it?.experimentConditionType.contains("http")) {
                            s = it.experimentConditionType.substring(it.experimentConditionType.indexOf("http"))?.trim()
                        }
                        // an array list of
//                    Language language = fixNameWithUrl(Language.findOrSaveByUrl(s))
                        ConditionType conditionType = fixNameWithUrl(ConditionType.findOrSaveByUrl(s))
                        it.addToTypes(conditionType)

                        conditionType.addToConditions(it)
                        conditionType.save()
                    }
                }
            }
        }

    }

    def stubPresentationDevice() {
        fixNameWithUrl(StimulusPresentationDevice.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2432000"))
        fixNameWithUrl(StimulusPresentationDevice.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6159000"))

        Stimulus.list().each {
            if (it.stimulusPresentationDevice) {
                StimulusPresentationDevice stimulusPresentationDevice = StimulusPresentationDevice.findByUrl(it.stimulusPresentationDevice)
                it.presentationDevice = stimulusPresentationDevice
            }
        }
    }

    def stubPresentationSoftware() {

        Stimulus.list().each {
            if (it.stimulusPresentationSoftware) {
                Software stimulusPresentationSoftware = Software.findByUrl(it.stimulusPresentationSoftware)
                it.presentationSoftware = stimulusPresentationSoftware
            }
        }
    }


    def stubStimulusType() {

        // TODO: do subclasses
//        # specified as subclasses of the following: NEMO_0000409, NEMO_0000410, NEMO_0000411, NEMO_0000412
//        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000382")
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrls(["NEMO_0000409", "NEMO_0000410", "NEMO_0000411", "NEMO_0000412"])
        for (OWLClass subClass : subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(StimulusType.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(StimulusType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000424"))
//        fixNameWithUrl(StimulusType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000420"))
//        fixNameWithUrl(StimulusType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000419"))
//        fixNameWithUrl(StimulusType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6581000"))
//        fixNameWithUrl(StimulusType.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8638000"))


        Stimulus.list().each {
            if (it.targetStimulusType) {
                StimulusType targetStimulusType = StimulusType.findByUrl(it.targetStimulusType)
                it.targetType = targetStimulusType
            }
            if (it.primeStimulusType) {
                StimulusType primeStimulusType = StimulusType.findByUrl(it.primeStimulusType)
                it.primeType = primeStimulusType
            }
        }

    }

    def stubStimulusModality() {

//        log.debug "trying to stub the stimulus type"
//        Set<OWLClass> owlClasses = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6440000")
//        log.debug "number of classes: " + owlClasses.size()
//        owlClasses.each{
//            log.debug "owlClass: "  + it
//        }

        fixNameWithUrl(StimulusModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000453"))
        fixNameWithUrl(StimulusModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000452"))
        fixNameWithUrl(StimulusModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000454"))
        fixNameWithUrl(StimulusModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000456"))


        Stimulus.list().each {
            if (it.targetStimulusModality) {
                StimulusModality targetStimulusModality = StimulusModality.findByUrl(it.targetStimulusModality)
                it.targetModality = targetStimulusModality
            }
            if (it.primeStimulusModality) {
                StimulusModality primeStimulusModality = StimulusModality.findByUrl(it.primeStimulusModality)
                it.primeModality = primeStimulusModality
            }
        }
    }

    def addStimulusQuality(Stimulus it, String s) {
        if (!s.toString().startsWith("http") && it?.targetStimulusQuality?.contains("http")) {
            s = it.targetStimulusQuality.substring(it.targetStimulusQuality.indexOf("http"))?.trim()
        }

        if (s.toString().startsWith("http") && s.toString().contains("#NEMO")) {
            // an array list of
//                    Language language = fixNameWithUrl(Language.findOrSaveByUrl(s))
            StimulusQuality stimulusQuality = fixNameWithUrl(StimulusQuality.findOrSaveByUrl(s))
            it.addToTargetQualities(stimulusQuality)

            stimulusQuality.addToStimuli(it)
            stimulusQuality.save()
        }

    }

    def stubStimulusQuality() {

//        http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000429,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000367,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000441,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000442,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000443,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000444,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000845,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0907000,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0956000,http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6498000
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrls([
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000429",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000367",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000441",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000442",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000443",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000444",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000845",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0907000",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0956000",
                "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6498000"
        ])
        for (OWLClass subClass : subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(StimulusQuality.findOrSaveByUrl(subClass.toStringID()))
            }
        }


        Stimulus.list().each {
            if (it.targetStimulusQuality) {
                Yaml yaml = new Yaml()
                Iterable<Object> objects = yaml.loadAll(it.targetStimulusQuality)
                for (obj in objects) {
                    if (obj instanceof Collection) {
                        for (s in obj) {
                            addStimulusQuality(it, s.toString())
                        }
                    } else {
                        addStimulusQuality(it, obj.toString())
                    }
                }
//                StimulusQuality targetStimulusQuality= StimulusQuality.findByUrl(it.targetStimulusQuality)
//                it.targetQuality = targetStimulusQuality
            }
            if (it.primeStimulusQuality) {
                StimulusQuality primeStimulusQuality = StimulusQuality.findByUrl(it.primeStimulusQuality)
                it.primeQuality = primeStimulusQuality
            }
        }
    }


    def stubResponseDevice() {
        fixNameWithUrl(ResponseDevice.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8525000"))
        fixNameWithUrl(ResponseDevice.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3866000"))
        fixNameWithUrl(ResponseDevice.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7145000"))

        Response.list().each {
            if (it.responseDevice) {
                it.device = ResponseDevice.findByUrl(it.responseDevice)
            }
        }
    }

    def stubResponseModality() {
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7214000"))
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6754000"))
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9993000"))
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8772000"))
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2386000"))
        fixNameWithUrl(ResponseModality.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9738000"))

        Response.list().each {
            if (it.responseModality) {
                it.modality = ResponseModality.findByUrl(it.responseModality)
            }
        }
    }

    def stubResponseRole() {

        // TODO subclasses of     - http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000469
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000469")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(ResponseRole.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(ResponseRole.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000408"))
//        fixNameWithUrl(ResponseRole.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000407"))
//        fixNameWithUrl(ResponseRole.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000404"))
//        fixNameWithUrl(ResponseRole.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4306000"))
//        fixNameWithUrl(ResponseRole.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8201000"))

        Response.list().each {
            if (it.responseRole) {
                it.role = ResponseRole.findByUrl(it.responseRole)
            }
        }
    }

    def stubResponseSoftware() {
        Response.list().each {
            if (it.experimentControlSoftware) {
                it.software = Software.findByUrl(it.experimentControlSoftware)
            }
        }
    }

    def stubEegSoftware() {
        EegDataCollection.list().each {
            if (it.eegDataCollectionSoftware) {
                it.software = Software.findByUrl(it.eegDataCollectionSoftware)
            }
        }
    }

    def stubElectrodeArrayManufacturer() {

        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3195000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8542000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2696000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6500000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1926000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3084000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0506000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6746000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8151000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1543000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7577000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1999000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3045000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6023000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1619000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3920000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0646000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2379000"))
        fixNameWithUrl(ElectrodeArrayManufacturer.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8603000"))

        EegDataCollection.list().each {
            if (it.electrodeArrayManufacturer) {
                it.manufacturer = ElectrodeArrayManufacturer.findByUrl(it.electrodeArrayManufacturer)
            }
        }
    }

    def stubElectrodeArrayLayout() {

//        TODO should subclasses of http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6227000
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6227000")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(edu.uoregon.nic.nemo.portal.ElectrodeArrayLayout.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(ElectrodeArrayLayout.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6584000"))
//        fixNameWithUrl(ElectrodeArrayLayout.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7075000"))
//        fixNameWithUrl(ElectrodeArrayLayout.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8351000"))
//        fixNameWithUrl(ElectrodeArrayLayout.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2925000"))
//        fixNameWithUrl(ElectrodeArrayLayout.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3280000"))


        EegDataCollection.list().each {
            if (it.sensorNetMontage) {
                it.electrodeArrayLayout = ElectrodeArrayLayout.findByUrl(it.sensorNetMontage)
            }
        }
    }

    def stubGroundElectrode() {
        fixNameWithUrl(GroundElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2064000"))
        fixNameWithUrl(GroundElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3904000"))
        fixNameWithUrl(GroundElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3561000"))
        fixNameWithUrl(GroundElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2343000"))
        fixNameWithUrl(GroundElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7907000"))

        EegDataCollection.list().each {
            if (it.groundElectrode) {
                it.ground = GroundElectrode.findByUrl(it.groundElectrode)
            }
        }
    }

    def stubReferenceElectrode() {

        // TODO should be subclass of http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2400000
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2400000")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(edu.uoregon.nic.nemo.portal.ReferenceElectrode.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(ReferenceElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2064000"))
//        fixNameWithUrl(ReferenceElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3094000"))
//        fixNameWithUrl(ReferenceElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6914000"))
//        fixNameWithUrl(ReferenceElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8218000"))
//        fixNameWithUrl(ReferenceElectrode.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3160000"))

        EegDataCollection.list().each {
            if (it.referenceElectrode) {
                it.reference = ReferenceElectrode.findByUrl(it.referenceElectrode)
            }
        }
    }

    def stubErpEvent() {
        fixNameWithUrl(ErpEvent.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4762000"))
        fixNameWithUrl(ErpEvent.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5167000"))

        ErpDataPreprocessing.list().each {
            if (it.erpEvent) {
                it.event = ErpEvent.findByUrl(it.erpEvent)
            }
        }
    }

    def stubOfflineReference() {

        // TODO subclass of http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2400000
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2400000")
        for (OWLClass subClass in subClassList) {
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(edu.uoregon.nic.nemo.portal.OfflineReference.findOrSaveByUrl(subClass.toStringID()))
            }
        }

//        fixNameWithUrl(OfflineReference.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6914000"))
//        fixNameWithUrl(OfflineReference.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8218000"))
//        fixNameWithUrl(OfflineReference.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_6101000"))

        ErpDataPreprocessing.list().each {
            if (it.electrophysiologicalDataReference) {
                it.reference = OfflineReference.findByUrl(it.electrophysiologicalDataReference)
            }
        }
    }

    def stubDataCleaningReferences() {
        fixNameWithUrl(CleaningTransformation.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9037000"))
        fixNameWithUrl(CleaningTransformation.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8468000"))
        fixNameWithUrl(CleaningTransformation.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_5960000"))
        fixNameWithUrl(CleaningTransformation.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2296000"))

        ErpDataPreprocessing.list().each {
            Yaml yaml = new Yaml()
            if (it.dataCleaningTransformation) {
                Iterable<Object> objects = yaml.loadAll(it.dataCleaningTransformation)
                for (obj in objects) {
                    if (obj instanceof Collection) {
                        for (s in obj) {
                            CleaningTransformation cleaningTransformation = fixNameWithUrl(CleaningTransformation.findOrSaveByUrl(s))
                            it.addToCleaningTransformations(cleaningTransformation)
//                        addStimulusQuality(it, s.toString())
                        }
                    } else {
                        CleaningTransformation cleaningTransformation = fixNameWithUrl(CleaningTransformation.findOrSaveByUrl(obj.toString()))
                        it.addToCleaningTransformations(cleaningTransformation)
//                    addStimulusQuality(it, obj.toString())
                    }
                }
            }
//            if(it.electrophysiologicalDataReference){
//                it.reference = OfflineReference.findByUrl(it.electrophysiologicalDataReference)
//            }
        }
    }

    def stubUsers() {
        Role adminRole = Role.findOrSaveByAuthority("ROLE_ADMIN")

        for (SecUser user in SecUser.list()) {
//            if(user.password=='abc123'){
            user.password = 'ilikebrains'
//            }
            if (user.username in adminUsers) {
                SecUserRole.findOrSaveBySecUserAndRole(user, adminRole)
            }
        }
    }

    /**
     * For each experiment, get the datafiles, if one ERP per bunch, assign that recipricollay
     */
    void createErpPatternExtractions() {
        DataFormat dataFormat = DataFormat.findByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2241000")

        List<Experiment> experiments = Experiment.all
        experiments.each { experiment ->
//            List<DataFile> dataFileList = DataFile.findAllByFormatAndExperiment(dataFormat, experiment)
//            ErpDataPreprocessing erpDataPreprocessing = null
//            if (experiment.erpDataPreprocessings.size() == 1) {
//                erpDataPreprocessing = experiment.erpDataPreprocessings.iterator().next()
//            }
//
//
//            dataFileList.each { it ->
//                new ErpPatternExtraction(
//                        artifactFileName: it.artifactFileName
//                        , download: it.download
//                        , experiment: experiment
//                        , erpDataPreprocessing: erpDataPreprocessing
//                        , format: dataFormat
//                ).save(insert: true, failOnError: true)
//                it.experiment.erpPatternExtractionNarrativeSummary = it.experiment.dataFilesNarrativeSummary
//            }
        }


    }

    /**
     * If one ErpPattern Extraction, assign allt o that one
     */
    void createErpAnalysisResults() {
        DataFormat dataFormat = DataFormat.findByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8913000")
        DataSet dataSet = DataSet.findByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_3872000")

        List<Experiment> experiments = Experiment.all

        experiments.each { experiment ->
//            List<DataFile> dataFileList = DataFile.findAllByFormatAndExperiment(dataFormat, experiment)
//            ErpPatternExtraction erpPatternExtraction = null
//            if (experiment.erpPatternExtractions.size() == 1) {
//                erpPatternExtraction = experiment.erpPatternExtractions.iterator().next()
//            }
//
//            dataFileList.each { it ->
//                ErpAnalysisResult erpAnalysisResult = new ErpAnalysisResult(
//                        artifactFileName: it.artifactFileName ?: it.identifier
//                        , format: dataFormat
//                        , set: dataSet
//                        , rdfContent: it.rdfContent
//                        , inferredOntology: it.inferredOntology
//                        , processing: it.processing
//                        , startClassification: it.startClassification
//                        , endClassification: it.endClassification
//                        , experiment: experiment
//                        , erpPatternExpression: erpPatternExtraction
//                ).save(insert: true, failOnError: true)
//                it.experiment.erpAnalysisResultNarrativeSummary = it.experiment.dataFilesNarrativeSummary
//
//                // convert patternImages:
//                PatternImage.findAllByDataFile(it).each { PatternImage patternImage ->
//                    patternImage.erpAnalysisResult = erpAnalysisResult
//                    patternImage.save()
//                }
//            }
        }
    }

    def clearAllStatisticalTestAndStatisticalThreshold() {

        ErpAnalysisResult.all.each { ErpAnalysisResult erpAnalysisResult ->
            erpAnalysisResult.thresholdQuality = null
            erpAnalysisResult.analysisMethod = null
            // these appear to be set properly
//            erpAnalysisResult.dependentVariable = null
//            erpAnalysisResult.independentVariable = null
            erpAnalysisResult.save(failOnError: true, flush: true)
        }

    }

    def createErpExtractionMethod(String s) {
        Set<OWLClass> subClassList = ontologyService.getChildrenForUrl(s)
        log.debug "analysis subclsses: " + subClassList.size()
        for (OWLClass subClass in subClassList) {
            log.debug "subclass url: " + subClass.toStringID()
            if (!subClass.toStringID().contains("Nothing")) {
                fixNameWithUrl(PatternExtractionMethod.findOrSaveByUrl(subClass.toStringID()))
            }
        }
    }


    def addErpExtractionMethods() {
        createErpExtractionMethod("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000512")
        createErpExtractionMethod("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000513")
        fixNameWithUrl(PatternExtractionMethod.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000512"))
        fixNameWithUrl(PatternExtractionMethod.findOrSaveByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000513"))
    }

    def createPreSfnTables() {
        clearAllStatisticalTestAndStatisticalThreshold()
        stubThresholdQuality()
        stubAnalysisMethod()
        stubAnalysisVariables()
        addErpExtractionMethods()
    }
}

