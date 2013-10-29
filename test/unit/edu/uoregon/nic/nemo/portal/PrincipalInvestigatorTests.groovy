package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(PrincipalInvestigator)
class PrincipalInvestigatorTests extends AbstractUnitTest{

    void testSomething() {
        new PrincipalInvestigator(
                name: getRandomString()
                ,url: getRandomUrl()
        )
        .save(failOnError: true)
    }
}
