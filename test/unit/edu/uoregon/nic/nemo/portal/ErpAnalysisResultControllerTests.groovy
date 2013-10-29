package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Ignore

@TestFor(ErpAnalysisResultController)
//@Mock([ErpAnalysisResult])
@Mock([Laboratory,Experiment,ErpPatternExtraction,ErpDataPreprocessing,DataSet,DataFormat,ErpAnalysisResult,UserService,SecUser,SecUserRole,ErpAnalysisService])

class ErpAnalysisResultControllerTests {

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
                , erpAnalysisResultNarrativeSummary: "blah blah blah"

        )
        .save(failOnError: true)
        params["experiment"] = experiment
    }

    void testIndex() {
        controller.index()
        assert "/erpAnalysisResult/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.erpAnalysisResultInstanceList.size() == 0
        assert model.erpAnalysisResultInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.erpAnalysisResultInstance != null
    }

    @Ignore
    void testSave() {
        assert ErpAnalysisResult.count() == 0
        controller.searchService = new SearchService()
        controller.searchService.runAsynchornous = false
        controller.save()

//        assert model.erpAnalysisResultInstance != null
        assert model.erpAnalysisResultInstance == null
//        assert view == '/erpAnalysisResult/create'
        assert response.redirectedUrl == '/erpAnalysisResult/show/1'
        assert ErpAnalysisResult.count() == 1

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/erpAnalysisResult/show/2'
        assert controller.flash.message != null
        assert ErpAnalysisResult.count() == 2
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/erpAnalysisResult/list'


        populateValidParams(params)
        def erpAnalysisResult = new ErpAnalysisResult(params)

        assert erpAnalysisResult.save() != null

        params.id = erpAnalysisResult.id

        controller.show(erpAnalysisResult.id)

        assert model.erpAnalysisResultInstance == erpAnalysisResult
    }

    void testEdit() {
        SecUser secUser = injectSecurityUser()
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/erpAnalysisResult/list'


        populateValidParams(params)

        def erpAnalysisResult = new ErpAnalysisResult(params)

        assert erpAnalysisResult.save() != null

        params.id = erpAnalysisResult.id
        response.reset()

        controller.edit()

        assert model.erpAnalysisResultInstance == erpAnalysisResult
    }

    @Ignore
    void testUpdate() {
        SecUser secUser = injectSecurityUser()
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/erpAnalysisResult/list'

        response.reset()


        populateValidParams(params)
        def erpAnalysisResult = new ErpAnalysisResult(params)

        assert erpAnalysisResult.save() != null

        erpAnalysisResult.experiment = Experiment.get(1)

        // test invalid parameters in update
        params.id = erpAnalysisResult.id
        params.artifactFileName = null

        controller.update()

        assert view == "/erpAnalysisResult/edit"
        assert model.erpAnalysisResultInstance != null

        erpAnalysisResult.clearErrors()

        populateValidParams(params)
        controller.searchService = new SearchService()
        controller.searchService.runAsynchornous = false
        controller.update()

        assert response.redirectedUrl == "/erpAnalysisResult/show/$erpAnalysisResult.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        erpAnalysisResult.clearErrors()

        populateValidParams(params)
        params.id = erpAnalysisResult.id
        params.version = -1
        controller.update()

        assert view == "/erpAnalysisResult/edit"
        assert model.erpAnalysisResultInstance != null
        assert model.erpAnalysisResultInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        injectSecurityUser()
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/erpAnalysisResult/list'

        response.reset()

        populateValidParams(params)
        def erpAnalysisResult = new ErpAnalysisResult(params)

        assert erpAnalysisResult.save() != null
        assert ErpAnalysisResult.count() == 1

        params.id = erpAnalysisResult.id

        controller.delete()

        assert ErpAnalysisResult.count() == 0
        assert ErpAnalysisResult.get(erpAnalysisResult.id) == null
        assert response.redirectedUrl == '/experiment/list/'+erpAnalysisResult.experiment.id
    }
}
