package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Stimulus)
@Mock([StimulusType,StimulusModality])
class StimulusTests {

    void testSomething() {
        new Stimulus(
                identifier: RandomStringGenerator.getRandomString()
                , stimulusOnsetAsynchrony: 12.2
                , targetType: new StimulusType()
                , targetModality: new StimulusModality()
        )
                .save(failOnError: true)
    }
}
