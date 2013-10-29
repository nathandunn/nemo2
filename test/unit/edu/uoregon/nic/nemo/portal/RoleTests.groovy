package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Role)
class RoleTests {

    void testSomething() {
        new Role(
                authority: "ROLE_ADMIN"
        )
        .save(failOnError: true)
    }
}
