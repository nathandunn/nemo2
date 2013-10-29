package edu.uoregon.nic.nemo.portal
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import static org.junit.Assert.fail

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class ConditionTypeTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testSomething() {
        ConditionType conditionType = new ConditionType(
               url:"http://asdfasdf.asdf"
                ,name: "condition"
        ).save(flush: true)
    }
}
