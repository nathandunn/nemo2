package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(StimulusQuality)
class StimulusQualityTests {

    void testSomething() {
        new StimulusQuality(
                url: "http://asdfasdf.asdf"
                , name: "condition"
        ).save(flush: true)
    }
}
