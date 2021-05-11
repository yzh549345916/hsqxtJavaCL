package model.openlayers;

public class OpenlayersQJMethodBaseModel {
    private String type;
    private OpenlayersQJMethodModel[] features;

    public OpenlayersQJMethodModel[] getFeatures() {
        return features;
    }

    public void setFeatures(OpenlayersQJMethodModel[] features) {
        this.features = features;
    }

    public OpenlayersQJMethodBaseModel() {
    }

    public OpenlayersQJMethodBaseModel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
