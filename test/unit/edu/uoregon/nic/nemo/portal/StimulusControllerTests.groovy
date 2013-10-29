package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(StimulusController)
@Mock([Laboratory,Experiment,TaskInstruction,Stimulus, Condition,  StimulusType, StimulusModality,SecUser,SecUserRole,UserService,Role])
class StimulusControllerTests extends AbstractUnitTest {


    SecUser populateValidParams(params) {
        assert params != null


//        injectSecurityUserAsAdmin(controller)
        SecUser secUser = SecUser.get(1) ?: injectSecurityUser(controller)

        params.identifier = getRandomString()
        params.stimulusOnsetAsynchrony = 0.4
//        params.stimulusOnsetAsynchrony
        params.targetType = new StimulusType().save(failOnError: true)
        params.targetModality = new StimulusModality().save(failOnError: true)


        Laboratory laboratory = new Laboratory(
                identifier: getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
                ,users: [secUser]
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
                identifier: getRandomString()
                ,experiment: experiment
                ,taskInstruction: new TaskInstruction()
        )
        .save(failOnError: true)

        params.condition = condition

        return secUser
    }

    void testIndex() {
        controller.index()
        assert "/stimulus/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.stimulusInstanceList.size() == 0
        assert model.stimulusInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.stimulusInstance != null
    }

    void testSave() {
        controller.save()

        assert model.stimulusInstance != null
        assert view == '/stimulus/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/stimulus/show/1'
        assert controller.flash.message != null
        assert Stimulus.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/stimulus/list'


        populateValidParams(params)
        def stimulus = new Stimulus(params)

        assert stimulus.save() != null

        params.id = stimulus.id

        def model = controller.show()

        assert model.stimulusInstance == stimulus
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/stimulus/list'


        populateValidParams(params)
        def stimulus = new Stimulus(params)

        assert stimulus.save() != null

        params.id = stimulus.id

        response.reset()

        controller.edit()

        assert model.stimulusInstance == stimulus
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/stimulus/list'

        response.reset()


        populateValidParams(params)
        def stimulus = new Stimulus(params)

        assert stimulus.save() != null

        // test invalid parameters in update
        params.id = stimulus.id
        params.identifier = null

        controller.update()

        assert view == "/stimulus/edit"
        assert model.stimulusInstance != null

        stimulus.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/stimulus/show/${stimulus.id}"
        assert flash.message != null

        //test outdated version number
        response.reset()
        stimulus.clearErrors()

        SecUser secUser = populateValidParams(params)
        makeUserAdmin(secUser)
        params.id = stimulus.id
        params.version = -1
        controller.update()

        assert view == "/stimulus/edit"
        assert model.stimulusInstance != null
        assert model.stimulusInstance.errors.getFieldError('version')
        assert flash.message != null
    }



    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/stimulus/list'

        response.reset()

        populateValidParams(params)
        def stimulus = new Stimulus(params)

        assert stimulus.save() != null
        assert Stimulus.count() == 1

        params.id = stimulus.id

        controller.delete()

        assert Stimulus.count() == 0
        assert Stimulus.get(stimulus.id) == null
        assert response.redirectedUrl == '/stimulus/list'
    }
}
