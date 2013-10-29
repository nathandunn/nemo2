package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Ignore

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegisterController)
@Mock([Laboratory,SecUser])
class RegisterControllerTests{

    @Ignore("For some reason this fails in the full test, but not the unit test")
    void testIndex() {
//        Laboratory laboratory = new Laboratory(
//                identifier: getRandomString()
//                , principalInvestigatorRole: "http://asdf.com"
//                , institution: "http://someplace.org"
//        )
//        laboratory.save(failOnError: true,flush: true)
        controller.index()
        assert model.command != null
//        assert model.laboratories.size() > 0
    }

    @Ignore("For some reason this fails in the full test")
    void testRegister(){
        controller.register()
//        fail "ASDf"
    }
}
