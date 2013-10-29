package edu.uoregon.nic.nemo.portal

class CleaningTransformation extends Ontological {


    static hasMany = [
            erpDataPreprocessings: ErpDataPreprocessing
    ]

    static belongsTo = [
            ErpDataPreprocessing
    ]

}
