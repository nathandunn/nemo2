package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*
import org.uoregon.nemo.nic.QueryListEnum

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PatternController)
@Mock([OntologyService,ErpAnalysisResult,Ontology])
class PatternControllerTests {


    void testSomething() {
        controller.show()

        assert flash.message != null

        assert response.redirectedUrl == "/pattern/show/${QueryListEnum.MFN_LEXICALITY_EFFECT.url.substring(1)}"


        params.id = QueryListEnum.MFN_LEXICALITY_EFFECT.url.substring(1)

        def model = controller.show()

        assert model.id == params.id
        assert model.url == params.id
        assert model.label != "Not Found"
        assert model.instances != null
//        assert model.instances.size() > 0
        assert model.availableInstances != null
        assert model.availableInstances.size() > 0

        params.id = "abc123"

        model = controller.show()

        assert model.id == params.id
        assert model.url == params.id
        assert model.label == "Not Found"
        assert model.instances != null
        assert model.instances.size() == 0
        assert model.availableInstances != null
        assert model.availableInstances.size() > 0
    }
}
