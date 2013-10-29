package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Response)
@Mock([ResponseRole,ResponseModality])
class ResponseTests {

    void testSomething() {
        new Response(
               identifier: RandomStringGenerator.getRandomString()
                ,role:  new ResponseRole()
                ,modality:  new ResponseModality()
        )
        .save(failOnError: true)
    }
}
