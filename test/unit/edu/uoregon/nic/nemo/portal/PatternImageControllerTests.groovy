package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(PatternImageController)
@Mock([PatternImage,ErpAnalysisResult,DataFormat,DataSet])
class PatternImageControllerTests extends AbstractUnitTest {


    def populateValidParams(params) {
        assert params != null
        ErpAnalysisResult erpAnalysisResult = new ErpAnalysisResult(
                artifactFileName: getRandomString()
                ,set: new DataSet()
                ,format: new DataFormat()
        )
        .save(failOnError: true)
        params.patternName = getRandomString()
        params.image = new byte[12]
        params.rawImage = new byte[12]
        params.erpAnalysisResult = erpAnalysisResult
    }

    void testIndex() {
        controller.index()
        assert "/patternImage/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.patternImageInstanceList.size() == 0
        assert model.patternImageInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.patternImageInstance != null
    }

    void testSave() {
        controller.save()

        assert model.patternImageInstance != null
        assert view == '/patternImage/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/patternImage/show/1'
        assert controller.flash.message != null
        assert PatternImage.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/patternImage/list'


        populateValidParams(params)
        def patternImage = new PatternImage(params)

        assert patternImage.save() != null

        params.id = patternImage.id

        def model = controller.show()

        assert model.patternImageInstance == patternImage
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/patternImage/list'


        populateValidParams(params)
        def patternImage = new PatternImage(params)

        assert patternImage.save() != null

        params.id = patternImage.id

        def model = controller.edit()

        assert model.patternImageInstance == patternImage
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/patternImage/list'

        response.reset()


        populateValidParams(params)
        def patternImage = new PatternImage(params)

        assert patternImage.save() != null

        // test invalid parameters in update
        params.id = patternImage.id
        params.patternName = null

        controller.update()

        assert view == "/patternImage/edit"
        assert model.patternImageInstance != null

        patternImage.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/patternImage/show/$patternImage.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        patternImage.clearErrors()

        populateValidParams(params)
        params.id = patternImage.id
        params.version = -1
        controller.update()

        assert view == "/patternImage/edit"
        assert model.patternImageInstance != null
        assert model.patternImageInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/patternImage/list'

        response.reset()

        populateValidParams(params)
        def patternImage = new PatternImage(params)

        assert patternImage.save() != null
        assert PatternImage.count() == 1

        params.id = patternImage.id

        controller.delete()

        assert PatternImage.count() == 0
        assert PatternImage.get(patternImage.id) == null
        assert response.redirectedUrl == '/patternImage/list'
    }
}
