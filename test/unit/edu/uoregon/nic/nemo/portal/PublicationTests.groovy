package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Publication)
@Mock([Laboratory, Experiment,SecUser,PublicationType])
class PublicationTests extends AbstractUnitTest {

    void testSomething() {
        Publication publication = new Publication(
                identifier: getRandomString()
                , publicationDate: 2000
                , digitalObjectIdentifier: getRandomString()
                , type: new PublicationType()
        )
                .save(failOnError: true)
    }

}
