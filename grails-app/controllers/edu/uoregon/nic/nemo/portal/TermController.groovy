package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.TermView
import grails.web.RequestParameter
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLOntology

class TermController {

    def ontologyService

    def afterInterceptor = { model ->
        model.experimentHeader = model.experimentHeader ?: model.response?.condition?.experiment
        model.experimentHeader = model.experimentHeader ?: model.responseInstance?.condition?.experiment
    }

    def show(@RequestParameter('id') String lookupURL) {

//        String lookupURL = params.id

        if (!lookupURL) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])
            redirect(action: "list")
            return
        }

        List<Ontological> ontological = Ontological.findAllByUrl(ontologyService.createFullURI(lookupURL))
        println "ontological found ${ontological?.relatedLookup}"
        if (!ontological) {
            log.debug("not found for in NEMO ontology [${lookupURL}]")
        }

        TermView termView = ontologyService.createTermView(lookupURL)

        // to determine if a term will be in pattern
        // TODO: this is a bug !
        Map<String, Set<ErpAnalysisResult>> instancesMap = new TreeMap<String, Set<ErpAnalysisResult>>()
        instancesMap = ontologyService.getErpsFromErpAnalysisResults(lookupURL)
//        for(key in instancesMap.keySet()){
//            println "key ${key}"
//        }

//        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager()
//        OWLClass parent = owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(OntologyService.NS + QueryListEnum.SCALP_RECORDED_ERP_DIFFWAVE_COMPONENT.url))
//        ontologyService.getNemoReasoner()
//        OWLReasoner reasoner = ontologyService.getReasonerForDataFile(dataFile)
//        OWLOntology localOntology = ontologyService.getOntologyFromDataFile(dataFile)

        [termView: termView, related: ontological?.relatedLookup, instances: instancesMap]
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        params.offset = params.offset ? params.offset as Integer : 0


        OWLOntology ontology = ontologyService.getOntology()
        Set<OWLClass> classes = ontology.getClassesInSignature(false)
//        if (params.sort == "uri") {
//            classes = ontology.getClassesInSignature(false).sort() { a, b ->
//                if (params.order == "asc") {
//                    return a.toStringID().compareToIgnoreCase(b.toStringID())
//                } else {
//                    return b.toStringID().compareToIgnoreCase(a.toStringID())
//                }
//            }
//        }
//        else{

//        }

        List<OWLClass> orderedClasses
        if (params.sort == "uri") {
            orderedClasses = classes.sort() { a, b ->
                if (params.order == "asc") {
                return a.toStringID().compareToIgnoreCase(b.toStringID())
                }
                else{
                    return b.toStringID().compareToIgnoreCase(a.toStringID())
                }
            }
        } else {
            orderedClasses = classes as List
        }

        Iterator<OWLClass> iter = orderedClasses.iterator()
        def termInstantList = []
        for (int i = 0; i < params.offset && i < classes.size(); i++) {
            iter.next()
        }
        for (int i = params.offset; i < params.max + params.offset && i < classes.size(); i++) {
            OWLClass entity = iter.next()
            TermView termView = ontologyService.createTermView(entity.toStringID())
            termInstantList.add(termView)
        }
        println "classes.size ${classes.size()} "
        println "termInstanceList.size ${termInstantList.size()} "
        [termInstanceList: termInstantList, termInstanceTotal: classes.size()]
    }
}
