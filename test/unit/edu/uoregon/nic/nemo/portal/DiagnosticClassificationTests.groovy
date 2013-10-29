package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DiagnosticClassification)
class DiagnosticClassificationTests {

    void testSomething() {
        new DiagnosticClassification(
                url: "http://asdfasdf.asdf"
                , name: "condition"
        ).save(flush: true)
    }
}
