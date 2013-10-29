package edu.uoregon.nic.nemo.portal

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.semanticweb.HermiT.Reasoner
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLOntology

import static org.junit.Assert.*
import org.junit.Ignore
import org.semanticweb.owlapi.reasoner.OWLReasoner

class OntologyServiceTests {

    def ontologyService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void getOntology() {
        OWLOntology gotOntology = ontologyService.getOntology()
        assertNotNull(gotOntology)
        gotOntology = ontologyService.getOntology()
        assertNotNull(gotOntology)
        assertTrue(gotOntology.axiomCount > 100)
    }

//    @Test
//    void loadRdfDataForDatafile() {
//        def experiment = Experiment.findAllByCachedSlugLike("gaf-lp1").iterator().next()
//        for (dataFile in experiment.dataFiles) {
//            dataFile.rdfContent = null
//            assertNull(dataFile.rdfContent)
//            if (dataFile.isRdf()) {
//                assertTrue(ontologyService.loadRdfDataForDataFile(dataFile))
//                assertNotNull(dataFile.rdfContent)
//                assertFalse(ontologyService.loadRdfDataForDataFile(dataFile))
//                assertNotNull(dataFile.rdfContent)
//            }
//            else {
//                assertFalse(ontologyService.loadRdfDataForDataFile(dataFile))
//            }
//        }
//    }

    @Test
    void getLabelForUrl(){
//        Ontology.findByName("Global Ontology").delete(flush: true)
        String label = ontologyService.getLabelForUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2938000")
        println "label: " + label
        assertNotNull(label)
    }

//    @Test
//    void loadDataFilesForExperiment() {
//        def experiment = Experiment.findAllByCachedSlugLike("gaf-lp%").iterator().next()
//        ontologyService.loadDataFilesForExperiment(experiment)
//    }

    @Ignore("Takes a long time")
    public void inferInstanceTest(){
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.findByArtifactFileName('GAF-LP2_LexicalityEffect_Subgroup2.rdf')

        ErpAnalysisResult analysisResult = new ErpAnalysisResult(
                experiment: Experiment.list().get(0)
                ,identifier: "testDF"
                ,set: erpAnalysisResult.set
                ,format: erpAnalysisResult.format
//                ,state:  dataFileToCopy.state
                ,artifactFileName: "testFilename.rdf"
//                ,artifactContentType: dataFileToCopy.artifactContentType
//                ,instances: dataFileToCopy.instances
                ,artifactFileSize: erpAnalysisResult.artifactFileSize
//                ,download: dataFileToCopy.download
                ,rdfContent: erpAnalysisResult.rdfContent
                ,inferredOntology: null
        ).save(flush: true,validate: true,failOnError: true)

        // TOD: find a quicker version of this test
        ontologyService.inferRdfInstance(analysisResult)

    }

    @Ignore("Takes a long time")
    public void inferAsynchronousInstanceTest(){
        println 'running asyncrhouns test'
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.findByArtifactFileName('GAF-LP2_LexicalityEffect_Subgroup2.rdf')

        long startTime = System.currentTimeMillis()
        ErpAnalysisResult result = new ErpAnalysisResult(
                experiment: Experiment.list().get(0)
                ,identifier: "testDF"
                ,set: erpAnalysisResult.set
                ,format: erpAnalysisResult.format
//                ,state:  dataFileToCopy.state
                ,artifactFileName: "testFilename.rdf"
//                ,artifactContentType: dataFileToCopy.artifactContentType
//                ,instances: dataFileToCopy.instances
                ,artifactFileSize: erpAnalysisResult.artifactFileSize
//                ,download: dataFileToCopy.download
                ,rdfContent: erpAnalysisResult.rdfContent
                ,inferredOntology: null
        ).save(flush: true,validate: true,failOnError: true)


        ontologyService.inferRdfInstanceAsync(result)

        while(result.processing){
            println 'processing . . . ' + ((System.currentTimeMillis()-startTime)/1000f/60f) + ' minutes'
            sleep(60000)
        }

        println 'finished'

    }

    @Ignore("Takes a long time")
    void inferInstance() {
        def experiment = Experiment.findAllByIdentifierLike("GAF-LP1").iterator().next()
        for (dataFile in experiment.dataFiles) {
            if (dataFile.isRdf()) {
//                if (dataFile.rdfContent == null) {
//                    println "is null "
//                    assertNull(dataFile.rdfContent)
//                    assertTrue(ontologyService.loadRdfDataForDataFile(dataFile))
//                }
//                else {
//                    println "rdf content was not null"
//                }
                println "should not be null now null "
                assertNotNull(dataFile.rdfContent)
                if (dataFile.isRdf()) {
                    dataFile.inferredOntology = null
                    assertTrue(ontologyService.inferRdfInstance(dataFile))
                    assertNotNull(dataFile.inferredOntology)
                    OWLClass parentClass = ontologyService.getOwlClass("8913000")
                    assertNotNull(parentClass)
                    println "parent: " + parentClass

                    // rehydrate ontology
                    OWLOntology dataFileInferredOwlOntology = ontologyService.getOntologyFromDataFile(dataFile)
                    assertNotNull(dataFileInferredOwlOntology)

                    Reasoner reasoner = new Reasoner(dataFileInferredOwlOntology)
//                    println dataFile.dataFileReasoner.getSubClasses(parentClass,false).flattened
                    println reasoner.getSubClasses(parentClass,false).flattened
                    println reasoner.getInstances(parentClass,false).flattened
//                    assertTrue(dataFile.dataFileReasoner.getSubClasses(ontologyService.getOwlClass("8913000"),false).flattened.size()>10)
                    assertFalse(ontologyService.inferRdfInstance(dataFile))
                }
                else {
                    assertFalse(ontologyService.inferRdfInstance(dataFile))
                }
            }
        }
    }

    @Test
    void testSomething() {
        OWLClass owlClass = ontologyService.getNameForClass("NEMO_8980000");
        println owlClass
        Map map = owlClass.getProperties();
        Iterator iter = map.keySet().iterator();
//        owlClass.get
        while (iter.hasNext()) {
            def key = iter.next()
            println key + " - " + map.get(key)
        }
    }

    @Test
    void inferQuery2MfnLexicalityEffect(){
        OWLOntology gotOntology = ontologyService.getOntology()
        OWLReasoner reasoner = ontologyService.createReasoner(gotOntology)
    }
}
