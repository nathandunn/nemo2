package edu.uoregon.nic.nemo.portal

class Condition {

    static hasMany = [
            stimulus: Stimulus,
            response: Response,
            types: ConditionType,
    ]

    static belongsTo = [
        Experiment
    ]

    static constraints = {
        experiment nullable: false
        identifier unique: true, nullable: false,blank: false

        taskInstruction nullable: false
        unverifiedCopy nullable: false
    }

    String description
    Experiment experiment
    Date created_at
    Date updatedAt
    Boolean unverifiedCopy = false
    String identifier
    Integer numberTrials
    String experimentInstruction

    TaskInstruction taskInstruction
}
