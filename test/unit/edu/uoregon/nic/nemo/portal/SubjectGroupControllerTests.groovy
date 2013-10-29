package edu.uoregon.nic.nemo.portal

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(SubjectGroupController)
@Mock([SubjectGroup, Experiment, Laboratory, UserService, SecUser, SecUserRole])
class SubjectGroupControllerTests extends AbstractUnitTest {


    def populateValidParams(params) {
        assert params != null

        params.identifier = getRandomString()
        params.groupSize = 12
        params.genus = "Homo"
        params.species = "Sapiens"
        params.groupAge = 30

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

        params.experiment = experiment
        injectSecurityUser(controller)

    }

    void testIndex() {
        controller.index()
        assert "/subjectGroup/list" == response.redirectedUrl
    }

    void testList() {
        injectSecurityUser(controller)
        controller.list()

        assert model.subjectGroupInstanceList.size() == 0
        assert model.subjectGroupInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.subjectGroupInstance != null
    }

    void testSave() {
        controller.save()

        assert model.subjectGroupInstance != null
        assert view == '/subjectGroup/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/subjectGroup/show/1'
        assert controller.flash.message != null
        assert SubjectGroup.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/subjectGroup/list'


        populateValidParams(params)
        def subjectGroup = new SubjectGroup(params)

        assert subjectGroup.save() != null

        params.id = subjectGroup.id

        def model = controller.show()

        assert model.subjectGroupInstance == subjectGroup
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/subjectGroup/list'


        populateValidParams(params)
        def subjectGroup = new SubjectGroup(params)

        assert subjectGroup.save() != null

        params.id = subjectGroup.id

        def model = controller.edit()

        assert model.subjectGroupInstance == subjectGroup
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/subjectGroup/list'

        response.reset()


        populateValidParams(params)
        def subjectGroup = new SubjectGroup(params)

        assert subjectGroup.save() != null

        // test invalid parameters in update
        params.id = subjectGroup.id
        params.identifier = null

        controller.update()

        assert view == "/subjectGroup/edit"
        assert model.subjectGroupInstance != null

        subjectGroup.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/subjectGroup/show/$subjectGroup.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        subjectGroup.clearErrors()

        populateValidParams(params)
        params.id = subjectGroup.id
        params.version = -1
        controller.update()

        assert view == "/subjectGroup/edit"
        assert model.subjectGroupInstance != null
        assert model.subjectGroupInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/subjectGroup/list'

        response.reset()

        populateValidParams(params)
        def subjectGroup = new SubjectGroup(params)

        assert subjectGroup.save() != null
        assert SubjectGroup.count() == 1

        params.id = subjectGroup.id

        controller.delete()

        assert SubjectGroup.count() == 0
        assert SubjectGroup.get(subjectGroup.id) == null
        assert response.redirectedUrl == '/subjectGroup/list'
    }

    void testCopySubjectGroup() {
        assert 0 == SubjectGroup.count()
        controller.save()

        assert model.subjectGroupInstance != null
        assert view == '/subjectGroup/create'

        response.reset()
        assert 0 == SubjectGroup.count()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/subjectGroup/show/1'
        assert controller.flash.message != null
        assert SubjectGroup.count() == 1
        assert Experiment.count() == 1

        Experiment experiment = Experiment.get(1)
        params.id = experiment.id
        SubjectGroup oldSubjectGroup = SubjectGroup.get(1)
        params.subjectGroupId = oldSubjectGroup.id
        assert oldSubjectGroup.unverifiedCopy == false

        response.reset()
        assert 1 == SubjectGroup.count()
        controller.copySubjectGroup()
        assert 2 == SubjectGroup.count()

        assert response.redirectedUrl == null
        assert view=='/subjectGroup/edit'
        assert model!=null
        assert model.subjectGroupInstance!=null
        SubjectGroup subjectGroup = model.subjectGroupInstance
        assert subjectGroup.id == SubjectGroup.get(2)?.id
        assert subjectGroup.unverifiedCopy == true




    }
}
