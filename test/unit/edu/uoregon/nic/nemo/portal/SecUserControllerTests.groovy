package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(SecUserController)
@Mock([Laboratory,SecUser, UserService, SecUserRole, Role])
class SecUserControllerTests extends AbstractUnitTest{


    def populateValidParams(params) {
        assert params != null
        params["username"] = getRandomEmail()
        params["password"] = getRandomString()
        params["password2"] = params.password
        params.level = "Commercial"
        params.firstname = "George"
        params.lastname = "TheMonkey"

    }

//    SecUser injectSecurityUser() {
//        SecUser secUser = new SecUser(
//                username: getRandomEmail()
//                , password: "secret"
//                , level: "Commercial"
//                , firstname: "George"
//                , lastname: "TheMonkey"
//        )
//                .save(failOnError: true, flush: true)
//        controller.springSecurityService = new StubSpringSecurityService()
//        controller.springSecurityService.setCurrentUser(secUser)
//        return secUser
//    }

    void testIndex() {
        controller.index()
        assert "/secUser/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.secUserInstanceList.size() == 0
        assert model.secUserInstanceTotal == 0
    }

    void testCreate() {
        controller.create()
        assert response.redirectedUrl == '/secUser/list'
    }

    void testSave() {

        injectSecurityUserAsAdmin(controller)

        controller.save()

        params.clear()

        assert model.secUserInstance != null
        assert view == '/secUser/create'


        response.reset()

        populateValidParams(params)
        controller.save()

        // 2 not 1 . . . because the first user is the admin
        assert response.redirectedUrl == '/secUser/show/1'
        assert controller.flash.message != null
        assert SecUser.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/secUser/list'


        populateValidParams(params)
        def secUser = new SecUser(params)

        assert secUser.save() != null

        params.id = secUser.id

        def model = controller.show()

        assert model.secUserInstance == secUser
    }

    void testEdit() {
        injectSecurityUser(controller)
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/secUser/list'


        populateValidParams(params)
        def secUser = new SecUser(params)

        assert secUser.save() != null

        params.id = secUser.id

        controller.edit()

        assert model.secUserInstance == secUser
    }

    void testUpdate() {
        SecUser secUser = injectSecurityUser(controller)
        secUser.save(failOnError: true,flush: true)

        // see bug: http://jira.grails.org/browse/GRAILS-7506
        SecUser.metaClass.isDirty = {
            return true
        }

//        SecUser secUser = injectSecurityUser()
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/secUser/list'

        response.reset()


        // we want to use the same user and simulate editing ourselves
        populateValidParams(params)
//        def secUser = new SecUser(params)

//        assert secUser.save() != null

        // test invalid parameters in update
        params.id = secUser.id
//        params.username = null

        // Can not use this here . . needs to be an integration test :
        // TODO: http://jira.grails.org/browse/GRAILS-8779

//        controller.update()
//
//        assert view == "/secUser/edit"
//        assert model.secUserInstance != null
//
//        secUser.clearErrors()

//        populateValidParams(params)
//        controller.update()
//
//        assert response.redirectedUrl == "/secUser/show/${secUser.id}"
//        assert flash.message != null
//
//        //test outdated version number
//        response.reset()
//        secUser.clearErrors()
//
//        populateValidParams(params)
//        params.id = secUser.id
//        params.version = -1
//        controller.update()
//
//        assert view == "/secUser/edit"
//        assert model.secUserInstance != null
//        assert model.secUserInstance.errors.getFieldError('version')
//        assert flash.message != null
    }

    void testDelete() {
        injectSecurityUserAsAdmin(controller)
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/secUser/list'

        response.reset()

        populateValidParams(params)
        def secUser = new SecUser(params)

        assert secUser.save() != null
        assert SecUser.count() == 1

        params.id = secUser.id

        controller.delete(secUser.id)

        assert SecUser.count() == 0
        assert SecUser.get(secUser.id) == null
        assert response.redirectedUrl == '/secUser/list'
    }
}
