package model;

import cn.hutool.core.date.DateTime;

public class 区台格点数值预报站点Model {
    private String ID;
    private DateTime MyDate;
    private int purpose;
    private int level;

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public 区台格点数值预报站点Model(String ID, DateTime myDate, int SX) {
        this.ID = ID;
        MyDate = myDate;
        this.SX = SX;
    }

    public 区台格点数值预报站点Model(String ID, DateTime myDate, int SX, double TEM, double TMAX, double TMIN, double WIU10, double WIV10, double ERH) {
        this.ID = ID;
        MyDate = myDate;
        this.SX = SX;
        this.TEM = TEM;
        this.TMAX = TMAX;
        this.TMIN = TMIN;
        this.WIU10 = WIU10;
        this.WIV10 = WIV10;
        this.ERH = ERH;
    }

    private int SX;

    public 区台格点数值预报站点Model(String ID, DateTime myDate, int SX, double PRE_3h, double TEM, double TMAX, double TMIN, double WIU10, double WIV10, double ERH) {
        this.ID = ID;
        MyDate = myDate;
        this.SX = SX;
        this.PRE_3h = PRE_3h;
        this.TEM = TEM;
        this.TMAX = TMAX;
        this.TMIN = TMIN;
        this.WIU10 = WIU10;
        this.WIV10 = WIV10;
        this.ERH = ERH;
    }

    private double PRE_3h=-999999;
    private double TEM=-999999;
    private double TMAX=-999999;
    private double TMIN=-999999;
    private double WIU10=-999999;
    private double WIV10=-999999;
    private double ERH=-999999;
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public DateTime getMyDate() {
        return MyDate;
    }

    public void setMyDate(DateTime myDate) {
        MyDate = myDate;
    }

    public int getSX() {
        return SX;
    }

    public void setSX(int SX) {
        this.SX = SX;
    }

    public double getPRE_3h() {
        return PRE_3h;
    }

    public void setPRE_3h(double PRE_3h) {
        this.PRE_3h = PRE_3h;
    }

    public double getTEM() {
        return TEM;
    }

    public void setTEM(double TEM) {
        this.TEM = TEM;
    }

    public double getTMAX() {
        return TMAX;
    }

    public void setTMAX(double TMAX) {
        this.TMAX = TMAX;
    }

    public double getTMIN() {
        return TMIN;
    }

    public void setTMIN(double TMIN) {
        this.TMIN = TMIN;
    }

    public double getWIU10() {
        return WIU10;
    }

    public void setWIU10(double WIU10) {
        this.WIU10 = WIU10;
    }

    public double getWIV10() {
        return WIV10;
    }

    public void setWIV10(double WIV10) {
        this.WIV10 = WIV10;
    }

    public double getERH() {
        return ERH;
    }

    public void setERH(double ERH) {
        this.ERH = ERH;
    }


}
