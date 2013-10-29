package edu.uoregon.nic.nemo.portal

import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SubjectGroup)
class SubjectGroupTests extends AbstractUnitTest{

    void testSomething() {
        Experiment experiment = new Experiment()

        new SubjectGroup(
                experiment: experiment
                ,identifier: getRandomString()
                ,groupSize: 12
                ,genus: "Homo"
                ,species: "Sapiens"
                ,groupAge: 30
        )
        .save(failOnError: true)
    }
}
