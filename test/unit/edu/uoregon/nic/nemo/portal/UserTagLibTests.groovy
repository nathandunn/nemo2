package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(UserTagLib)
@Mock([SecUser,UserService,Role,SecUserRole])
class UserTagLibTests extends AbstractUnitTest{

    void testSomething() {

        SecUser bob = injectSecurityUser(tagLib)
        bob.save(failOnError: true)
        tagLib.springSecurityService = new StubSpringSecurityService()
        String tagText = '<g:editable users="${bob}">yes</g:editable>'
        assertEquals("",applyTemplate(tagText,[bob:bob]))
        tagLib.springSecurityService.setCurrentUser(bob)
        assertEquals("yes",applyTemplate(tagText,[bob:bob]))
    }
}
