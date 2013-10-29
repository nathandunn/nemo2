package edu.uoregon.nic.nemo.portal

import grails.test.mixin.*
import grails.test.mixin.support.*

import edu.uoregon.nemo.nic.portal.util.CollectionParser

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class CollectionParserTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testParse(){
        String inputString = " ['<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_8461000>', '<http://purl.bioo tology.org/NEMO/ontology/NEMO.owl#NEMO_3483000>'," +
                "  '<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1715000>', '<http://purl.bioontology org/NEMO/ontology/NEMO.owl#NEMO_1985000>']"

        def inputs = CollectionParser.parseStringAsArray(inputString){ input ->
            input.replaceAll("\\[|\\]|'","")
        }

        assertEquals(4,inputs.size())

    }


    void testParse2(){
        def list = []
        list.add("A")
        list.add("B")
        list.add("C")
        list.add("D")

        def listString = list as String
//        println  CollectionParser.parseStringAsArray(listString){ input ->
//            input.replaceAll("\\[|\\]|'","")
//        }

        def list2 = listString as List
    }


    void testMapCollection1(){

        String def2 = "{A=[1,2,3],B=[7,8,9 ], C =[10,11,12]}"

        Map<String,List<String>> map = CollectionParser.parseStringAsMap(def2)
        assert map.keySet().size()==3
    }

    void testMapCollection2(){
        String def1 = "{<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_9669000>="+\
                      "[<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0290000>,"+\
                      "<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0541000>,"+\
                        "<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0622000>],"
        def1 += "<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1634000>="+\
                "[<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0290000>,"+\
                "<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0541000>, "+\
                 "<http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_0622000>]}"
        Map<String,List<String>> map = CollectionParser.parseStringAsMap(def1){ input ->
            input.replaceAll("<|>|'| ","")
        }
        assert map.keySet().size()==2
        Set<String> keys = map.keySet()
        for(key in keys){
            assert map.get(key).size() ==3
        }

    }
}
