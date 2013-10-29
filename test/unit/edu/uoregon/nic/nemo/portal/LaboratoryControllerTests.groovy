package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import edu.uoregon.nemo.nic.portal.util.StubSpringSecurityService
import grails.test.mixin.*

@TestFor(LaboratoryController)
@Mock([Experiment, Institution, UserService,SecUser,SecUserRole,Laboratory])
class LaboratoryControllerTests {

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

    def populateValidParams(params) {
      assert params != null
        params.identifier = RandomStringGenerator.getRandomString()
        params.principalInvestigatorRole = "http://asdf.com"
        params.institution = "http://someplace.org"
    }

    void testIndex() {
        controller.index()
        assert "/laboratory/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.laboratoryInstanceList.size() == 0
        assert model.laboratoryInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.laboratoryInstance != null
    }

    void testSave() {
        controller.save()

        assert model.laboratoryInstance != null
        assert view == '/laboratory/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/laboratory/show/1'
        assert controller.flash.message != null
        assert Laboratory.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/laboratory/list'


        populateValidParams(params)
        def laboratory = new Laboratory(params)

        assert laboratory.save() != null

        params.id = laboratory.id

        def model = controller.show()

        assert model.laboratoryInstance == laboratory
    }

    void testEdit() {
        injectSecurityUser()
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/laboratory/list'


        populateValidParams(params)
        def laboratory = new Laboratory(params)

        assert laboratory.save() != null

        params.id = laboratory.id

        def model = controller.edit()

        assert model.laboratoryInstance == laboratory
    }

    void testUpdate() {
        injectSecurityUser()
        controller.update(0)

        assert flash.message != null
        assert response.redirectedUrl == '/laboratory/list'

        response.reset()


        populateValidParams(params)
        def laboratory = new Laboratory(params)

        assert laboratory.save() != null

        // test invalid parameters in update
        params.id = laboratory.id

        params.identifier = null

        controller.update(laboratory.id)

        assert view == "/laboratory/edit"
        assert model.laboratoryInstance != null

        laboratory.clearErrors()

        populateValidParams(params)
        controller.update(laboratory.id)

        assert response.redirectedUrl == "/laboratory/show/$laboratory.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        laboratory.clearErrors()

        populateValidParams(params)
        params.id = laboratory.id
        params.version = -1
        controller.update(laboratory.id)

        assert view == "/laboratory/edit"
        assert model.laboratoryInstance != null
        assert model.laboratoryInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        injectSecurityUser()
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/laboratory/list'

        response.reset()

        populateValidParams(params)
        def laboratory = new Laboratory(params)

        assert laboratory.save() != null
        assert Laboratory.count() == 1

        params.id = laboratory.id

        controller.delete()

        assert Laboratory.count() == 0
        assert Laboratory.get(laboratory.id) == null
        assert response.redirectedUrl == '/laboratory/list'
    }
}
