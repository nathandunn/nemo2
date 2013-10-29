package edu.uoregon.nic.nemo.portal

class ConditionType extends Ontological {

    static hasMany = [
            conditions: Condition
    ]

    static belongsTo = [
            Condition
    ]
}
