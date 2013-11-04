package edu.uoregon.nic.nemo.portal

class ConditionService {

    def createIdentifierForCondition() {

    }

    def createIdentifierForStimulus(Stimulus stimulus) {
        String origIdentifier = stimulus.condition.identifier + "Stim"
        String identifier = origIdentifier
        while(Stimulus.findByIdentifier(identifier)){
            identifier = origIdentifier + UUID.randomUUID().toString().substring(0,4)
        }
        return identifier
    }
}
