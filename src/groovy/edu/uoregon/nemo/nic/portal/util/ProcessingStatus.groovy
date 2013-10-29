package edu.uoregon.nemo.nic.portal.util

/**
 */
public enum ProcessingStatus {

    DONE(0),
    IN_PROCESS(1),
    ERROR(2),
    UNPROCESSED(3),

    private final Integer value

    ProcessingStatus(Integer value){
        this.value = value
    }

    Integer value(){
        return value
    }

    static ProcessingStatus getTypeForValue(Integer value){
        for (processingStatus in ProcessingStatus.values()){
            if(value==processingStatus.toString() as Integer){
                return processingStatus
            }
        }
        throw new RuntimeException("Failed to retrieve a status for value: " + value)
    }
}