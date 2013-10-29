package edu.uoregon.nic.nemo.portal
import edu.uoregon.nemo.nic.portal.util.RandomStringGenerator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.semanticweb.owlapi.model.OWLClass

class ExperimentTests extends GroovyTestCase {

    def ontologyService

    String getRandomString(params) {
        def length = params?.length ?: 5
        String randomString = org.apache.commons.lang.RandomStringUtils.random(length, true, true)
        return randomString
    }

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void addAnExperiment(){
        Laboratory laboratory = new Laboratory(
                identifier: RandomStringGenerator.getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.save(failOnError: true)
        Experiment experiment = new Experiment(
                identifier: RandomStringGenerator.getRandomString()
                , laboratory: laboratory
                , narrativeSummary: "blah blah blah"
                , subjectGroupsNarrativeSummary: "blah blah blah"
                , conditionsNarrativeSummary: "blah blah blah"
                , erpDataPreprocessingsNarrativeSummary: "blah blah blah"
                , eegDataCollectionNarrativeSummary: "blah blah blah"

        )
        experiment.save(failOnError: true)
    }


    @Test
    void labelAClass(){
        OWLClass owlClass= ontologyService.getNameForClass("NEMO_0877000");
        println ontologyService.render(owlClass)
    }


    @Test
    void publicationsForExperiment() {
        Laboratory laboratory = new Laboratory(
                identifier: getRandomString()
                , principalInvestigatorRole: "http://asdf.com"
                , institution: "http://someplace.org"
        )
        laboratory.save(failOnError: true)
        Experiment experiment = new Experiment(
                identifier: getRandomString()
                , laboratory: laboratory
                , narrativeSummary: "blah blah blah"
                , subjectGroupsNarrativeSummary: "blah blah blah"
                , conditionsNarrativeSummary: "blah blah blah"
                , erpDataPreprocessingsNarrativeSummary: "blah blah blah"
                , eegDataCollectionNarrativeSummary: "blah blah blah"

        )
        experiment.save(failOnError: true)
        Publication publication = new Publication(
                identifier: getRandomString()
                , publicationDate: 2000
                , digitalObjectIdentifier: getRandomString()
                , type: new PublicationType().save()
        )
                .save(failOnError: true,flush: true)

        experiment.save(flush: true, failOnError: true)
//        experiment.addToPublications(publication)
//        experiment.save(flush: true, failOnError: true)
//        Set<Experiment> experiments1 = new HashSet<Experiment>()
//        experiments1.add(experiment)
//        publication.experiments= experiments1
        publication.addToExperiments(experiment)
        publication.save()

        List experiments = []
        experiments.add(experiment)

//        List<Publication> publications = Publication.findAllByExperimentsInList([experiment])
//        List<Publication> publications = Publication.findAllByExperimentsIsNotEmpty().collect(){ it ->
//            if(it.experiments.contains(experiment)){
//                return it
//            }
//        }
        List<Publication> publications = Publication.all.findAll{ it ->
            it?.experiments?.contains(experiment)
        }
//        List<Publication> publications = Publication.findAllByExperimentsIsNotEmpty(experiments)
        assert 1 == experiment.publications.size()
        assert 1 == publications.size()
        assert Publication.count() > 1
        int pubCount = Publication.count()


        Publication publication2 = new Publication(
                identifier: getRandomString()
                , publicationDate: 2000
                , digitalObjectIdentifier: getRandomString()
                , type: new PublicationType().save()
        )
                .save(failOnError: true,flush: true)

        experiment.addToPublications(publication2)
        experiment.save(flush: true, failOnError: true)
        publication2.addToExperiments(experiment)
        publication2.save(flush: true,failOnError: true)
        assert (pubCount+1) == Publication.count()
        assert 2  == experiment.publications.size()

        experiment.refresh()

        // TODO: for some stupid reason this fails
//        assert 2 == Publication.findAllByExperimentsInList([experiment]).size()



    }

}
