package edu.uoregon.nic.nemo.portal

import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Ontology)
class OntologyTests {

    void testSomething() {
        Ontology ontology = new Ontology(
                name:  "asdf"
                ,description:  "stuff and stuff "
                ,xmlContent:  "other stuff"
        )
        .save(flush: true,validate: true)
    }
}
