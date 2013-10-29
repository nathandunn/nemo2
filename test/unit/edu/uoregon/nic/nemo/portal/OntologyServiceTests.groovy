package edu.uoregon.nic.nemo.portal

import grails.test.mixin.TestFor
import org.junit.Test
import org.semanticweb.owlapi.model.IRI

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(OntologyService)
class OntologyServiceTests {

    @Test
    void createOntologyService() {
        OntologyService ontologyService = new OntologyService()
    }

    @Test
    void createIRIFromString() {
        assertEquals(IRI.create(service.NS + "#NEMO_8913000").toQuotedString(), service.createIRIFromString("8913000").toQuotedString())
        assertEquals(IRI.create(service.NS + "#NEMO_8913000").toQuotedString(), service.createIRIFromString("#NEMO_8913000").toQuotedString())
        assertEquals(IRI.create(service.NS + "#NEMO_8913000").toQuotedString(), service.createIRIFromString(service.NS + "#NEMO_8913000").toQuotedString())
    }

    @Test
    void localFileTests() {
        assertEquals("/WEB-INF/files/", OntologyService.webInfFilesDir)
        assertEquals("./web-app/WEB-INF/files/ontology/", service.getLocalOntologiesDirectory())
//        assertEquals("./web-app/WEB-INF/files/rdf/", service.getLocalRdfDirectory())
        assertEquals("./web-app/WEB-INF/files/ontology/NEMO.owl", service.getLocalNemoOntologyFile())
        assertEquals(IRI.create(new File("./web-app/WEB-INF/files/ontology/NEMO.owl")), service.getLocalNemoIri())
    }

    @Test
    void testDateParsing(){
        String testString = '\"2011-11-05\"'
//        String s = testString.replaceAll("\"","")
        String s = testString.replaceAll (/"/, '')
//        s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()=="10"){
        println "should be a dash ${s.substring(4,5)=="-"}"
        println s.substring(7,8)=="-"
        println s.length()==10
        println (s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10)
        Calendar calendar = GregorianCalendar.instance

        Date date = service.parseDate(testString)
        assert date!=null
        calendar.setTime(date)

        println "year ${calendar.get(Calendar.YEAR)}"
        println "month ${calendar.get(Calendar.MONTH)}"
        println "day ${calendar.get(Calendar.DAY_OF_MONTH)}"

        assert calendar.get(Calendar.YEAR)==2011
        assert calendar.get(Calendar.MONTH)==10
        assert calendar.get(Calendar.DAY_OF_MONTH)==05
    }

    @Test
    void testDateParsing2(){
        String testString = 'http://www.w3.org/2001/XMLSchema#string2010-04-24'
//        String s = testString.replaceAll("\"","")
        String s = testString.replaceAll (/"/, '')
//        s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()=="10"){
        println "should be a dash ${s.substring(4,5)=="-"}"
        println s.substring(7,8)=="-"
        println s.length()==10
        println (s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10)
        Calendar calendar = GregorianCalendar.instance

        Date date = service.parseDate(testString)
        assert date!=null
        calendar.setTime(date)

        println "year ${calendar.get(Calendar.YEAR)}"
        println "month ${calendar.get(Calendar.MONTH)}"
        println "day ${calendar.get(Calendar.DAY_OF_MONTH)}"

        assert calendar.get(Calendar.YEAR)==2010
        assert calendar.get(Calendar.MONTH)==3
        assert calendar.get(Calendar.DAY_OF_MONTH)==24
    }

    @Test
    void testDateParsing3(){
        String testString = '""http://www.w3.org/2001/XMLSchema#string"2010-04-24"^^xsd:string'
//        String s = testString.replaceAll("\"","")
        String s = testString.replaceAll (/"/, '')
//        s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()=="10"){
        println "should be a dash ${s.substring(4,5)=="-"}"
        println s.substring(7,8)=="-"
        println s.length()==10
        println (s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10)
        Calendar calendar = GregorianCalendar.instance

        Date date = service.parseDate(testString)
        assert date!=null
        calendar.setTime(date)

        println "year ${calendar.get(Calendar.YEAR)}"
        println "month ${calendar.get(Calendar.MONTH)}"
        println "day ${calendar.get(Calendar.DAY_OF_MONTH)}"

        assert calendar.get(Calendar.YEAR)==2010
        assert calendar.get(Calendar.MONTH)==3
        assert calendar.get(Calendar.DAY_OF_MONTH)==24
    }

    @Test
    void testDateParsing4(){
        String testString = '"2010-07-27"^^xsd:string'
        '"2012-05-20"^^xsd:string'
//        String s = testString.replaceAll("\"","")
        String s = testString.replaceAll (/"/, '')
//        s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()=="10"){
        println "should be a dash ${s.substring(4,5)=="-"}"
        println s.substring(7,8)=="-"
        println s.length()==10
        println (s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10)
        Calendar calendar = GregorianCalendar.instance

        Date date = service.parseDate(testString)
        assert date!=null
        calendar.setTime(date)

        println "year ${calendar.get(Calendar.YEAR)}"
        println "month ${calendar.get(Calendar.MONTH)}"
        println "day ${calendar.get(Calendar.DAY_OF_MONTH)}"

        assert calendar.get(Calendar.YEAR)==2010
        assert calendar.get(Calendar.MONTH)==6
        assert calendar.get(Calendar.DAY_OF_MONTH)==27
    }

    @Test
    void testDateParsing5(){
        String testString = '"2012-05-20"^^xsd:string' // '"2010-07-27"^^xsd:string'

//        String s = testString.replaceAll("\"","")
        String s = testString.replaceAll (/"/, '')
//        s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()=="10"){
        println "should be a dash ${s.substring(4,5)=="-"}"
        println s.substring(7,8)=="-"
        println s.length()==10
        println (s.substring(4,5)=="-" && s.substring(7,8)=="-" && s.length()==10)
        Calendar calendar = GregorianCalendar.instance

        Date date = service.parseDate(testString)
        assert date!=null
        calendar.setTime(date)

        println "year ${calendar.get(Calendar.YEAR)}"
        println "month ${calendar.get(Calendar.MONTH)}"
        println "day ${calendar.get(Calendar.DAY_OF_MONTH)}"

        assert calendar.get(Calendar.YEAR)==2012
        assert calendar.get(Calendar.MONTH)==4
        assert calendar.get(Calendar.DAY_OF_MONTH)==20
    }
}
