package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(StimulusModality)
class StimulusModalityTests {

    void testSomething() {
        new StimulusModality(
                url: "http://asdfasdf.asdf"
                , name: "condition"
        ).save(flush: true)
    }
}
