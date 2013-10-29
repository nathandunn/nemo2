package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ResponseController)
@Mock([Response,Condition,SecUser,SecUserRole,UserService])
class ResponseControllerTests extends AbstractUnitTest {


    def populateValidParams(params) {
        assert params != null
        params.identifier = getRandomString()
        params.role = new ResponseRole()
        params.modality = new ResponseModality()

        injectSecurityUser(controller)
    }

    void testIndex() {
        controller.index()
        assert "/response/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.responseInstanceList.size() == 0
        assert model.responseInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.responseInstance != null
    }

    void testSave() {
        controller.save()

        assert model.responseInstance != null
        assert view == '/response/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/response/show/1'
        assert controller.flash.message != null
        assert Response.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/response/list'


        populateValidParams(params)
        def response = new Response(params)

        assert response.save() != null

        params.id = response.id

        def model = controller.show()

        assert model.responseInstance == response
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/response/list'


        populateValidParams(params)
        def response = new Response(params)

        assert response.save() != null

        params.id = response.id

        def model = controller.edit()

        assert model.responseInstance == response
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/response/list'

        response.reset()


        populateValidParams(params)
        def responseInstance = new Response(params)

        assert responseInstance.save() != null

        // test invalid parameters in update
        params.id = responseInstance.id
        params.identifier = null

        controller.update()

        assert view == "/response/edit"
        assert model.responseInstance != null

        responseInstance.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/response/show/$responseInstance.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        responseInstance.clearErrors()

        populateValidParams(params)
        params.id = responseInstance.id
        params.version = -1
        controller.update()

        assert view == "/response/edit"
        assert model.responseInstance != null
        assert model.responseInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/response/list'

        response.reset()

        populateValidParams(params)
        def responseInstance = new Response(params)

        assert responseInstance.save() != null
        assert Response.count() == 1

        params.id = responseInstance.id

        controller.delete()

        assert Response.count() == 0
        assert Response.get(responseInstance.id) == null
        assert response.redirectedUrl == '/response/list'
    }
}
