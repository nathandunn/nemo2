package edu.uoregon.nic.nemo.portal.client;

/**
 */
public enum BrainLocationEnum {

    // frontal
    LFTEMP(82, 60, 120, 100, "F7 cluster", "left fronto-temporal ROI", "NEMO_0000011"),
    LFRONT(133, 96, 159, 118, "F3 cluster", "left frontal ROI", "NEMO_0000013"),
    MFRONT(184, 100, 226, 131, "Fz cluster", "mid frontal ROI", "NEMO_0000014"),
    RFRONT(241, 92, 280, 120, "F4 cluster", "right frontal ROI", "NEMO_0000015"),
    RFTEMP(288, 74, 330, 100, "F8 cluster", "right fronto-temporal ROI", "NEMO_0000012"),

    LCTEMP(30, 160, 80, 180, "T7 cluster", "left centro-temporal ROI", "NEMO_5937000"),
    LCENT(108, 158, 145, 181, "C3 cluster", "left central ROI", "NEMO_2327000"),
    MCENT(175, 155, 232, 190, "Cz cluster", "mid central ROI", "NEMO_4013000"),
    RCENT(259, 155, 302, 190, "C4 cluster", "right central ROI", "NEMO_2471000"),
    RCTEMP(326, 153, 377, 187, "T8 cluster", "right centro-temporal ROI", "NEMO_6141000"),

    LPTEMP(46, 239, 89, 274, "P7 cluster", "left postero-temporal ROI", "NEMO_0000024"),
    LPAR(114, 232, 152, 262, "P3 cluster",  "left parietal ROI", "NEMO_0000021"),
    MPAR(177, 223, 233, 257, "Pz cluster",  "mid parietal ROI", "NEMO_0000022"),
    RPAR(262, 233, 300, 260, "P4 cluster", "right parietal ROI", "NEMO_0000023"),
    RPTEMP(320, 248, 365, 279, "P8 cluster", "right postero-temporal ROI", "NEMO_0000025"),


    LOTEMP(104, 309, 150, 342, "PO7 cluster","left occipito-temporal ROI","NEMO_0596000"),
    LOCC(141, 287, 180, 313, "O1 cluster", "left occipital ROI", "NEMO_0000016"),
    MOCC(185, 283, 227, 308, "Oz cluster", "mid occipital ROI", "NEMO_0000017"),
    ROCC(234, 287, 269, 309, "O2 cluster","right occipital ROI", "NEMO_0000279"),
    ROTEMP(267, 316, 306, 341, "PO8 cluster",  "right occipito-temporal ROI", "NEMO_9172000"),

    // TODO: remove
//    RATEMP,
    ;

    // add minx, miny, maxx, maxy

    private final int minX, minY, maxX, maxY;
    protected final String clusterName;
    protected final String longName;
    protected final String url;

    protected static final Integer standardWidth = 400 ;


    // upper-left coordinates, lower-right coordinates
    BrainLocationEnum(int minX, int minY, int maxX, int maxY, String clusterName, String longName, String url) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.clusterName = clusterName;
        this.longName = longName;
        this.url = url;
    }

    static BrainLocationEnum getEnumForString(String value) {
        for (BrainLocationEnum brainLocationEnum : BrainLocationEnum.values()) {
            if (brainLocationEnum.name().equalsIgnoreCase(value)) {
                return brainLocationEnum;
            }
        }
        return null;
    }



    static BrainLocationEnum getEnumForLocation(int x, int y) {
        return getEnumForLocation(x,y,BrainLocationEnum.standardWidth);
    }

    public static BrainLocationEnum getEnumForLocation(int x, int y, Integer imageWidth) {
//        float scale = (float) imageWidth / (float) standardWidth ;
        float scale = (float) standardWidth / (float) imageWidth;
        for (BrainLocationEnum brainLocationEnum : BrainLocationEnum.values()) {
            if (x*scale < brainLocationEnum.maxX && x*scale > brainLocationEnum.minX && y*scale < brainLocationEnum.maxY && y*scale > brainLocationEnum.minY) {
                return brainLocationEnum;
            }
        }
        return null;
    }

    public static BrainLocationEnum getEnumForName(String p) {
        for (BrainLocationEnum brainLocationEnum : BrainLocationEnum.values()) {
            if(p.equalsIgnoreCase(brainLocationEnum.name())) {
                return brainLocationEnum;
            }
        }
        return null;
    }
}