package model;

import cn.hutool.core.date.DateTime;

public class Rmaps数值预报站点Model {
    private String ID;
    private DateTime MyDate;
    private int SX;
    private double PRE=-999999;
    private double TEM=-999999;
    private double TEM_surface=-999999;
    private double RHU=-999999;
    private double PRS=-999999;
    private double WIU10=-999999;
    private double WIV10=-999999;
    private double VIS=-999999;
    private double Low_cloud=-999999;
    private double Medium_cloud=-999999;
    private double High_cloud=-999999;
    private double TEM_isobaric_500=-999999;
    private double TEM_isobaric_700=-999999;
    private double TEM_isobaric_850=-999999;
    private double RHU_isobaric_500=-999999;
    private double RHU_isobaric_700=-999999;
    private double RHU_isobaric_850=-999999;
    private double WIU10_isobaric_500=-999999;
    private double WIU10_isobaric_700=-999999;
    private double WIU10_isobaric_850=-999999;
    private double WIV10_isobaric_500=-999999;
    private double WIV10_isobaric_700=-999999;
    private double WIV10_isobaric_850=-999999;
    private double Geopotential_height_500=-999999;
    private double Geopotential_height_700=-999999;
    private double Geopotential_height_850=-999999;

    public Rmaps数值预报站点Model() {
    }

    public Rmaps数值预报站点Model(Rmaps数值预报站点Model mydata,double PRE) {
        this.ID = mydata.ID;
        MyDate = mydata.MyDate;
        this.SX = mydata.SX;
        this.PRE = PRE;
        this.TEM = mydata.TEM;
        this.TEM_surface = mydata.TEM_surface;
        this.RHU = mydata.RHU;
        this.PRS = mydata.PRS;
        this.WIU10 = mydata.WIU10;
        this.WIV10 = mydata.WIV10;
        this.VIS = mydata.VIS;
        Low_cloud = mydata.Low_cloud;
        Medium_cloud = mydata.Medium_cloud;
        High_cloud = mydata.High_cloud;
        this.TEM_isobaric_500 = mydata.TEM_isobaric_500;
        this.TEM_isobaric_700 = mydata.TEM_isobaric_700;
        this.TEM_isobaric_850 = mydata.TEM_isobaric_850;
        this.RHU_isobaric_500 = mydata.RHU_isobaric_500;
        this.RHU_isobaric_700 = mydata.RHU_isobaric_700;
        this.RHU_isobaric_850 = mydata.RHU_isobaric_850;
        this.WIU10_isobaric_500 = mydata.WIU10_isobaric_500;
        this.WIU10_isobaric_700 = mydata.WIU10_isobaric_700;
        this.WIU10_isobaric_850 = mydata.WIU10_isobaric_850;
        this.WIV10_isobaric_500 = mydata.WIV10_isobaric_500;
        this.WIV10_isobaric_700 = mydata.WIV10_isobaric_700;
        this.WIV10_isobaric_850 = mydata.WIV10_isobaric_850;
        Geopotential_height_500 = mydata.Geopotential_height_500;
        Geopotential_height_700 = mydata.Geopotential_height_700;
        Geopotential_height_850 =mydata.Geopotential_height_850;
    }
    public Rmaps数值预报站点Model(Rmaps数值预报站点Model mydata) {
        this.ID = mydata.ID;
        MyDate = mydata.MyDate;
        this.SX = mydata.SX;
        this.PRE = mydata.PRE;
        this.TEM = mydata.TEM;
        this.TEM_surface = mydata.TEM_surface;
        this.RHU = mydata.RHU;
        this.PRS = mydata.PRS;
        this.WIU10 = mydata.WIU10;
        this.WIV10 = mydata.WIV10;
        this.VIS = mydata.VIS;
        Low_cloud = mydata.Low_cloud;
        Medium_cloud = mydata.Medium_cloud;
        High_cloud = mydata.High_cloud;
        this.TEM_isobaric_500 = mydata.TEM_isobaric_500;
        this.TEM_isobaric_700 = mydata.TEM_isobaric_700;
        this.TEM_isobaric_850 = mydata.TEM_isobaric_850;
        this.RHU_isobaric_500 = mydata.RHU_isobaric_500;
        this.RHU_isobaric_700 = mydata.RHU_isobaric_700;
        this.RHU_isobaric_850 = mydata.RHU_isobaric_850;
        this.WIU10_isobaric_500 = mydata.WIU10_isobaric_500;
        this.WIU10_isobaric_700 = mydata.WIU10_isobaric_700;
        this.WIU10_isobaric_850 = mydata.WIU10_isobaric_850;
        this.WIV10_isobaric_500 = mydata.WIV10_isobaric_500;
        this.WIV10_isobaric_700 = mydata.WIV10_isobaric_700;
        this.WIV10_isobaric_850 = mydata.WIV10_isobaric_850;
        Geopotential_height_500 = mydata.Geopotential_height_500;
        Geopotential_height_700 = mydata.Geopotential_height_700;
        Geopotential_height_850 =mydata.Geopotential_height_850;
    }
    public Rmaps数值预报站点Model(String ID, DateTime myDate, int SX, double PRE, double TEM, double TEM_surface, double RHU, double PRS, double WIU10, double WIV10, double VIS, double low_cloud, double medium_cloud, double high_cloud, double TEM_isobaric_500, double TEM_isobaric_700, double TEM_isobaric_850, double RHU_isobaric_500, double RHU_isobaric_700, double RHU_isobaric_850, double WIU10_isobaric_500, double WIU10_isobaric_700, double WIU10_isobaric_850, double WIV10_isobaric_500, double WIV10_isobaric_700, double WIV10_isobaric_850, double geopotential_height_500, double geopotential_height_700, double geopotential_height_850) {
        this.ID = ID;
        MyDate = myDate;
        this.SX = SX;
        this.PRE = PRE;
        this.TEM = TEM;
        this.TEM_surface = TEM_surface;
        this.RHU = RHU;
        this.PRS = PRS;
        this.WIU10 = WIU10;
        this.WIV10 = WIV10;
        this.VIS = VIS;
        Low_cloud = low_cloud;
        Medium_cloud = medium_cloud;
        High_cloud = high_cloud;
        this.TEM_isobaric_500 = TEM_isobaric_500;
        this.TEM_isobaric_700 = TEM_isobaric_700;
        this.TEM_isobaric_850 = TEM_isobaric_850;
        this.RHU_isobaric_500 = RHU_isobaric_500;
        this.RHU_isobaric_700 = RHU_isobaric_700;
        this.RHU_isobaric_850 = RHU_isobaric_850;
        this.WIU10_isobaric_500 = WIU10_isobaric_500;
        this.WIU10_isobaric_700 = WIU10_isobaric_700;
        this.WIU10_isobaric_850 = WIU10_isobaric_850;
        this.WIV10_isobaric_500 = WIV10_isobaric_500;
        this.WIV10_isobaric_700 = WIV10_isobaric_700;
        this.WIV10_isobaric_850 = WIV10_isobaric_850;
        Geopotential_height_500 = geopotential_height_500;
        Geopotential_height_700 = geopotential_height_700;
        Geopotential_height_850 = geopotential_height_850;
    }

    public Rmaps数值预报站点Model(String ID, DateTime myDate, int SX) {
        this.ID = ID;
        MyDate = myDate;
        this.SX = SX;
    }

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

    public double getPRE() {
        return PRE;
    }

    public void setPRE(double PRE) {
        this.PRE = PRE;
    }

    public double getTEM() {
        return TEM;
    }

    public void setTEM(double TEM) {
        this.TEM = TEM;
    }

    public double getTEM_surface() {
        return TEM_surface;
    }

    public void setTEM_surface(double TEM_surface) {
        this.TEM_surface = TEM_surface;
    }

    public double getRHU() {
        return RHU;
    }

    public void setRHU(double RHU) {
        this.RHU = RHU;
    }

    public double getPRS() {
        return PRS;
    }

    public void setPRS(double PRS) {
        this.PRS = PRS;
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

    public double getVIS() {
        return VIS;
    }

    public void setVIS(double VIS) {
        this.VIS = VIS;
    }

    public double getLow_cloud() {
        return Low_cloud;
    }

    public void setLow_cloud(double low_cloud) {
        Low_cloud = low_cloud;
    }

    public double getMedium_cloud() {
        return Medium_cloud;
    }

    public void setMedium_cloud(double medium_cloud) {
        Medium_cloud = medium_cloud;
    }

    public double getHigh_cloud() {
        return High_cloud;
    }

    public void setHigh_cloud(double high_cloud) {
        High_cloud = high_cloud;
    }

    public double getTEM_isobaric_500() {
        return TEM_isobaric_500;
    }

    public void setTEM_isobaric_500(double TEM_isobaric_500) {
        this.TEM_isobaric_500 = TEM_isobaric_500;
    }

    public double getTEM_isobaric_700() {
        return TEM_isobaric_700;
    }

    public void setTEM_isobaric_700(double TEM_isobaric_700) {
        this.TEM_isobaric_700 = TEM_isobaric_700;
    }

    public double getTEM_isobaric_850() {
        return TEM_isobaric_850;
    }

    public void setTEM_isobaric_850(double TEM_isobaric_850) {
        this.TEM_isobaric_850 = TEM_isobaric_850;
    }

    public double getRHU_isobaric_500() {
        return RHU_isobaric_500;
    }

    public void setRHU_isobaric_500(double RHU_isobaric_500) {
        this.RHU_isobaric_500 = RHU_isobaric_500;
    }

    public double getRHU_isobaric_700() {
        return RHU_isobaric_700;
    }

    public void setRHU_isobaric_700(double RHU_isobaric_700) {
        this.RHU_isobaric_700 = RHU_isobaric_700;
    }

    public double getRHU_isobaric_850() {
        return RHU_isobaric_850;
    }

    public void setRHU_isobaric_850(double RHU_isobaric_850) {
        this.RHU_isobaric_850 = RHU_isobaric_850;
    }

    public double getWIU10_isobaric_500() {
        return WIU10_isobaric_500;
    }

    public void setWIU10_isobaric_500(double WIU10_isobaric_500) {
        this.WIU10_isobaric_500 = WIU10_isobaric_500;
    }

    public double getWIU10_isobaric_700() {
        return WIU10_isobaric_700;
    }

    public void setWIU10_isobaric_700(double WIU10_isobaric_700) {
        this.WIU10_isobaric_700 = WIU10_isobaric_700;
    }

    public double getWIU10_isobaric_850() {
        return WIU10_isobaric_850;
    }

    public void setWIU10_isobaric_850(double WIU10_isobaric_850) {
        this.WIU10_isobaric_850 = WIU10_isobaric_850;
    }

    public double getWIV10_isobaric_500() {
        return WIV10_isobaric_500;
    }

    public void setWIV10_isobaric_500(double WIV10_isobaric_500) {
        this.WIV10_isobaric_500 = WIV10_isobaric_500;
    }

    public double getWIV10_isobaric_700() {
        return WIV10_isobaric_700;
    }

    public void setWIV10_isobaric_700(double WIV10_isobaric_700) {
        this.WIV10_isobaric_700 = WIV10_isobaric_700;
    }

    public double getWIV10_isobaric_850() {
        return WIV10_isobaric_850;
    }

    public void setWIV10_isobaric_850(double WIV10_isobaric_850) {
        this.WIV10_isobaric_850 = WIV10_isobaric_850;
    }

    public double getGeopotential_height_500() {
        return Geopotential_height_500;
    }

    public void setGeopotential_height_500(double geopotential_height_500) {
        Geopotential_height_500 = geopotential_height_500;
    }

    public double getGeopotential_height_700() {
        return Geopotential_height_700;
    }

    public void setGeopotential_height_700(double geopotential_height_700) {
        Geopotential_height_700 = geopotential_height_700;
    }

    public double getGeopotential_height_850() {
        return Geopotential_height_850;
    }

    public void setGeopotential_height_850(double geopotential_height_850) {
        Geopotential_height_850 = geopotential_height_850;
    }
}
