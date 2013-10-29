package edu.uoregon.nic.nemo.portal

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class UpdateOntologiesJobTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Ignore("Takes too long")
    void testSomething() {
        UpdateOntologiesJob updateOntologiesJob = new UpdateOntologiesJob()
        updateOntologiesJob.ontologyService =new OntologyService()
        updateOntologiesJob.execute()
    }
}
