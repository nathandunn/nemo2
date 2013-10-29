package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([SecUser, SecUserRole, Role])
class UserServiceTests extends AbstractUnitTest {

    void testSomething() {
        SecUser user1 = injectSecurityUser()
        assertFalse(service.isAdmin(user1))
        assertFalse(service.isAdminOrCurrent([], user1))
        assertTrue(service.isAdminOrCurrent([user1], user1))
        SecUser user2 = injectSecurityUserAsAdmin()
        assertTrue(service.isAdmin(user2))
        assertTrue(service.isAdminOrCurrent([], user2))
        assertTrue(service.isAdminOrCurrent([user1], user2))
    }
}
