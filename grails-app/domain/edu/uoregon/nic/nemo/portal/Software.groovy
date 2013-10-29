package edu.uoregon.nic.nemo.portal
/**
 * Should be all
 */

class Software extends Ontological{

    static hasMany = [
            stimuli: Stimulus,
            responses: Response,
            eegDataCollections: EegDataCollection
    ]

    static belongsTo = [
            Stimulus,EegDataCollection
    ]

}
