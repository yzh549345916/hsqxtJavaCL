package model;

public class 站点信息 {
    private String ID;
    private String Name;
    private String Admin_Code;
    private String Province;
    private String City;
    private String Cnty;
    private int Level;
    private double Lat;
    private double Lon;
    private double myX;
    private double myY;

    public double getMyX() {
        return myX;
    }

    public void setMyX(double myX) {
        this.myX = myX;
    }

    public double getMyY() {
        return myY;
    }

    public void setMyY(double myY) {
        this.myY = myY;
    }

    public 站点信息(){}
    public 站点信息(String ID, String name, String admin_Code, String province, String city, String cnty, int level, double lat, double lon, double height) {
        this.ID = ID;
        Name = name;
        Admin_Code = admin_Code;
        Province = province;
        City = city;
        Cnty = cnty;
        Level = level;
        Lat = lat;
        Lon = lon;
        Height = height;
    }

    private double Height;



    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
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

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getCnty() {
        return Cnty;
    }

    public void setCnty(String cnty) {
        Cnty = cnty;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getAdmin_Code() {
        return Admin_Code;
    }

    public void setAdmin_Code(String admin_Code) {
        Admin_Code = admin_Code;
    }
}
