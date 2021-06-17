package yzh.model;

import java.io.Serializable;
import java.util.Date;


public class ECSurface implements Serializable {
    private String stationid;

    public Double getTEM() {
        return TEM;
    }

    public void setTEM(Double TEM) {
        this.TEM = TEM;
    }

    public Double getPRS() {
        return PRS;
    }

    public void setPRS(Double PRS) {
        this.PRS = PRS;
    }

    public Double getWIU10() {
        return WIU10;
    }

    public void setWIU10(Double WIU10) {
        this.WIU10 = WIU10;
    }

    public Double getWIV10() {
        return WIV10;
    }

    public void setWIV10(Double WIV10) {
        this.WIV10 = WIV10;
    }

    public Double getDPT() {
        return DPT;
    }

    public void setDPT(Double DPT) {
        this.DPT = DPT;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getValidtime() {
        return validtime;
    }

    public void setValidtime(Integer validtime) {
        this.validtime = validtime;
    }

    public Integer getFcstlevel() {
        return fcstlevel;
    }

    public void setFcstlevel(Integer fcstlevel) {
        this.fcstlevel = fcstlevel;
    }

    public Double getTmax() {
        return tmax;
    }

    public void setTmax(Double tmax) {
        this.tmax = tmax;
    }

    public Double getTmin() {
        return tmin;
    }

    public void setTmin(Double tmin) {
        this.tmin = tmin;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ECSurface(String stationid, Date datetime, Integer validtime, Integer fcstlevel) {
        this.stationid = stationid;
        this.datetime = datetime;
        this.validtime = validtime;
        this.fcstlevel = fcstlevel;
    }

    private Date datetime;

    private Integer validtime;

    private Integer fcstlevel;

    private Double tmax;

    private Double tmin;

    private Double rain;
    private Double TEM;
    private Double PRS;
    private Double WIU10;
    private Double WIV10;
    private Double DPT;

    private static final long serialVersionUID = 1L;

}