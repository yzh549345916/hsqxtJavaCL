package model.openlayers;

public class OpenlayersQJMethodPropertiesModel {
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public OpenlayersQJMethodPropertiesModel(double lat, double lon, double ybvalue, String ybName, String ybUnit) {
        this.lat = lat;
        this.lon = lon;
        this.ybvalue = ybvalue;
        this.ybName = ybName;
        this.ybUnit = ybUnit;
    }

    private double ybvalue;
    private double ybvalue2;

    public double getYbvalue2() {
        return ybvalue2;
    }

    public void setYbvalue2(double ybvalue2) {
        this.ybvalue2 = ybvalue2;
    }

    public double getYbvalue() {
        return ybvalue;
    }

    public void setYbvalue(double ybvalue) {
        this.ybvalue = ybvalue;
    }

    public String getYbName() {
        return ybName;
    }

    public void setYbName(String ybName) {
        this.ybName = ybName;
    }

    public String getYbUnit() {
        return ybUnit;
    }

    public void setYbUnit(String ybUnit) {
        this.ybUnit = ybUnit;
    }

    private String ybName;
    private String ybUnit;
    private String ybName2;



    public String getYbName2() {
        return ybName2;
    }

    public void setYbName2(String ybName2) {
        this.ybName2 = ybName2;
    }

    public String getYbUnit2() {
        return ybUnit2;
    }

    public void setYbUnit2(String ybUnit2) {
        this.ybUnit2 = ybUnit2;
    }

    private String ybUnit2;
    public OpenlayersQJMethodPropertiesModel() {
    }


}
