package edu.uoregon.nic.nemo.portal

class ConditionService {

    def createIdentifierForCondition() {

    }

    def createIdentifierForStimulus(Stimulus stimulus) {
        String identifier = stimulus.condition.identifier + "StimType"
        while(Stimulus.findByIdentifier(identifier)){

            identifier += UUID.randomUUID().toString().substring(0,4)
        }
        return identifier
    }
}
