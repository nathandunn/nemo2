package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ResponseRole)
class ResponseRoleTests {

    void testSomething() {
        new ResponseRole(
                url: "http://asdfasdf.asdf"
                , name: "condition"
        ).save(flush: true)
    }
}
