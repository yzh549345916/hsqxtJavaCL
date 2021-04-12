package yzh.数值预报处理.环境气象;

public class 站点信息 {
    private String ID;
    private String Name;
    private String Admin_Code;
    private String Province;
    private String City;
    private String Cnty;
    private int Station_levl;
    private double Lat;
    private double Lon;
    private double High;
    private String Type;

    public 站点信息() {
    }

    public 站点信息(String ID, String name, String admin_Code, String province, String city, String cnty, int station_levl, double lat, double lon, double high, String type) {
        this.ID = ID;
        Name = name;
        Admin_Code = admin_Code;
        Province = province;
        City = city;
        Cnty = cnty;
        Station_levl = station_levl;
        Lat = lat;
        Lon = lon;
        High = high;
        Type = type;
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

    public String getAdmin_Code() {
        return Admin_Code;
    }

    public void setAdmin_Code(String admin_Code) {
        Admin_Code = admin_Code;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCnty() {
        return Cnty;
    }

    public void setCnty(String cnty) {
        Cnty = cnty;
    }

    public int getStation_levl() {
        return Station_levl;
    }

    public void setStation_levl(int station_levl) {
        Station_levl = station_levl;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getHigh() {
        return High;
    }

    public void setHigh(double high) {
        High = high;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
