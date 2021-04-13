package yzh.数值预报处理.环境气象;

import java.util.Date;

public class EC高空Model {
    private String stationID;
    private Date Datetime;
    private Integer validTime;
    private Integer fcstLevel;
    private Double GPH;
    private Double TEM;
    private Double SHU;
    private Double WIU;
    private Double WIV;

    public EC高空Model(String stationID, Date datetime, Integer validTime, Integer fcstLevel, Double GPH, Double TEM, Double SHU, Double WIU, Double WIV) {
        this.stationID = stationID;
        Datetime = datetime;
        this.validTime = validTime;
        this.fcstLevel = fcstLevel;
        this.GPH = GPH;
        this.TEM = TEM;
        this.SHU = SHU;
        this.WIU = WIU;
        this.WIV = WIV;
    }

    public EC高空Model() {
    }

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

    public Integer getFcstLevel() {
        return fcstLevel;
    }

    public void setFcstLevel(Integer fcstLevel) {
        this.fcstLevel = fcstLevel;
    }

    public Double getGPH() {
        return GPH;
    }

    public void setGPH(Double GPH) {
        this.GPH = GPH;
    }

    public Double getTEM() {
        return TEM;
    }

    public void setTEM(Double TEM) {
        this.TEM = TEM;
    }

    public Double getSHU() {
        return SHU;
    }

    public void setSHU(Double SHU) {
        this.SHU = SHU;
    }

    public Double getWIU() {
        return WIU;
    }

    public void setWIU(Double WIU) {
        this.WIU = WIU;
    }

    public Double getWIV() {
        return WIV;
    }

    public void setWIV(Double WIV) {
        this.WIV = WIV;
    }
}
