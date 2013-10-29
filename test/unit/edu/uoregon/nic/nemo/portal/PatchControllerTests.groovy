package edu.uoregon.nic.nemo.portal



import org.junit.*
import grails.test.mixin.*

@TestFor(PatchController)
@Mock(Patch)
class PatchControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/patch/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.patchInstanceList.size() == 0
        assert model.patchInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.patchInstance != null
    }

    void testSave() {
        controller.save()

        assert model.patchInstance != null
        assert view == '/patch/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/patch/show/1'
        assert controller.flash.message != null
        assert Patch.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/patch/list'


        populateValidParams(params)
        def patch = new Patch(params)

        assert patch.save() != null

        params.id = patch.id

        def model = controller.show()

        assert model.patchInstance == patch
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/patch/list'


        populateValidParams(params)
        def patch = new Patch(params)

        assert patch.save() != null

        params.id = patch.id

        def model = controller.edit()

        assert model.patchInstance == patch
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/patch/list'

        response.reset()


        populateValidParams(params)
        def patch = new Patch(params)

        assert patch.save() != null

        // test invalid parameters in update
        params.id = patch.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/patch/edit"
        assert model.patchInstance != null

        patch.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/patch/show/$patch.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        patch.clearErrors()

        populateValidParams(params)
        params.id = patch.id
        params.version = -1
        controller.update()

        assert view == "/patch/edit"
        assert model.patchInstance != null
        assert model.patchInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/patch/list'

        response.reset()

        populateValidParams(params)
        def patch = new Patch(params)

        assert patch.save() != null
        assert Patch.count() == 1

        params.id = patch.id

        controller.delete()

        assert Patch.count() == 0
        assert Patch.get(patch.id) == null
        assert response.redirectedUrl == '/patch/list'
    }
}
