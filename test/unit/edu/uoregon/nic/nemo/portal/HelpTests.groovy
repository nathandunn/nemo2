package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.*

import edu.uoregon.nic.nemo.portal.Help

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Help)
class HelpTests {

    void testSomething() {
        new Help(
                emailFrom: RandomStringGenerator.getRandomEmail()
                ,subject: RandomStringGenerator.getRandomString()
                ,message: RandomStringGenerator.getRandomString()
        )
        .save(failOnError: true)
    }
}
