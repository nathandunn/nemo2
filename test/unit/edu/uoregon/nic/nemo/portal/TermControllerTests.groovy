package edu.uoregon.nic.nemo.portal

import edu.uoregon.nemo.nic.portal.util.TermView
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.uoregon.nemo.nic.QueryListEnum

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(TermController)
@Mock([Ontology, OntologyService])
class TermControllerTests {

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectUrl == "/term/list"

        // TODO: not yet implemented for ontological Classes:
//        http://jira.grails.org/browse/GRAILS-8688?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
//        params.id = QueryListEnum.MFN_LEXICALITY_EFFECT.url
//        def model = controller.show()
//        model.termView != null
//        model.related != null
//
//        params.id = "garbage"
//        model = controller.show()
//        model.termView == null
//        model.related != null

    }

}
