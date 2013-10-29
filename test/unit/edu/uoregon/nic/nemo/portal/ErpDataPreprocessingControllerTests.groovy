package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ErpDataPreprocessingController)
@Mock([ErpDataPreprocessing,Experiment, Laboratory, ErpEvent, OfflineReference, UserService, SecUser, SecUserRole])
class ErpDataPreprocessingControllerTests {


    def populateValidParams(params) {
        assert params != null
        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
                , users: [injectSecurityUser()]
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

        params.identifier = RandomStringGenerator.getRandomString()
        params.experiment = experiment
        params.event = new ErpEvent()
        params.reference = new OfflineReference()
        params.erpEpochLength = 12
        params.baselineLength = 12
        params.highpassFilterAlgorithm = 12
        params.lowpassFilterAlgorithm = 12
        params.numberGoodTrials = 12
    }

    SecUser injectSecurityUser(){
        SecUser secUser = new SecUser(
                username: RandomStringGenerator.getRandomEmail()
                , password: "secret"
                , level: "Commercial"
                , firstname: "George"
                , lastname: "TheMonkey"
        )
        controller.springSecurityService = new StubSpringSecurityService()
        controller.springSecurityService.setCurrentUser(secUser)
        return secUser
    }

    void testIndex() {
        controller.index()
        assert "/erpDataPreprocessing/list" == response.redirectedUrl
    }

    void testList() {

        controller.list()

        assert model.erpDataPreprocessingInstanceList.size() == 0
        assert model.erpDataPreprocessingInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.erpDataPreprocessingInstance != null
    }

    void testSave() {
        controller.save()

        assert model.erpDataPreprocessingInstance != null
        assert view == '/erpDataPreprocessing/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/erpDataPreprocessing/show/1'
        assert controller.flash.message != null
        assert ErpDataPreprocessing.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/erpDataPreprocessing/list'


        populateValidParams(params)
        def erpDataPreprocessing = new ErpDataPreprocessing(params)

        assert erpDataPreprocessing.save() != null

        params.id = erpDataPreprocessing.id

        def model = controller.show()

        assert model.erpDataPreprocessingInstance == erpDataPreprocessing
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/erpDataPreprocessing/list'


        populateValidParams(params)
        def erpDataPreprocessing = new ErpDataPreprocessing(params)

        assert erpDataPreprocessing.save() != null

        params.id = erpDataPreprocessing.id

        def model = controller.edit()

        assert model.erpDataPreprocessingInstance == erpDataPreprocessing
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/erpDataPreprocessing/list'

        response.reset()


        populateValidParams(params)
        def erpDataPreprocessing = new ErpDataPreprocessing(params)

        assert erpDataPreprocessing.save() != null

        // test invalid parameters in update
        params.id = erpDataPreprocessing.id
        params.identifier = null

        controller.update()

        assert view == "/erpDataPreprocessing/edit"
        assert model.erpDataPreprocessingInstance != null

        erpDataPreprocessing.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/erpDataPreprocessing/show/$erpDataPreprocessing.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        erpDataPreprocessing.clearErrors()

        populateValidParams(params)
        params.id = erpDataPreprocessing.id
        params.version = -1
        controller.update()

        assert view == "/erpDataPreprocessing/edit"
        assert model.erpDataPreprocessingInstance != null
        assert model.erpDataPreprocessingInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/erpDataPreprocessing/list'

        response.reset()

        populateValidParams(params)
        def erpDataPreprocessing = new ErpDataPreprocessing(params)

        assert erpDataPreprocessing.save() != null
        assert ErpDataPreprocessing.count() == 1

        params.id = erpDataPreprocessing.id

        controller.delete()

        assert ErpDataPreprocessing.count() == 0
        assert ErpDataPreprocessing.get(erpDataPreprocessing.id) == null
        assert response.redirectedUrl == '/erpDataPreprocessing/list'
    }
}
