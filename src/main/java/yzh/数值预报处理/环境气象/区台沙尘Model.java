package yzh.数值预报处理.环境气象;

import java.util.Date;

public class 区台沙尘Model {
    private String stationID;
    private Date Datetime;

    public 区台沙尘Model(String stationID, Date datetime, Integer validTime, String fcstName, Double fcstLevel, Double fcstValue) {
        this.stationID = stationID;
        Datetime = datetime;
        this.validTime = validTime;
        this.fcstName = fcstName;
        this.fcstLevel = fcstLevel;
        this.fcstValue = fcstValue;
    }

    private Integer validTime;

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public Date getDatetime() {
        return Datetime;
    }

    public void setDatetime(Date datetime) {
        Datetime = datetime;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public String getFcstName() {
        return fcstName;
    }

    public void setFcstName(String fcstName) {
        this.fcstName = fcstName;
    }

    public Double getFcstLevel() {
        return fcstLevel;
    }

    public void setFcstLevel(Double fcstLevel) {
        this.fcstLevel = fcstLevel;
    }

    public Double getFcstValue() {
        return fcstValue;
    }

    public void setFcstValue(Double fcstValue) {
        this.fcstValue = fcstValue;
    }

    private String fcstName;
    private Double fcstLevel;
    private Double fcstValue;
}
