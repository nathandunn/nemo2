package edu.uoregon.nic.nemo.portal
import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ConditionController)
@Mock([Condition, Experiment, ConditionType, Stimulus, Response, UserService,SecUser,SecUserRole,Laboratory,TaskInstruction,Institution,ExperimentService])
class ConditionControllerTests extends AbstractUnitTest{

    def populateValidParams(params) {
        assert params != null

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

        params["identifier"] = 'Condition-C'
        params["experiment"] = experiment
        params["taskInstruction"] = new TaskInstruction()
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
        assert "/condition/list" == response.redirectedUrl
    }

    void testList() {

        injectSecurityUser(controller)
//        def model = controller.list()
        controller.list()

        assert model.conditionInstanceList.size() == 0
        assert model.conditionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.conditionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.conditionInstance != null
        assert view == '/condition/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/condition/show/1'
        assert controller.flash.message != null
        assert Condition.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/condition/list'


        populateValidParams(params)
        def condition = new Condition(params)

        assert condition.save() != null

        params.id = condition.id

        def model = controller.show()

        assert model.conditionInstance == condition
    }

    void testEdit() {
        SecUser secUser = injectSecurityUser(controller)
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/condition/list'


        populateValidParams(params)
        params.experiment.laboratory.users = [secUser]
        def condition = new Condition(params)

        assert condition.save() != null

        response.reset()
        params.id = condition.id

        controller.edit()
        assert view == '/condition/edit'
        assert model != null

        assert model.conditionInstance == condition
        assert model.conditionInstance == controller.modelAndView.model.conditionInstance
    }

    void testCopyCondition() {
        SecUser secUser = injectSecurityUser(controller)
        controller.copyCondition()

        assert flash.message != null
        assert response.redirectedUrl == '/experiment/list'


        populateValidParams(params)
        params.experiment.laboratory.users = [secUser]
        def condition = new Condition(params)

        assert 0==Condition.count()
        assert condition.save() != null
        assert false==condition.unverifiedCopy

        response.reset()
        params.id = condition.experiment.id
        params.conditionId = condition.id

        assert 1==Condition.count()

        controller.copyCondition()
        assert 2==Condition.count()

        assert view == '/condition/edit'
        assert model != null

        assert model.conditionInstance != condition
        Condition newCondition = Condition.get(2)
        assert model.conditionInstance == newCondition
        assert true==newCondition.unverifiedCopy
        assert  newCondition != null
        assert model.conditionInstance.experiment.id == condition.experiment.id
//        assert model.conditionInstance == controller.modelAndView.model.conditionInstance
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/condition/list'

        response.reset()


        populateValidParams(params)
        def condition = new Condition(params)

        assert condition.save() != null

        // test invalid parameters in update
        params.id = condition.id
        //TODO: add invalid values to params object
        params.experiment = null

        controller.update()

        assert view == "/condition/edit"
        assert model.conditionInstance != null

        condition.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/condition/show/$condition.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        condition.clearErrors()

        populateValidParams(params)
        params.id = condition.id
        params.version = -1
        controller.update()

        assert view == "/condition/edit"
        assert model.conditionInstance != null
        assert model.conditionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/condition/list'

        response.reset()

        populateValidParams(params)
        def condition = new Condition(params)

        assert condition.save() != null
        assert Condition.count() == 1

        params.id = condition.id

        controller.delete()

        assert Condition.count() == 0
        assert Condition.get(condition.id) == null
        assert response.redirectedUrl == '/condition/list'
    }

}
