package yzh.model;

import java.io.Serializable;
import java.util.Date;


public class ECSurface implements Serializable {
    private String stationid;

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

    private static final long serialVersionUID = 1L;

}