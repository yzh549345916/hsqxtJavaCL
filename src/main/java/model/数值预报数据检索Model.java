package model;


import java.util.Date;

public class 数值预报数据检索Model {
    private String TableName;
    private String ID;
    private int purpose;

    public 数值预报数据检索Model() {
    }

    public 数值预报数据检索Model(String tableName, int purpose, int level, Date time, String dataType) {
        TableName = tableName;
        this.purpose = purpose;
        this.level = level;
        Time = time;
        DataType = dataType;
    }

    private int level;

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public int getSX() {
        return SX;
    }

    public void setSX(int SX) {
        this.SX = SX;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    private Date Time;
    private int SX;
    private String DataType;

}
