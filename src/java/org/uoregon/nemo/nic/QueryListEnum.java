package org.uoregon.nemo.nic;

/**
 * Provides the list of queries that are easy to look up.
 */
public enum QueryListEnum {

    MFN_LEXICALITY_EFFECT("#NEMO_3056000"),
    SCALP_RECORDED_ERP_DIFFWAVE_COMPONENT("#NEMO_0877000"),
    STATISTICALLY_SIGNIFICANT("#NEMO_9658000"),
    HAS_NUMERIC_VALUE("#NEMO_7943000"),
    MEAN_INTENSITY_MEASUREMENT_DATUM("#NEMO_9201000"),
    ;


    private String url ;

    private QueryListEnum(String newUrl){
        url = newUrl ;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getUrl();
    }

}
