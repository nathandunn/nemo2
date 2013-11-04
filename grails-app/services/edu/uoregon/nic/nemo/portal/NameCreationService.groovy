package edu.uoregon.nic.nemo.portal

class NameCreationService {

    def createIdentifierForCondition(Experiment experiment) {
        String origIdentifier = experiment.identifier + "Cond"
        String identifier = origIdentifier
        Integer counter = 1
        while(Condition.findByIdentifier(identifier)){
            identifier = origIdentifier + counter.toString().padLeft(2,"0")
            ++counter
        }
        return identifier
    }

    def createIdentifierForStimulus(Stimulus stimulus) {
        String origIdentifier = stimulus.condition.identifier + "Stim"
        String identifier = origIdentifier
        Integer counter = 1
        while(Stimulus.findByIdentifier(identifier)){
            identifier = origIdentifier + counter.toString().padLeft(2,"0")
            ++counter
        }
        return identifier
    }

    String createStimulusIdentifierForCondition(Condition condition) {
        String origIdentifier = condition.identifier + "Stim"
        String identifier = origIdentifier
        Integer counter = 1
        while(Stimulus.findByIdentifier(identifier)){
            identifier = origIdentifier + counter.toString().padLeft(2,"0")
            ++counter
        }
        return identifier
    }

    String createIdentifierForSubjectGroup(Experiment experiment) {
        String origIdentifier = experiment.identifier + "Subj"
        String identifier = origIdentifier
        Integer counter = 1
        while(SubjectGroup.findByIdentifier(identifier)){
            identifier = origIdentifier + counter.toString().padLeft(2,"0")
            ++counter
        }
        return identifier
    }
}
