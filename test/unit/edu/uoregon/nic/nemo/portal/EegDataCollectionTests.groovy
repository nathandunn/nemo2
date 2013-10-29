package edu.uoregon.nic.nemo.portal
import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EegDataCollection)
@Mock([Laboratory,Experiment,ElectrodeArrayManufacturer,ElectrodeArrayLayout,Software])
class EegDataCollectionTests {

    void testSomething() {
        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.save(failOnError: true)
        Experiment experiment = new Experiment(
                identifier: RandomStringGenerator.getRandomString()
                , laboratory: laboratory
                , narrativeSummary: "blah blah blah"
                , subjectGroupsNarrativeSummary: "blah blah blah"
                , conditionsNarrativeSummary: "blah blah blah"
                , erpDataPreprocessingsNarrativeSummary: "blah blah blah"
                , eegDataCollectionNarrativeSummary: "blah blah blah"

        )
        experiment.save(failOnError: true)

        new EegDataCollection(
                identifier: RandomStringGenerator.getRandomString()
                ,experiment: experiment
                ,samplingRateSetting: 12.2f
                ,electrodeArrayLayout: new ElectrodeArrayLayout()
        )
    }
}
