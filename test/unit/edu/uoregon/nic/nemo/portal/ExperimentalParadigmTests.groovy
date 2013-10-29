package edu.uoregon.nic.nemo.portal

import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ExperimentalParadigm)
class ExperimentalParadigmTests {

    void testSomething() {
        new ExperimentalParadigm(
                url: "http://asdfasdf.asdf"
                , name: "condition"
        ).save(flush: true)
    }
}
