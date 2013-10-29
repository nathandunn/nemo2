package edu.uoregon.nic.nemo.portal
import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ExperimentController)
@Mock([Experiment,Laboratory,Publication,SubjectGroup,Condition,Stimulus,SecUser,SecUserRole,Response,EegDataCollection,ErpDataPreprocessing,UserService])
class ExperimentControllerTests extends AbstractUnitTest {


    def populateValidParams(params) {
        assert params != null
        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.save(failOnError: true)
        params["identifier"] = RandomStringGenerator.getRandomString()
        params["laboratory"] = laboratory
        params.narrativeSummary = "blah blah blah"
        params.subjectGroupsNarrativeSummary = "blah blah blah"
        params.conditionsNarrativeSummary = "blah blah blah"
        params.erpDataPreprocessingsNarrativeSummary = "blah blah blah"
        params.eegDataCollectionNarrativeSummary = "blah blah blah"

    }

//    SecUser injectSecurityUser(){
//        SecUser secUser = new SecUser(
//                username: RandomStringGenerator.getRandomEmail()
//                , password: "secret"
//                , level: "Commercial"
//                , firstname: "George"
//                , lastname: "TheMonkey"
//        )
//        controller.springSecurityService = new StubSpringSecurityService()
//        controller.springSecurityService.setCurrentUser(secUser)
//        return secUser
//    }

    void testIndex() {
        controller.index()
        assert "/experiment/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.experimentInstanceList.size() == 0
        assert model.experimentInstanceTotal == 0
    }

    void testCreate() {
        injectSecurityUser(controller)
        def model = controller.create()
        assert model.experimentInstance != null
    }

    void testSave() {
        SecUser secUser = injectSecurityUser(controller)
        controller.save()

        assert model.experimentInstance != null
        assert view == '/experiment/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/experiment/show/1'
        assert controller.flash.message != null
        assert Experiment.count() == 1
    }

    void testShow() {
        injectSecurityUser(controller)
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/experiment/list'


        populateValidParams(params)
        def experiment = new Experiment(params)

        assert experiment.save(failOnError: true) != null

        params.id = experiment.id

        def model = controller.show()

        assert model.experimentInstance == experiment
    }

    void testEdit() {
        injectSecurityUser(controller)
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/experiment/list'


        populateValidParams(params)
        def experiment = new Experiment(params)

        assert experiment.save() != null

        params.id = experiment.id

        def model = controller.edit()

        assert model.experimentInstance == experiment
    }

    void testUpdate() {
        injectSecurityUser(controller)
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/experiment/list'

        response.reset()


        populateValidParams(params)
        def experiment = new Experiment(params)

        assert experiment.save() != null

        // test invalid parameters in update
        params.id = experiment.id
        params.identifier = null

        controller.update()

        assert view == "/experiment/edit"
        assert model.experimentInstance != null

        experiment.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/experiment/show/$experiment.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        experiment.clearErrors()

        populateValidParams(params)
        params.id = experiment.id
        params.version = -1
        controller.update()

        assert view == "/experiment/edit"
        assert model.experimentInstance != null
        assert model.experimentInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        injectSecurityUser(controller)
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/experiment/list'

        response.reset()

        populateValidParams(params)
        def experiment = new Experiment(params)

        assert experiment.save() != null
        assert Experiment.count() == 1

        params.id = experiment.id

        controller.delete()

        assert Experiment.count() == 0
        assert Experiment.get(experiment.id) == null
        assert response.redirectedUrl == '/experiment/list'
    }
}
