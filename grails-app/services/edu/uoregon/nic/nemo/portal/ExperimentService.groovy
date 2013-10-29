package edu.uoregon.nic.nemo.portal

class ExperimentService {

    def replicateConditionParameters(Condition conditionFrom,Condition conditionTo) {

        Experiment experiment = conditionFrom.experiment

        conditionFrom.response.eachWithIndex { Response resp, iter ->
            Response response = new Response(resp.properties)
            response.identifier = experiment.laboratory.identifier + "Response-" + (iter as String)+"-"+ UUID.randomUUID().toString()
            response.condition = conditionTo
            response.save(flush: true, insert: true)
            conditionTo.addToResponse(response)
        }

        conditionFrom.stimulus.eachWithIndex { Stimulus existingStimulus, iter ->
            Stimulus newStimulus = new Stimulus(existingStimulus.properties)
            newStimulus.identifier = experiment?.laboratory?.identifier + "Stimulus-" + (iter as String) +"-"+ UUID.randomUUID().toString()
            newStimulus.condition = conditionTo

            newStimulus.targetQualities = null

            existingStimulus.targetQualities.each { quality ->
                newStimulus.addToTargetQualities(quality)
                quality.addToStimuli(existingStimulus)
            }


//            stimulus.targetModality = null
//            stimulus.targetType = null
//            stimulus.primeType = null
//            stimulus.primeModality = null
//            stimulus.primeQuality= null
//            stimulus.presentationDevice = null
//            stimulus.presentationSoftware = null


            newStimulus.save(flush: true, insert: true,failOnError: true)
            conditionTo.addToStimulus(newStimulus)
        }

        conditionTo.types = null
        conditionFrom.types.eachWithIndex { ConditionType type, iter ->
//            ConditionType conditionType = new ConditionType(type.properties)
            type.addToConditions(conditionTo)
            type.save(flush: true, insert: false,failOnError: true)
            conditionTo.addToTypes(type)
        }


    }
}
