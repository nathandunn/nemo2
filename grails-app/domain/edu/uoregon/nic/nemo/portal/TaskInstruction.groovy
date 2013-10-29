package edu.uoregon.nic.nemo.portal

class TaskInstruction extends Ontological{

    static hasMany = [
            conditions: Condition
    ]

    static belongsTo = [
            Condition
    ]

}
