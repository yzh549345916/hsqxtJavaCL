package yzh.数值预报处理.环境气象;

import java.util.Date;

public class 高空要素Model {
    private Integer validTime;
    private Integer level;
    private Double ysValue;
    private String stationID;
    private Date Datetime;

    public Date getDatetime() {
        return Datetime;
    }

    public 高空要素Model(Integer validTime, Integer level, Double ysValue, String stationID, Date datetime) {
        this.validTime = validTime;
        this.level = level;
        this.ysValue = ysValue;
        this.stationID = stationID;
        Datetime = datetime;
    }

    public void setDatetime(Date datetime) {
        Datetime = datetime;
    }

    public 高空要素Model(Integer validTime, Integer level, Double ysValue, String stationID) {
        this.validTime = validTime;
        this.level = level;
        this.ysValue = ysValue;
        this.stationID = stationID;
    }

    public 高空要素Model() {
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getYsValue() {
        return ysValue;
    }

    public void setYsValue(Double ysValue) {
        this.ysValue = ysValue;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }
}
