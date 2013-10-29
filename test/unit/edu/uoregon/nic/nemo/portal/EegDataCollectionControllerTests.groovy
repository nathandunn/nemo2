package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(EegDataCollectionController)
@Mock([EegDataCollection, Laboratory, Experiment, ElectrodeArrayManufacturer, ElectrodeArrayLayout, Software, UserService, SecUser, SecUserRole])
class EegDataCollectionControllerTests {


    def populateValidParams(params) {
        assert params != null
        SecUser secUser = injectSecurityUser()
        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.users = [secUser]
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
        params.samplingRateSetting = 12.2f
        params.electrodeArrayLayout = new ElectrodeArrayLayout()
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
        assert "/eegDataCollection/list" == response.redirectedUrl
    }

    void testList() {

        controller.list()

        assert model.eegDataCollectionInstanceList.size() == 0
        assert model.eegDataCollectionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.eegDataCollectionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.eegDataCollectionInstance != null
        assert view == '/eegDataCollection/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/eegDataCollection/show/1'
        assert controller.flash.message != null
        assert EegDataCollection.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/eegDataCollection/list'


        populateValidParams(params)
        def eegDataCollection = new EegDataCollection(params)

        assert eegDataCollection.save() != null

        params.id = eegDataCollection.id

        def model = controller.show()

        assert model.eegDataCollectionInstance == eegDataCollection
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/eegDataCollection/list'


        populateValidParams(params)
        def eegDataCollection = new EegDataCollection(params)

        assert eegDataCollection.save() != null

        params.id = eegDataCollection.id

        response.reset()

        def model = controller.edit()

        assert model.eegDataCollectionInstance == eegDataCollection
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/eegDataCollection/list'

        response.reset()


        populateValidParams(params)
        def eegDataCollection = new EegDataCollection(params)

        assert eegDataCollection.save() != null

        // test invalid parameters in update
        params.id = eegDataCollection.id
        params.identifier = null

        controller.update()

        assert view == "/eegDataCollection/edit"
        assert model.eegDataCollectionInstance != null

        eegDataCollection.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/eegDataCollection/show/${eegDataCollection.id}"
        assert flash.message != null

        //test outdated version number
        response.reset()
        eegDataCollection.clearErrors()

        populateValidParams(params)
        params.id = eegDataCollection.id
        params.version = -1
        controller.update()

        assert view == "/eegDataCollection/edit"
        assert model.eegDataCollectionInstance != null
        assert model.eegDataCollectionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/eegDataCollection/list'

        response.reset()

        populateValidParams(params)
        def eegDataCollection = new EegDataCollection(params)

        assert eegDataCollection.save() != null
        assert EegDataCollection.count() == 1

        params.id = eegDataCollection.id

        controller.delete()

        assert EegDataCollection.count() == 0
        assert EegDataCollection.get(eegDataCollection.id) == null
        assert response.redirectedUrl == '/eegDataCollection/list'
    }
}
