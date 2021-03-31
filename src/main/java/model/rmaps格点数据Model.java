package model;

public class rmaps格点数据Model {
    private Double EARTH_RADIUS;
    private String Grid_Mapping_Name;
    private Double Latitude_Of_Projection_Origin;
    private Double Longitude_Of_Central_Meridian;
    private Double StartX;
    private Double StartY;
    private int xRows;
    private int yRows;
    private Double dx;

    public float[] getData() {
        return Data;
    }

    public void setData(float[] data) {
        Data = data;
    }


    public rmaps格点数据Model(Double EARTH_RADIUS, String grid_Mapping_Name, Double latitude_Of_Projection_Origin, Double longitude_Of_Central_Meridian, Double startX, Double startY, int xRows, int yRows, Double dx, Double dy) {
        this.EARTH_RADIUS = EARTH_RADIUS;
        Grid_Mapping_Name = grid_Mapping_Name;
        Latitude_Of_Projection_Origin = latitude_Of_Projection_Origin;
        Longitude_Of_Central_Meridian = longitude_Of_Central_Meridian;
        StartX = startX;
        StartY = startY;
        this.xRows = xRows;
        this.yRows = yRows;
        this.dx = dx;
        this.dy = dy;
    }

    public rmaps格点数据Model() {
    }

    private Double dy;
    private int forecastTime;
    private String paramType;

    public rmaps格点数据Model(Double EARTH_RADIUS, String grid_Mapping_Name, Double latitude_Of_Projection_Origin, Double longitude_Of_Central_Meridian, Double startX, Double startY, int xRows, int yRows, Double dx, Double dy, int forecastTime, String paramType, float[] data) {
        this.EARTH_RADIUS = EARTH_RADIUS;
        Grid_Mapping_Name = grid_Mapping_Name;
        Latitude_Of_Projection_Origin = latitude_Of_Projection_Origin;
        Longitude_Of_Central_Meridian = longitude_Of_Central_Meridian;
        StartX = startX;
        StartY = startY;
        this.xRows = xRows;
        this.yRows = yRows;
        this.dx = dx;
        this.dy = dy;
        this.forecastTime = forecastTime;
        this.paramType = paramType;
        Data = data;
    }

    private float[] Data;



    public int getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(int forecastTime) {
        this.forecastTime = forecastTime;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }



    public rmaps格点数据Model(Double EARTH_RADIUS, String grid_Mapping_Name, Double latitude_Of_Projection_Origin, Double longitude_Of_Central_Meridian, Double startX, Double startY, int xRows, int yRows, Double dx, Double dy, float[] data) {
        this.EARTH_RADIUS = EARTH_RADIUS;
        Grid_Mapping_Name = grid_Mapping_Name;
        Latitude_Of_Projection_Origin = latitude_Of_Projection_Origin;
        Longitude_Of_Central_Meridian = longitude_Of_Central_Meridian;
        StartX = startX;
        StartY = startY;
        this.xRows = xRows;
        this.yRows = yRows;
        this.dx = dx;
        this.dy = dy;
        Data = data;
    }

    public Double getEARTH_RADIUS() {
        return EARTH_RADIUS;
    }

    public void setEARTH_RADIUS(Double EARTH_RADIUS) {
        this.EARTH_RADIUS = EARTH_RADIUS;
    }

    public String getGrid_Mapping_Name() {
        return Grid_Mapping_Name;
    }

    public void setGrid_Mapping_Name(String grid_Mapping_Name) {
        Grid_Mapping_Name = grid_Mapping_Name;
    }

    public Double getLatitude_Of_Projection_Origin() {
        return Latitude_Of_Projection_Origin;
    }

    public void setLatitude_Of_Projection_Origin(Double latitude_Of_Projection_Origin) {
        Latitude_Of_Projection_Origin = latitude_Of_Projection_Origin;
    }

    public Double getLongitude_Of_Central_Meridian() {
        return Longitude_Of_Central_Meridian;
    }

    public void setLongitude_Of_Central_Meridian(Double longitude_Of_Central_Meridian) {
        Longitude_Of_Central_Meridian = longitude_Of_Central_Meridian;
    }

    public Double getStartX() {
        return StartX;
    }

    public void setStartX(Double startX) {
        StartX = startX;
    }

    public Double getStartY() {
        return StartY;
    }

    public void setStartY(Double startY) {
        StartY = startY;
    }

    public int getxRows() {
        return xRows;
    }

    public void setxRows(int xRows) {
        this.xRows = xRows;
    }

    public int getyRows() {
        return yRows;
    }

    public void setyRows(int yRows) {
        this.yRows = yRows;
    }

    public Double getDx() {
        return dx;
    }

    public void setDx(Double dx) {
        this.dx = dx;
    }

    public Double getDy() {
        return dy;
    }

    public void setDy(Double dy) {
        this.dy = dy;
    }
}
