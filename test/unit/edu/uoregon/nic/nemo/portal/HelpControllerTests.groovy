package edu.uoregon.nic.nemo.portal



import org.junit.*
import grails.test.mixin.*

@TestFor(HelpController)
@Mock(Help)
class HelpControllerTests {


//    def populateValidParams(params) {
//      assert params != null
//      // TODO: Populate valid properties like...
//      //params["name"] = 'someValidName'
//    }
//
    void testIndex() {
        controller.index()
        assert "/help/list" == response.redirectedUrl
    }
//
//    void testList() {
//
//        def model = controller.list()
//
//        assert model.helpInstanceList.size() == 0
//        assert model.helpInstanceTotal == 0
//    }
//
//    void testCreate() {
//       def model = controller.create()
//
//       assert model.helpInstance != null
//    }
//
//    void testSave() {
//        controller.save()
//
//        assert model.helpInstance != null
//        assert view == '/help/create'
//
//        response.reset()
//
//        populateValidParams(params)
//        controller.save()
//
//        assert response.redirectedUrl == '/help/show/1'
//        assert controller.flash.message != null
//        assert Help.count() == 1
//    }
//
//    void testShow() {
//        controller.show()
//
//        assert flash.message != null
//        assert response.redirectedUrl == '/help/list'
//
//
//        populateValidParams(params)
//        def help = new Help(params)
//
//        assert help.save() != null
//
//        params.id = help.id
//
//        def model = controller.show()
//
//        assert model.helpInstance == help
//    }
//
//    void testEdit() {
//        controller.edit()
//
//        assert flash.message != null
//        assert response.redirectedUrl == '/help/list'
//
//
//        populateValidParams(params)
//        def help = new Help(params)
//
//        assert help.save() != null
//
//        params.id = help.id
//
//        def model = controller.edit()
//
//        assert model.helpInstance == help
//    }
//
//    void testUpdate() {
//        controller.update()
//
//        assert flash.message != null
//        assert response.redirectedUrl == '/help/list'
//
//        response.reset()
//
//
//        populateValidParams(params)
//        def help = new Help(params)
//
//        assert help.save() != null
//
//        // test invalid parameters in update
//        params.id = help.id
//        params.email = 'badeamil'
//
//        controller.update()
//
//        assert view == "/help/edit"
//        assert model.helpInstance != null
//
//        help.clearErrors()
//
//        populateValidParams(params)
//        controller.update()
//
//        assert response.redirectedUrl == "/help/show/$help.id"
//        assert flash.message != null
//
//        //test outdated version number
//        response.reset()
//        help.clearErrors()
//
//        populateValidParams(params)
//        params.id = help.id
//        params.version = -1
//        controller.update()
//
//        assert view == "/help/edit"
//        assert model.helpInstance != null
//        assert model.helpInstance.errors.getFieldError('version')
//        assert flash.message != null
//    }
//
//    void testDelete() {
//        controller.delete()
//        assert flash.message != null
//        assert response.redirectedUrl == '/help/list'
//
//        response.reset()
//
//        populateValidParams(params)
//        def help = new Help(params)
//
//        assert help.save() != null
//        assert Help.count() == 1
//
//        params.id = help.id
//
//        controller.delete()
//
//        assert Help.count() == 0
//        assert Help.get(help.id) == null
//        assert response.redirectedUrl == '/help/list'
//    }
}
