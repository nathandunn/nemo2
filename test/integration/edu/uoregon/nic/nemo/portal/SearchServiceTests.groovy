package edu.uoregon.nic.nemo.portal

import org.junit.*

class SearchServiceTests {

    def searchService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSearch() {
        String resultJSON = searchService.searchErpsRaw(400,600,null)
        println "resultJSON ${resultJSON}"
    }

//    @Test
//    void testSearchIndividuals() {
//        String resultJSON = searchService.searchErps(400,600,null)
//        println "resultJSON ${resultJSON}"
//    }
}
