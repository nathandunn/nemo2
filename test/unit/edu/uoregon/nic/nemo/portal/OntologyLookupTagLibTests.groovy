package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(OntologyLookupTagLib)
@Mock([OntologyService,Ontology])
class OntologyLookupTagLibTests {

    void testSomething() {
//        http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8539000
        def template = '<g:renderUrl input="http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8539000"/>'
        tagLib.ontologyService = new OntologyService()
//
        def result = applyTemplate(template)
        assertEquals("start date for data collection ",result)
    }
}
