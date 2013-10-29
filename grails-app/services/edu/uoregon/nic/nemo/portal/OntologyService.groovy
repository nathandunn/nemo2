package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.InstancesEnum
import edu.uoregon.nemo.nic.portal.util.TermView
import groovy.time.TimeCategory
import org.apache.commons.io.IOUtils
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.InferenceType
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory
import org.semanticweb.owlapi.util.*
import org.uoregon.nemo.nic.QueryListEnum
import org.yaml.snakeyaml.Yaml

import javax.servlet.ServletContext

//require(url:'http://jyaml.sourceforge.net', jar:'jyaml.jar', version:'1.0')
class OntologyService {

    def nemoMailService

    final static String NS_BASE = "http://purl.bioontology.org/NEMO/ontology"
    final static String NS = NS_BASE + "/NEMO.owl"
    final static String RDF_NEMO_URL = "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8913000"
    final static String INSTITUTION_NEMO_URL = "http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8064000"
    final static DATA_URL = "http://purl.bioontology.org/NEMO/data/"

    // Example: "http://www.w3.org/2001/XMLSchema#string2010-04-24
    final static String schemaDateString = "http://www.w3.org/2001/XMLSchema#string"
    String dateParseString = "yyyy-MM-dd"

    static String webInfFilesDir = "/WEB-INF/files/"
    static String ontologiesDir = "ontology/"
    static String rdfDir = "rdf/"
    static String globalOntology = "Global Ontology"

    Map<String, OWLReasoner> cachedReasoners = new HashMap<String, OWLReasoner>()

    OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager()
    OWLDataFactory factory = owlOntologyManager.getOWLDataFactory()

    IRI localNemoIri = null
    OWLOntology nemoOntology = null
    OWLReasoner nemoReasoner = null
    Yaml yaml = new Yaml()
    OWLAnnotationProperty rdfsLabel = factory.getRDFSLabel()

    Boolean useLocal = true

    OntologyService() {
        String filename = getLocalNemoOntologyFile()
        log.debug "nemo ontology file: + " + filename
        if (useLocal) {
            localNemoIri = IRI.create(new File(filename))
        } else {
            localNemoIri = IRI.create(NS)
        }
        createMappers(owlOntologyManager)
    }

    def getLocalWebInfDirectory() {
        ServletContext servletContext = ServletContextHolder.servletContext
        if (servletContext == null) {
            return "./web-app" + webInfFilesDir
        } else {
//            String path = (servletContext.getRealPath(webInfFilesDir) ?: "./web-app/WEB-INF/files/") + "/"
            String path = (servletContext.getRealPath(webInfFilesDir) ?: "./web-app/WEB-INF/files/") + "/"
            log.info "loading ontologies with path: ${path}"
            return path
        }
    }


    def getLocalOntologiesDirectory() {
        getLocalWebInfDirectory() + ontologiesDir
    }

//    def getLocalRdfDirectory() {
//        getLocalWebInfDirectory() + rdfDir
//    }

    def getLocalNemoOntologyFile() {
        getLocalOntologiesDirectory() + "NEMO.owl"
    }

    def createMappers(OWLOntologyManager ontologyManager) {
//        String badDir = "scripts/baddir"
        if (useLocal) {
            ServletContext servletContext = ServletContextHolder.servletContext
//            String ontologiesDirPath = servletContext!=null ? servletContext.getRealPath(ontologiesDir) : "./web-app/"+ontologiesDir
            String ontologiesDirPath = getLocalOntologiesDirectory()
            log.debug "ontologiesDirPath = ${ontologiesDirPath}"
//            String ontologiesDirPath = servletContext.getRealPath(ontologiesDir)
            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://purl.bioontology.org/NEMO/ontology/NEMO.owl"), IRI.create(new File(ontologiesDirPath + "/NEMO.owl"))))
            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://purl.bioontology.org/NEMO/ontology/NEMO_annotation_properties.owl"), IRI.create(new File(ontologiesDirPath + "/NEMO_annotation_properties.owl"))))
            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://purl.bioontology.org/NEMO/ontology/NEMO_deprecated.owl"), IRI.create(new File(ontologiesDirPath + "/NEMO_deprecated.owl"))))
            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.ifomis.org/bfo/1.1"), IRI.create(new File(ontologiesDirPath + "/bfo-1.1.owl"))))

//            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://purl.obolibrary.org/obo/obi.owl"), IRI.create(new File(ontologiesDirPath + "/obi.owl"))))
//            ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://ecoinformatics.org/oboe/oboe.1.0/oboe.owl"), IRI.create(new File(ontologiesDirPath + "/oboe_v1.0.owl"))))
        }

    }

    OWLClass getOwlClass(String lookupString) {
        factory.getOWLClass(createIRIFromString(lookupString))
    }

    IRI createIRIFromString(String s) {
        IRI.create(createFullURI(s))
    }

    String createFullURI(String s) {
        String lookup = ""
        if (!s.startsWith(NS)) {
            lookup = NS
        }
        if (!s.contains("#")) {
            lookup += "#"
        }
        if (!s.contains("NEMO_")) {
            lookup += "NEMO_"
        }
        lookup += s
        return lookup
    }


    OWLOntology getOntology() {
//        def ontologies = owlOntologyManager.getOntologies()
//        log.debug ontologies.size()
//        log.debug "empty nemo ontology: " + nemoOntology
        if (nemoOntology == null) {
            if (owlOntologyManager.getOntology(localNemoIri)) {
//                if (owlOntologyManager.getOntology(owlIRI)) {
//                nemoOntology = owlOntologyManager.getOntology(owlIRI)
                nemoOntology = owlOntologyManager.getOntology(localNemoIri)
                return nemoOntology
            }
            // we need a new manager!
//            owlOntologyManager = null
//            owlOntologyManager = OWLManager.createOWLOntologyManager()
            Ontology globalOntology = Ontology.findByName(globalOntology)
//            Ontology globalOntology = null
//            log.debug "Global Ontology: " + globalOntology
            createMappers(owlOntologyManager)
//            log.debug "Loading Ontology: " + globalOntology
            owlOntologyManager.setSilentMissingImportsHandling(false)
            nemoOntology = owlOntologyManager.loadOntologyFromOntologyDocument(localNemoIri)
//            log.debug "LOADED Ontolgy: " + globalOntology
            nemoReasoner = getGlobalReasoner(nemoOntology)
//            log.debug "Got reasoner: " + globalOntology

            if (globalOntology) {
                OWLOntology inferredOntology = owlOntologyManager.loadOntologyFromOntologyDocument(new ByteArrayInputStream(globalOntology.xmlContent.bytes))

//                log.debug "PRE: " + nemoOntology.toString()
                listImports()

                Set<OWLAxiom> axioms = inferredOntology.getAxioms()
                for (OWLAxiom axiom in axioms) {
                    AddAxiom addAxiom = new AddAxiom(nemoOntology, axiom)
                    owlOntologyManager.applyChange(addAxiom)
                }

//                log.debug "POST: " + nemoOntology.toString()
                listImports()
            } else {
//                log.debug "no global ontology . . . creating"
                log.debug nemoOntology.toString()

                OWLClass parent = factory.getOWLClass(IRI.create(OntologyService.NS + "#NEMO_0877000"))
                Set<OWLClass> subClassList = nemoReasoner.getSubClasses(parent, false).flattened

                List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>()
                gens.add(new InferredSubClassAxiomGenerator())
                gens.add(new InferredClassAssertionAxiomGenerator());
                gens.add(new InferredEquivalentClassAxiomGenerator());
                gens.add(new InferredDataPropertyCharacteristicAxiomGenerator());
                gens.add(new InferredSubDataPropertyAxiomGenerator());
                gens.add(new InferredPropertyAssertionGenerator());

                OWLOntology infOnt = owlOntologyManager.createOntology()
                InferredOntologyGenerator iog = new InferredOntologyGenerator(nemoReasoner, gens)
                iog.fillOntology(owlOntologyManager, infOnt);


                OutputStream outputStream = new ByteArrayOutputStream()

                owlOntologyManager.saveOntology(infOnt, outputStream)
                String string = IOUtils.toString(outputStream.toByteArray(), "UTF-8")

                Ontology ontology = new Ontology(
                        name: globalOntology
                        , description: "init load"
                        , xmlContent: string
                ).save(validate: true, flush: true)
                listImports()
            }
        }

        return nemoOntology
    }

    def listImports() {
        log.debug "# of imports: " + nemoOntology.getImports()
        for (OWLOntology imported : nemoOntology.getImports()) {
            log.debug(imported.toString() + " came from " + owlOntologyManager.getOntologyDocumentIRI(imported).toString());
        }

    }

    OWLReasoner createReasoner(OWLOntology owlOntology) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String reasonerFactoryName;
        // reasonerFactoryName = "uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory";
        reasonerFactoryName = "org.semanticweb.HermiT.Reasoner\$ReasonerFactory";
//            log.debug "creating nemoReasoner: " + reasonerFactoryName
        // reasonerFactoryName = "au.csiro.snorocket.owlapi3.SnorocketReasonerFactory";
        // reasonerFactoryName = "com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory";
        OWLReasonerFactory reasonerFactory = (OWLReasonerFactory) Class.forName(reasonerFactoryName).newInstance();
//        log.debug "Getting nemoReasoner for: " + owlOntology
        OWLReasoner reasoner = reasonerFactory.createReasoner(owlOntology);
        return reasoner;
    }

    OWLReasoner getGlobalReasoner(OWLOntology owlOntology) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (nemoReasoner == null) {
            nemoReasoner = createReasoner(owlOntology)
//            nemoReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY)
        }
        return nemoReasoner;
    }


    String render(OWLEntity e) {
        StringBuffer sb = new StringBuffer();
        boolean foundLabel = addLabel(e, sb);
        sb.append(e.getIRI().toString());
        if (foundLabel) {
            sb.append(')');
            log.debug "render found: " + foundLabel
        } else {
            log.debug "render NOT found: " + foundLabel
        }

        return sb.toString();
    }

    private boolean addLabel(OWLEntity e, StringBuffer sb) {
        for (OWLOntology ont : ontology.getImportsClosure()) {
            Set<OWLAnnotation> annotations = e.getAnnotations(ont, rdfsLabel)
//            log.debug "other annotaions size: " + annotations.size()
            for (OWLAnnotation annotation : annotations) {
                OWLAnnotationValue value = annotation.getValue()
                if (value instanceof OWLLiteral) {
                    sb.append(((OWLLiteral) value).getLiteral())
                    sb.append(" (")
                    return true;
                }
            }
        }
        return false;
    }

/**
 * Should be of the form: http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0000393
 * @param inputClassName
 * @return
 */
    List cleanClassName(String inputClassName) {
        String parseString = "NEMO.owl#"
        if (!inputClassName.contains(parseString)) {
            return [(NS + "#" + inputClassName).trim()]
        } else {
            def returnList = []
            for (unfilteredClassName in inputClassName.split("\n")) {
                for (className in unfilteredClassName.split(parseString)) {
                    returnList.add((NS + "#" + className).trim())
                }
            }
            return returnList
        }
    }

    String getLabelTextForYamlUrls(String className, String delimiter = " ") {
        if (!className) return null

        def classNames = yaml.load(className)

        String returnString = ""

        def size = classNames.size()
        classNames.eachWithIndex { classNameInstance, i ->
//            for (classNameInstance in classNames) {
            def label = getLabelForUrl(classNameInstance)
            if (label) {
                returnString += label + (i < size - 1 ? delimiter : "")
            }
        }
        return returnString
    }

    String getLabelForUrl(String classNameInstance) {
        classNameInstance = createFullURI(classNameInstance)
        OWLDataFactory dataFactory = getOntology().getOWLOntologyManager().getOWLDataFactory()
        OWLClass parent = dataFactory.getOWLClass(IRI.create(classNameInstance))
        OWLAnnotationProperty rdfsLabel = dataFactory.getRDFSLabel();
        for (OWLOntology owlOntology1 : getOntology().getImportsClosure()) {
            Set<OWLAnnotation> owlAnnotationSet = parent.getAnnotations(owlOntology1, rdfsLabel)
            for (OWLAnnotation owlAnnotation in owlAnnotationSet) {
                OWLAnnotationValue owlAnnotationValue = owlAnnotation.getValue()
                if (owlAnnotationValue instanceof OWLLiteral) {
                    return ((OWLLiteral) owlAnnotationValue).getLiteral()
                }
            }
        }
        return null
    }

    OWLClass getNameForClass(String className) {
        String reference = !className.contains("#") ? className : className.split("#")[1]
        OWLClass parent = factory.getOWLClass(IRI.create(NS + "#" + reference));

        render(parent)
        return parent;
    }


    def inferRdfInstance(ErpAnalysisResult erpAnalysisResult) {
        return inferRdfInstance(erpAnalysisResult, null)
    }

    def inferRdfInstance(ErpAnalysisResult erpAnalysisResult, String email) {

        log.debug 'ErpAnalysisResult trying to infer the instance: ' + erpAnalysisResult.artifactFileName + ' status - ' + erpAnalysisResult.processing

        if (!erpAnalysisResult.rdfContent || erpAnalysisResult.inferredOntology) {
            nemoMailService.emailError(erpAnalysisResult, email)
            return false
        }


        def future = callAsync {


            log.debug erpAnalysisResult.artifactFileName + ' creating new owl manager '
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            log.debug 'loading ontology from uploaded RDF and is open '
            log.debug 'size: ' + erpAnalysisResult?.rdfContent.size()
            createMappers(manager)
            OWLOntology ontologyCopy = manager.loadOntologyFromOntologyDocument(IOUtils.toInputStream(erpAnalysisResult.rdfContent, "UTF-8"))
//        log.debug "ontologyCopy: " + ontologyCopy
//        secondSession.close()
            log.debug "closed session . . . ready for inferring "

            // this will be fast now!
            // create a separate reasoner
            log.debug "createing reasoner: "
            def timeStart = new Date()
            OWLReasoner inferenceReasoner = createReasoner(ontologyCopy)
//            inferenceReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS)
            inferenceReasoner.precomputeInferences(InferenceType.values())
            def timeStop = new Date()
            log.debug "Reasoner time: " + TimeCategory.minus(timeStop, timeStart)
            log.debug "query 1 start : "
            timeStart = new Date()
            inferQuery1ScalpRecorded(inferenceReasoner)
            timeStop = new Date()
            log.debug "Query 1 time : " + TimeCategory.minus(timeStop, timeStart)
            log.debug "query 2 start : "
            timeStart = new Date()
            inferQuery2MfnLexicalityEffect(inferenceReasoner)
            timeStop = new Date()
            log.debug "Query 2 time : " + TimeCategory.minus(timeStop, timeStart)


            List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>()
            gens.add(new InferredSubClassAxiomGenerator())
            gens.add(new InferredClassAssertionAxiomGenerator());
            gens.add(new InferredEquivalentClassAxiomGenerator());
            gens.add(new InferredDataPropertyCharacteristicAxiomGenerator());
            gens.add(new InferredSubDataPropertyAxiomGenerator());
            gens.add(new InferredPropertyAssertionGenerator());

            OWLOntology inferredOntology = manager.createOntology()
            log.debug "starting to store "
            timeStart = new Date()
            InferredOntologyGenerator iog = new InferredOntologyGenerator(inferenceReasoner, gens)
            iog.fillOntology(manager, inferredOntology);
            timeStop = new Date()
            log.debug "Ontology Store time : " + TimeCategory.minus(timeStop, timeStart)

            OutputStream outputStream = new ByteArrayOutputStream()

            manager.saveOntology(inferredOntology, outputStream)

            log.debug "final ontology COPY: " + ontologyCopy
            log.debug "final INFERRED ontology: " + inferredOntology
            def inferredOntologyXml = IOUtils.toString(outputStream.toByteArray(), "UTF-8")
            log.debug erpAnalysisResult.artifactFileName + ' loaded inferred Ontology into string '


            return inferredOntologyXml

        }

        // will either return false or the appropriate XML string
        def inferenceOutput = future.get()
        if (inferenceOutput) {
            log.debug 'status returned '
            // separate thread, I think so need to retrieve the datafile
            ErpAnalysisResult df2 = ErpAnalysisResult.get(erpAnalysisResult.id)
            log.debug df2.artifactFileName + ' final save - session opened '
//        Transaction transaction = session.beginTransaction()
//        log.debug dataFile.artifactFileName + ' final save - transaction opened '
            // set property for reload
            df2.inferredOntology = inferenceOutput
            log.debug df2.artifactFileName + ' final save - inferred ontology set '
            List<Individual> individuals = Individual.findAllByErpAnalysisResult(df2)
            Individual.deleteAll(individuals)
//            searchService.cacheErpResultIndividuals(df2)
            df2.setDoneProcessing()
            df2.validate()
            if (df2.hasErrors()) {
                log.debug " has errors: " + df2.errors
            } else {
                log.debug " NO errors: " + df2.errors
            }

            try {
                df2.save(flush: true, validate: true, failOnError: true)
                log.info "saved with status DONE: " + df2.processing

                OWLOntology inferredOntology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(IOUtils.toInputStream(df2.inferredOntology, "UTF-8"))

                OWLReasoner reasoner = createReasoner(inferredOntology)
                setReasonerForErpAnalysisResult(df2, reasoner)
            } catch (Exception e) {
                log.error "FAILED to saved with status DONE for some reason : " + e
            }
            finally {
                log.debug "saved with status DONE: " + df2.processing
                erpAnalysisResult.setDoneProcessing()
                nemoMailService.emailComplete(erpAnalysisResult, email)
            }
        } else {
            ErpAnalysisResult df2 = ErpAnalysisResult.get(erpAnalysisResult.id)
            df2.setErrorProcessing()
            df2.save(flush: true, validate: true, failOnError: true)
            log.debug "saved with status ERROR: " + erpAnalysisResult.processing
            nemoMailService.emailError(erpAnalysisResult, email)
        }

        log.debug erpAnalysisResult.artifactFileName + ' final save - end of method'
    }


    def inferQuery2MfnLexicalityEffect(OWLReasoner inferenceReasoner) {
        OWLClass parent = factory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.MFN_LEXICALITY_EFFECT.url))
        Set<OWLNamedIndividual> instances = inferenceReasoner.getInstances(parent, false).flattened
        for (OWLNamedIndividual instance in instances) {
            log.debug "instance: " + instance
        }
        return inferenceReasoner;
    }

    def inferQuery1ScalpRecorded(OWLReasoner inferenceReasoner) {
        OWLClass parent = factory.getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.SCALP_RECORDED_ERP_DIFFWAVE_COMPONENT.url))
        Set<OWLClass> subClassList = inferenceReasoner.getSubClasses(parent, false).flattened
        log.debug "subclasses: " + subClassList
//        Set<OWLNamedIndividual> instances = inferenceReasoner.getInstances(parent, false).flattened;

        for (OWLClass subClass in subClassList) {
            Set<OWLNamedIndividual> instances = inferenceReasoner.getInstances(subClass, false).flattened
            log.debug "instances: " + instances
//           for(OWLClass instance in instances){
//               log.debug "instance: " + instance
//           }
        }
        return inferenceReasoner;
    }

    OWLOntology getOntologyFromErpAnalysisResult(ErpAnalysisResult erpAnalysisResult, OWLOntologyManager owlOntologyManager1 = null) {
        OWLOntologyManager manager = (owlOntologyManager1 ?: OWLManager.createOWLOntologyManager())
        if (erpAnalysisResult.inferredOntology) {
            return manager.loadOntologyFromOntologyDocument(IOUtils.toInputStream(erpAnalysisResult.inferredOntology, "UTF-8"))
        } else {
            inferRdfInstanceAsync(erpAnalysisResult)
            return null
        }
    }

    def inferRdfInstanceAsync(ErpAnalysisResult erpAnalysisResult, String email = null) {
        log.debug 'set in process - inferring RDF dataFile async: ' + erpAnalysisResult.artifactFileName + " status: " + erpAnalysisResult.processing
        log.debug 'closing session - inferring RDF dataFile async: ' + erpAnalysisResult.artifactFileName + " status: " + erpAnalysisResult.processing

        runAsync {
            log.debug "inferring datafile in async: " + erpAnalysisResult.artifactFileName
            inferRdfInstance(erpAnalysisResult, email)
            log.debug "getting reasoner in async: " + erpAnalysisResult.artifactFileName
            getReasonerForErpAnalysisResult(erpAnalysisResult)
            log.debug "GOT reasoner in async: " + erpAnalysisResult.artifactFileName
        }
    }

    def isTestEXPT(OWLNamedIndividual owlNamedIndividual) {
        return getLabelForUrl(owlNamedIndividual.toStringID())?.contains("TESTEXPT")
    }

    List<String> getInstancesForUrl(ErpAnalysisResult erpAnalysisResult, String url) {
        url = createFullURI(url)
//        log.debug "eval url [" + url + "]"
        List<String> patternInstances = []
        OWLClass parent = factory.getOWLClass(createIRIFromString(url))
        OWLReasoner inferenceReasoner = getReasonerForErpAnalysisResult(erpAnalysisResult)

        if (inferenceReasoner) {
            long startTime = System.currentTimeMillis()
            Set<OWLNamedIndividual> instances = inferenceReasoner.getInstances(parent, false).flattened
//            cachedReasoners.put(dataFile.identifier,inferenceReasoner)
            long totalTime = System.currentTimeMillis() - startTime
            log.debug "instances found: " + instances.size()
            for (OWLNamedIndividual instance in instances) {
                if (!isTestEXPT(instance)) {
                    log.debug "adding instance: " + instance.toStringID()
                    patternInstances.add(instance.toStringID())
                }
            }

            log.debug "totalTime: ${totalTime}"
        }

        return patternInstances
    }


    void setReasonerForErpAnalysisResult(ErpAnalysisResult erpAnalysisResult, OWLReasoner reasoner) {
        cachedReasoners.put(erpAnalysisResult.artifactFileName, reasoner)
    }

    OWLReasoner getReasonerForErpAnalysisResult(ErpAnalysisResult erpAnalysisResult) {
        OWLReasoner reasoner
        if (cachedReasoners.containsKey(erpAnalysisResult.artifactFileName)) {
            reasoner = cachedReasoners.get(erpAnalysisResult.artifactFileName)
        } else {
            log.debug "is not cached loading reloading " + erpAnalysisResult.artifactFileName
            OWLOntology owlOntology1 = getOntologyFromErpAnalysisResult(erpAnalysisResult, owlOntologyManager)
            if (owlOntology1 == null) {
                return null
            }
            reasoner = createReasoner(owlOntology1)
            setReasonerForErpAnalysisResult(erpAnalysisResult, reasoner)
//            cachedReasoners.put(dataFile.identifier, reasoner)
            log.debug "finished reloading and put in cache " + erpAnalysisResult.artifactFileName
        }
        return reasoner
    }

    def getUrlValue(String url) {
        url.substring(url.indexOf("#") + 1).replaceAll("_", " ")
    }

    TermView createTermView(String lookupUrl) {
        TermView termView = new TermView()
        termView.uri = lookupUrl

        log.debug(lookupUrl)
        OWLClass owlClass = getOwlClass(lookupUrl)
        log.debug("found: " + owlClass)
        log.debug("found name: " + render(owlClass))


        for (OWLOntology ont : ontology.getImportsClosure()) {
            Set<OWLAnnotation> annotations = owlClass.getAnnotations(ont)
//            log.debug "other annotaions size: " + annotations.size()
            for (OWLAnnotation annotation : annotations) {
                String value = annotation.value.toString()
                String property = getUrlValue(annotation.property.toStringID())
                switch (property) {
                    case "label":
                        termView.label = value.replaceAll(/\"(.*)\".*/, "\$1").replaceAll("_", " ")
                        break;
                    case "definition":
                        termView.definition = value.replaceAll(/\"(.*)\".*/, "\$1")
                        break;
                    case "synonym":
                        termView.addSynonym(value.replaceAll(/\"(.*)\".*/, "\$1").replaceAll("_", " "))
                        break;
                    case "evidence code":
                        termView.evidenceCode = getUrlValue(value)
                        break;
                    case "curation status":
                        termView.curationStatus = getUrlValue(value)
                        break
                    case "created date":
                        termView.creationDate = parseDate(value)
                        break
                    case "onto definition":
                        int index = value.indexOf("\"^^xsd:string")
                        if (index >= 1) {
                            termView.detailedInfoDump = value.substring(1, index)
                        }
                        break
                    case "modified date":
                        termView.modifiedDate = parseDate(value)
                        break

                    case "curator":
                        termView.addContributor(getUrlValue(value))
                        break;
                    case "pref label":
                        termView.preferredLabel = value
                        break;
                    case "definition source":
                        int index = value.indexOf("\"^^xsd:string")
                        if (index >= 1) {
                            termView.addSource(value?.substring(1, index ))
                        }
                        break;
                    default:
//                       log.debug "unkown property: " + property
                        break;

                }
//               if (value instanceof OWLLiteral) {
//                   log.debug "literal value: " + ((OWLLiteral) value).getLiteral()
////                   sb.append(((OWLLiteral) value).getLiteral())
////                   sb.append(" (")
////                   return true;
//               }
//               else {
//                   log.debug "not literal: " + value
//               }
            }
        }

        // BASIC VIEW
//        termView.label = ontologyService.render(owlClass)
        // need to somehow pull NEMO_annot:defintion .. . is human readable
//        termView.definition =

        // need to somehow pull in a collection of NEMO_annot:synonyms (don't seem to be in any order)
//        termView.synonyms =

        // DETAILED INFO
        // this is NEMO_annot:onto_defintion
//        termView.detailedInfoDump =

        // CURATION STATUS
        // NEMO_annot.pref_label
//        termView.preferredLabel =
        // NEMO_annot.created_date
//        termView.creationDate =
        // NEMO_annot.modified_date.
//        termView.modifiedDate =
        // NEMO_annot.definition_source
//        termView.sources=
        // NEMO_annot.curation_status
//        termView.status=
        // NEMO_annot.evidence_code
//        termView.evidence_code=
        // NEMO_annot.curator
//        termView.contributors =


        return termView
    }

// will come in like this Date.parse("y-mm-dd",value)
    Date parseDate(String s) {
        String inputString = s;
        try {
//            s = s.replaceAll("\"", "")
            s = s.replaceAll (/"/, '')
            int index

            log.debug  "S: ${s}"
            index = s.indexOf(schemaDateString)
            if(index>=0){
                s = s.substring(schemaDateString.length())
                log.debug  "S3: [${inputString}]->[${s}]"
                return Date.parse(dateParseString, s)
            }

            index = s.indexOf("^")
            if(index>=0){
                s = s.substring(0, index)
                log.debug  "S1: [${inputString}]->[${s}]"

                return Date.parse(dateParseString, s)
            }

            if(s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10){
                log.debug  "S2: [${inputString}]-> [${s}]"
//                return Date.parse(dateParseString, s)
                return Date.parse("yyyy-MM-dd", s)
            }
            else{
                log.error "Failed to parse date [${inputString}]->[${s}]"
                println  "Failed to parse date [${inputString}]->[${s}]"
                return null
            }
        } catch (Exception e) {
            log.error "Failed to parse inputString [${inputString}] from parse date [${s}]:\n ${e}"
            return null
        }
    }

    Set<OWLClass> getChildrenForUrls(List<String> urls) {
        Set<OWLClass> subClasses = new HashSet<OWLClass>()
        for (url in urls) {
            subClasses.addAll(getChildrenForUrl(url))
        }
        return subClasses
    }

    Set<OWLClass> getChildrenForUrl(String url) {
        def fullUrl = createFullURI(url)
        log.debug "got the ful URL " + fullUrl
        OWLClass parent = factory.getOWLClass(IRI.create(fullUrl))
        log.debug "got the parent" + fullUrl
        Set<OWLClass> subClasses = nemoReasoner.getSubClasses(parent, false).flattened
        return subClasses
    }

    Set<OWLClass> getChildrenForUrlFromBlank(String url) {
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = owlOntologyManager.getOWLDataFactory();
        OWLClass parent = factory.getOWLClass(IRI.create(url));
        return nemoReasoner.getSubClasses(parent, false).getFlattened();

    }

    Map<String, Set<ErpAnalysisResult>> getErpsFromErpAnalysisResults(String url) {
        Map<String, Set<ErpAnalysisResult>> instancesMap = new TreeMap<String, Set<ErpAnalysisResult>>()
        for (ErpAnalysisResult erpAnalysisResult in ErpAnalysisResult.findAllByInferredOntologyIsNotNull()) {
            List<String> instances = getInstancesForUrl(erpAnalysisResult, url)
            log.debug("instances size: " + instances.size())
            for (String effect in instances) {
                effect = effect.substring(OntologyService.DATA_URL.length())
                log.debug("effect INFO: " + effect)
//                    log.error("effect error: "+effect)
                if (instancesMap.containsKey(effect)) {
                    Set<ErpAnalysisResult> dataFileSet = instancesMap.get(effect)
                    dataFileSet.add(erpAnalysisResult)
                    instancesMap.put(effect, dataFileSet)
                } else {
                    Set<ErpAnalysisResult> dataFileSet = new HashSet<ErpAnalysisResult>()
                    dataFileSet.add(erpAnalysisResult)
                    instancesMap.put(effect, dataFileSet)
                }
            }

//                ontologyService.saveReasoner(dataFile,ontologyService.getReasonerForDataFile(dataFile))
        }
        return instancesMap
    }

    def putReasonersIntoMemory() {
        DataFormat rdfFormat = DataFormat.findByNameLike("rdf%")
        List<ErpAnalysisResult> erpAnalysisResultList = ErpAnalysisResult.findAllByRdfContentIsNotNull()
        log.debug "rdf data format: " + rdfFormat
        for (ErpAnalysisResult erpAnalysisResult1 in erpAnalysisResultList) {
            log.debug "caching datafile: " + erpAnalysisResult1.artifactFileName
            getReasonerForErpAnalysisResult(erpAnalysisResult1)
//            OWLReasoner owlReasoner = getReasonerForDataFile(dataFile)

//            log.info "getting instances for ${dataFile.identifier}"
//            int total = 0
//            for(String key in instanceMap.keySet()){
//                total += getInstancesForUrl(dataFile,getUrlValue(key))?.size()
//            }
//            log.info "GOT instances for ${dataFile.identifier} + ${total}"
        }
    }

    def getInstanceMap() {
        // url, label
        Map<String, String> instanceMap = new TreeMap<String, String>()
        for (instance in InstancesEnum.values()) {
            instanceMap.put(instance, getLabelForUrl(instance.name())?.replaceAll("_", " "))
        }
        return instanceMap
    }


    def reloadGlobalOntology() {
        log.info "start reloading ontology "
        if (nemoOntology) {
            owlOntologyManager.removeOntology(nemoOntology)
            nemoOntology = null
        }

        Ontology globalOntology = Ontology.findByName(globalOntology)

        if (globalOntology) {
            globalOntology.delete(flush: true)
        }
//        Ontology ontology = new Ontology(name: "Global Ontology",date:new Date())
//        ontology.save(insert: true,flush: true,failOnError: true)

        // will do the reload process
        getOntology()

        log.info "finished reloading ontology "
    }

    def reInferAllOntologiesAsync() {
        reInferAllOntologiesAsync(null)
    }

    def reInferAllOntologiesAsync(String email) {
        runAsync {
            ErpAnalysisResult.findAllByRdfContentIsNotNull([sort: "artifactFileName", order: "asc"]).each { erpAnalysisResult ->
                log.info "START re-inferring " + erpAnalysisResult.artifactFileName
                erpAnalysisResult.setInProcess()
                erpAnalysisResult.inferredOntology = null
                erpAnalysisResult.save(flush: true, failOnError: true)
                inferRdfInstance(erpAnalysisResult, email)
                erpAnalysisResult.setDoneProcessing()
                erpAnalysisResult.save(flush: true, failOnError: true)
                log.info "DONE re-inferring " + erpAnalysisResult.artifactFileName
            }
        }
    }

    def updateOntologies(){

        String remoteURL = "http://nemo.nic.uoregon.edu/ontologies/"

        File file = new File(getLocalNemoOntologyFile())
        String oldText = file.exists() ? file.text : ""
        String newText = NS.toURL()?.text

        log.info "time to update: ${!oldText.equals(newText)} - ${oldText.size()} vs ${newText.size()}?"
        if (!oldText.equals(newText)) {
            log.error "ontologies have been updated . . . "
            // download the separate files
            downloadFile("NEMO.owl",remoteURL)
            downloadFile("NEMO_annotation_properties.owl",remoteURL)
            downloadFile("NEMO_deprecated.owl",remoteURL)

            reloadGlobalOntology()
            reInferAllOntologiesAsync()
        } else {
            log.error "ontologies are equivalent . . not updating!!"
        }
    }

    def downloadFile(String fileName,String remoteURL) {

        log.info "downloading " + fileName
        File localFile = new File(getLocalOntologiesDirectory() + fileName)
        if (localFile.delete()) {
            localFile << (remoteURL + fileName).toURL().text
            log.info "finished downloading " + fileName
        }
    }

}
