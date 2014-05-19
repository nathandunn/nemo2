import edu.uoregon.nemo.nic.portal.util.ProcessingStatus
import edu.uoregon.nic.nemo.portal.*
import grails.util.Environment
import org.apache.commons.io.IOUtils
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.uoregon.nemo.nic.QueryListEnum

class BootStrap {


    def ontologyService
    def dataStubService
    def searchService

    def init = { servletContext ->


        ontologyService.getOntology()

//        OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
//        ErpAnalysisResult testErpResult = ErpAnalysisResult.findByArtifactFileNameLike("GAF-LP1_LexicalityEffect_AllSubj.rdf")
//        OWLOntology localOntology= manager.loadOntologyFromOntologyDocument(IOUtils.toInputStream(testErpResult.rdfContent, "UTF-8"))
//        OWLReasoner reasoner= ontologyService.createReasoner(localOntology)
//
//        // TODO: should use Cached reasoner
////        OWLOntology localOntology = ontologyService.getOntologyFromErpAnalysisResult(testErpResult, manager)
////        OWLReasoner reasoner = ontologyService.getReasonerForErpAnalysisResult(testErpResult)
//
//        OWLDataFactory owlDataFactory = manager.getOWLDataFactory()
//        OWLClass meanIntensityMeasurementClass = owlDataFactory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.MEAN_INTENSITY_MEASUREMENT_DATUM.url))
//
//        Set<OWLNamedIndividual> meanIntensityIndividuals = reasoner.getInstances(meanIntensityMeasurementClass, false).flattened
//        println "# of mean intensiy individuals  ${meanIntensityIndividuals.size()}"
////        <http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_7943000>
//
//        IRI propname = IRI.create(OntologyService.NS + QueryListEnum.HAS_NUMERIC_VALUE.url)
//        OWLDataProperty owlDataProperty = owlDataFactory.getOWLDataProperty(propname)
//
//        OWLClass hasNumericValueParent = owlDataFactory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.HAS_NUMERIC_VALUE.url))
//        Set<OWLNamedIndividual> numericValuesSet = reasoner.getInstances(hasNumericValueParent, false).flattened
//        println "# of numeric values ${numericValuesSet.size()}"
//
////        OWLClassExpression owlClassExpression = owlDataFactory.getOWLDataHasValue(owlDataProperty,)
//
//        for (OWLNamedIndividual owlNamedIndividual in meanIntensityIndividuals) {
//            println "this individual: ${owlNamedIndividual.toString()}"
//
//            for(OWLLiteral literal in reasoner.getDataPropertyValues(owlNamedIndividual,owlDataProperty)){
//                println "reasoner literal ${literal.literal}"
//            }
//
//        }
//
//        System.exit(1)


        switch (Environment.current) {
            case Environment.PRODUCTION:
            case Environment.CUSTOM:
                ontologyService.putReasonersIntoMemory()
                searchService.cacheSearch()
                break
            case Environment.DEVELOPMENT:
                ontologyService.putReasonersIntoMemory()
                searchService.cacheSearch()
//                ontologyService.getErpsFromErpAnalysisResults(InstancesEnum.NEMO_3056000.name())
                break
            case Environment.TEST:
                ontologyService.putReasonersIntoMemory()
                searchService.cacheSearch()
                break
            default:
                throw new RuntimeException("Not sure how we got here")
        }

        //stubNewTables()
        preSfnTables()

        // should already be in the dump
//        String bootstrapValue = System.properties["bootstrap"]
//        log.debug "bootstrapValue: " + bootstrapValue
//        if (bootstrapValue) {
//            dataStubService.bootstrapData()
////            return
//        }

        // TODO: we need check all of the datafiles in case of restart if an rdf . . set ProcessingStatus explicitly:
        DataFormat rdfDataFormat = DataFormat.findByUrl(OntologyService.RDF_NEMO_URL)

        int setToNull = 0
        for (ErpAnalysisResult erpAnalysisResult : ErpAnalysisResult.findAllByRdfContentIsNotNullAndProcessingAndInferredOntologyIsNotNullAndFormat(ProcessingStatus.IN_PROCESS.value(), rdfDataFormat)) {
            erpAnalysisResult.inferredOntology = null
            erpAnalysisResult.save(flush: true)
            ++setToNull
        }
        log.info "ontologies setting to null ${setToNull}"

        // 2 - has rdfdata but no inferredOntology . . . need to set state to 0 and then begin inferring again
        int ontologyServiceCount = 0
        for (ErpAnalysisResult erpAnalysisResult : ErpAnalysisResult.findAllByRdfContentIsNotNullAndInferredOntologyIsNull()) {
            println "re-inferring: " + erpAnalysisResult.artifactFileName
            ++ontologyServiceCount
            erpAnalysisResult.setInProcess()
            ontologyService.inferRdfInstanceAsync(erpAnalysisResult, 'ndunn@me.com')
            println "re-inferring launched: " + erpAnalysisResult.artifactFileName
        }
        println "ontologies re-inferring ${ontologyServiceCount}"

        // has rdfdata and inferredOntology, but state is not 0 -> set 0
//        println "file set to 0 ${fileStatusSetToZero}"
        // 3 - has no rdfdata -> set inferredOntology null and processing to 3
        int noRdfStatusUpdates = ErpAnalysisResult.executeUpdate(" update ErpAnalysisResult df set processing = :processing , inferredOntology = null where rdfContent is null and format = :rdf_format and processing != :processing", ["processing": ProcessingStatus.UNPROCESSED.value(), "rdf_format": rdfDataFormat])
        println "non-rdf status updates ${noRdfStatusUpdates}"

//        for (experiment in Experiment.all) {
//            ontologyService.loadDataFilesForExperiment(experiment)
//        }

        // this should already be in the dump
//        Publication.list().each { publication ->
//            SecUser secUser = null
//            if (!publication.authors) {
//                switch (publication.familyName) {
//                    case "Frishkoff":
//                        secUser = SecUser.findByFullNameLike("Gwen Frishkoff")
//                        break
//                    case "Curran":
//                        secUser = SecUser.findByFullNameLike("Tim Frishkoff")
//                        break
////                    case "Lai":
////                        break
//                    default:
//                        secUser = null
//                }
//                if (secUser) {
//                    publication.addToAuthors(secUser)
//                    secUser.addToPublications(publication)
//                }
//            }
//        }

    }

    def preSfnTables() {

        if(!Patch.findByName("Pre-SFN-Tables")){
            dataStubService.createPreSfnTables()
            Patch patch = new Patch(name: "Pre-SFN-Tables",dateApplied: new Date()).save(insert:true,flush:true,failOnError: true)
        }

        //To change body of created methods use File | Settings | File Templates.
    }

    def stubNewTables() {
        // START stub
        println "stubbing new tables"
        dataStubService.bootstrapNewTables()
        println "done new tables"
        if (ErpPatternExtraction.count() == 0) {
            println "extracting ERP patterns"
            dataStubService.createErpPatternExtractions()
        }

        if (ErpAnalysisResult.count() == 0) {
            dataStubService.createErpAnalysisResults()
        }

        Laboratory.all.each { Laboratory lab ->
            println "fixing lab ${lab.identifier}"
            String piUrl = lab.principalInvestigatorRole
            if (piUrl?.startsWith("http://purl")) {
                lab.principalInvestigatorRole = piUrl.split("#")[1].replaceAll("_", " ")
            }

            String institutionUrl = lab.institution
            if (institutionUrl?.startsWith("http://purl")) {
                String institutionLabel = ontologyService.getLabelForUrl(institutionUrl)?.replaceAll("_", " ")
                lab.institution = institutionLabel
            }
        }
        // END stub
    }

    def destroy = {
    }

}
