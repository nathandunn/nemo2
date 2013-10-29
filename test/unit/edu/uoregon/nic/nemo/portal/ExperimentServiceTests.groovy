package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ExperimentService)
@Mock([Laboratory,Condition,Experiment,Stimulus,SecUser,SecUserRole,Response,StimulusType,StimulusModality,ConditionType,ResponseModality,ResponseRole])
class ExperimentServiceTests extends AbstractUnitTest{

    @Before
    void setup(){
        Laboratory laboratory = new Laboratory(
                identifier: getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.save(failOnError: true)
        Experiment experiment = new Experiment(
                identifier: getRandomString()
                , laboratory: laboratory
                , narrativeSummary: "blah blah blah"
                , subjectGroupsNarrativeSummary: "blah blah blah"
                , conditionsNarrativeSummary: "blah blah blah"
                , erpDataPreprocessingsNarrativeSummary: "blah blah blah"
                , eegDataCollectionNarrativeSummary: "blah blah blah"
        )
        experiment.save(failOnError: true)

        Condition condition = new Condition(
                identifier: "Condition-C"
                ,experiment: experiment
                ,taskInstruction: new TaskInstruction()
        )

        condition.save(failOnError: true)

        Stimulus stimulus = new Stimulus(
                identifier: getRandomString()
                ,stimulusOnsetAsynchrony: 0.4
                ,targetType: new StimulusType().save(failOnError: true)
                ,targetModality: new StimulusModality().save(failOnError: true)
                ,condition: condition
        )
        .save(failOnError: true)

        Response response = new Response(
                identifier: getRandomString()
                ,role: new ResponseRole()
                ,modality: new ResponseModality().save(failOnError: true)
                ,condition: condition
        )
                .save(failOnError: true)

        ConditionType conditionType = new ConditionType(
                url: getRandomNemoURL()
                ,conditions: [condition]
                ,name: getRandomString()
        )
                .save(failOnError: true)


        condition.addToStimulus(stimulus)
        condition.addToResponse(response)
        condition.addToTypes(conditionType)
//        conditionType.addToConditions(condition)

        condition.save(flush: true,insert: false)


    }

    void testConditionCopy() {
//        SecUser secUser = injectSecurityUser()
        assert 1 == Condition.count()
        Condition oldConditionInstance = Condition.get(1)

        Condition newConditionCopy = new Condition(oldConditionInstance.properties)
        newConditionCopy.id = null
        newConditionCopy.identifier += "-copy"
        newConditionCopy.experiment = oldConditionInstance.experiment
        newConditionCopy.unverifiedCopy = true
        newConditionCopy.stimulus = null
        newConditionCopy.response = null
        newConditionCopy.types = null

        service.replicateConditionParameters(oldConditionInstance,newConditionCopy)
        newConditionCopy.save(flush: true,insert: false)
        assert 2 == Condition.count()

        newConditionCopy.refresh()
        assert newConditionCopy==Condition.get(2)

        assert 1==newConditionCopy.stimulus.size()
        assert 1==newConditionCopy.response.size()
        assert 1==newConditionCopy.types.size()
    }
}
