package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ErpDataPreprocessing)
@Mock([Experiment,Laboratory,ErpEvent,OfflineReference])
class ErpDataPreprocessingTests {

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

        new ErpDataPreprocessing(
                identifier: RandomStringGenerator.getRandomString()
                ,experiment: experiment
                ,event:  new ErpEvent()
                ,reference:  new OfflineReference()
                ,erpEpochLength: 12
                ,baselineLength: 12
                ,highpassFilterAlgorithm: 12
                ,lowpassFilterAlgorithm: 12
                ,numberGoodTrials: 12
        )
        .save(failOnError: true)
    }
}
