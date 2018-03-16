package com.berry_med.monitordemo.data;

/**
 * Created by Wordh on 2018/3/07.
 */

public class NIBP {
    public int HIGH_PRESSURE_INVALID = 0;
    public int LOW_PRESSURE_INVALID  = 0;

    private int highPressure;
    private int meanPressure;
    private int lowPressure;
    private int cuffPressure;
    private int status;

    public NIBP(int highPressure, int meanPressure, int lowPressure, int cuffPressure, int status) {
        this.highPressure = highPressure;
        this.meanPressure = meanPressure;
        this.lowPressure = lowPressure;
        this.cuffPressure = cuffPressure;
        this.status = status;

    }

    public int getMeanPressure() {
        AllValues av = new AllValues();
        av.setMeanPressure(meanPressure);
        return meanPressure;
    }

    public int getHighPressure() {
        AllValues av = new AllValues();
        av.setHighPressure(highPressure);
        return highPressure;
    }

    public int getLowPressure() {
        AllValues av = new AllValues();
        av.setLowPressure(lowPressure);
        return lowPressure;
    }

    public int getCuffPressure() {
        return cuffPressure;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return  "Cuff:" +  (cuffPressure!=0 ? cuffPressure: "- -") + "\r\n" +
                "High:"+  (highPressure!=0 ? highPressure: "- -") +
                " Low:" +  (lowPressure !=0 ? lowPressure : "- -") +
                " Mean:"+  (meanPressure!=0 ? meanPressure: "- -");
    }
}
