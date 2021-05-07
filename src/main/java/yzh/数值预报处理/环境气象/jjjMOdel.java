package yzh.数值预报处理.环境气象;


import java.util.Date;

public class jjjMOdel {
    private String valueName;
    private Date myDate;
    private Double value;
    private String stationName;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public jjjMOdel(String valueName, Date myDate, Double value, String stationName) {
        this.valueName = valueName;
        this.myDate = myDate;
        this.value = value;
        this.stationName = stationName;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Date getMyDate() {
        return myDate;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public jjjMOdel() {
    }

    public jjjMOdel(String valueName, Date myDate, Double value) {
        this.valueName = valueName;
        this.myDate = myDate;
        this.value = value;
    }
}
