package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ErpPatternExtractionController)
//@Mock([ErpPatternExtraction])
@Mock([Laboratory,Experiment,ErpDataPreprocessing,DataSet,DataFormat,ErpPatternExtraction,UserService,SecUser,SecUserRole,ErpAnalysisService])

class ErpPatternExtractionControllerTests {

    SecUser injectSecurityUser(){
        SecUser secUser = SecUser.get(1) ?: new SecUser(
                username: RandomStringGenerator.getRandomEmail()
                , password: "secret"
                , level: "Commercial"
                , firstname: "George"
                , lastname: "TheMonkey"
        )
        .save(failOnError: true,flush: true)
        controller.springSecurityService = new StubSpringSecurityService()
        controller.springSecurityService.setCurrentUser(secUser)
        return secUser
    }

    def populateValidParams(params) {
        assert params != null
        params["artifactFileName"] = RandomStringGenerator.getRandomString()
        params["set"] = new DataSet()
        params["format"] = new DataFormat()

        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        if(SecUser.count()>0){
            laboratory.users = [SecUser.get(1)]
        }
        laboratory.save(failOnError: true)
        Experiment experiment = new Experiment(
                identifier: RandomStringGenerator.getRandomString()
                , laboratory: laboratory
                , narrativeSummary: "blah blah blah"
                , subjectGroupsNarrativeSummary: "blah blah blah"
                , conditionsNarrativeSummary: "blah blah blah"
                , erpDataPreprocessingsNarrativeSummary: "blah blah blah"
                , eegDataCollectionNarrativeSummary: "blah blah blah"
                , erpPatternExtractionNarrativeSummary: "blah blah blah"

        )
        .save(failOnError: true)
        params["experiment"] = experiment
    }

    void testIndex() {
        controller.index()
        assert "/erpPatternExtraction/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.erpPatternExtractionInstanceList.size() == 0
        assert model.erpPatternExtractionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.erpPatternExtractionInstance != null
    }

    void testSave() {
        // most of the parameters can be null, so no reason to fail
        assert ErpPatternExtraction.count() == 0
        controller.save()

//        assert model.erpPatternExtractionInstance != null
        assert model.erpPatternExtractionInstance == null
//        assert view == '/erpPatternExtraction/create'
        assert response.redirectedUrl == '/erpPatternExtraction/show/1'
        assert ErpPatternExtraction.count() == 1

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/erpPatternExtraction/show/2'
        assert controller.flash.message != null
        assert ErpPatternExtraction.count() == 2
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/erpPatternExtraction/list'


        populateValidParams(params)
        def erpPatternExtraction = new ErpPatternExtraction(params)

        assert erpPatternExtraction.save() != null

        params.id = erpPatternExtraction.id

        controller.show(erpPatternExtraction.id)

        assert model.erpPatternExtractionInstance == erpPatternExtraction
    }

    void testEdit() {
        SecUser secUser = injectSecurityUser()
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/erpPatternExtraction/list'


        populateValidParams(params)

        def erpPatternExtraction = new ErpPatternExtraction(params)

        assert erpPatternExtraction.save() != null

        params.id = erpPatternExtraction.id
        response.reset()

        controller.edit()

        assert model.erpPatternExtractionInstance == erpPatternExtraction
    }

    void testUpdate() {
        SecUser secUser = injectSecurityUser()
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/erpPatternExtraction/list'

        response.reset()


        populateValidParams(params)
        def erpPatternExtraction = new ErpPatternExtraction(params)

        assert erpPatternExtraction.save() != null

        erpPatternExtraction.experiment = Experiment.get(1)

        // test invalid parameters in update
        params.id = erpPatternExtraction.id
        params.artifactFileName = null

        controller.update()

        assert view == "/erpPatternExtraction/edit"
        assert model.erpPatternExtractionInstance != null

        erpPatternExtraction.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/erpPatternExtraction/show/$erpPatternExtraction.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        erpPatternExtraction.clearErrors()

        populateValidParams(params)
        params.id = erpPatternExtraction.id
        params.version = -1
        controller.update()

        assert view == "/erpPatternExtraction/edit"
        assert model.erpPatternExtractionInstance != null
        assert model.erpPatternExtractionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        injectSecurityUser()
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/erpPatternExtraction/list'

        response.reset()

        populateValidParams(params)
        def erpPatternExtraction = new ErpPatternExtraction(params)

        assert erpPatternExtraction.save() != null
        assert ErpPatternExtraction.count() == 1

        params.id = erpPatternExtraction.id

        controller.delete()

        assert ErpPatternExtraction.count() == 0
        assert ErpPatternExtraction.get(erpPatternExtraction.id) == null
        assert response.redirectedUrl == '/experiment/list/'+erpPatternExtraction.experiment.id
    }
}
