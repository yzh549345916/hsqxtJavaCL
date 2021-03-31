package model;

import cn.hutool.core.date.DateTime;

public class 区台数值预报数据Model {
    private String ID;
    private String Name;

    public 区台数值预报数据Model(String ID, String name, DateTime time, DateTime baseTime, int SX, String dataType, Double forecastValue) {
        this.ID = ID;
        Name = name;
        Time = time;
        BaseTime = baseTime;
        this.SX = SX;
        DataType = dataType;
        ForecastValue = forecastValue;
    }

    private DateTime Time;

    public DateTime getBaseTime() {
        return BaseTime;
    }

    public void setBaseTime(DateTime baseTime) {
        BaseTime = baseTime;
    }

    private DateTime BaseTime;
    private int SX;



    public int getSX() {
        return SX;
    }

    public void setSX(int SX) {
        this.SX = SX;
    }

    private String DataType;
    private Double ForecastValue;

    public 区台数值预报数据Model(String id, String dataType) {
        ID = id;
        DataType = dataType;
    }

    public 区台数值预报数据Model(String ID, String name, DateTime time, String dataType, Double forecastValue) {
        this.ID = ID;
        Name = name;
        Time = time;
        DataType = dataType;
        ForecastValue = forecastValue;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public DateTime getTime() {
        return Time;
    }

    public void setTime(DateTime time) {
        Time = time;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public Double getForecastValue() {
        return ForecastValue;
    }

    public void setForecastValue(Double forecastValue) {
        ForecastValue = forecastValue;
    }
}
