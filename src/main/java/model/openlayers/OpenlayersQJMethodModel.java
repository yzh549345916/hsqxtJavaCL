package model.openlayers;
public class OpenlayersQJMethodModel {
    private String type;
    private OpenlayersgeometryModel geometry;

    public OpenlayersQJMethodModel(String type,  OpenlayersgeometryModel geometry, OpenlayersQJMethodPropertiesModel properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }

    public OpenlayersgeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(OpenlayersgeometryModel geometry) {
        this.geometry = geometry;
    }

    public OpenlayersQJMethodModel(String type) {
        this.type = type;
    }

    public OpenlayersQJMethodModel() {
    }



    private OpenlayersQJMethodPropertiesModel properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





    public OpenlayersQJMethodPropertiesModel getProperties() {
        return properties;
    }

    public void setProperties(OpenlayersQJMethodPropertiesModel properties) {
        this.properties = properties;
    }
}
