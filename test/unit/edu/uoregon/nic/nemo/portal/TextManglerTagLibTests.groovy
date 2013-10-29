package edu.uoregon.nic.nemo.portal



import grails.test.mixin.*

import edu.uoregon.nic.nemo.portal.TextManglerTagLib

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(TextManglerTagLib)
class TextManglerTagLibTests {

    void testSomething() {
        String tagText = "<g:toggleTextLength input='longishtext'/>"
        assertEquals("longishtext",applyTemplate(tagText))
        tagText = "<g:toggleTextLength input='longishtext' maxLength='3'/>"
        assertTrue(applyTemplate(tagText).contains(">lon&nbsp;"))
    }
}
