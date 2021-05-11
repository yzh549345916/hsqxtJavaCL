package model.openlayers;

public class OpenlayersgeometryModel {
    private String type;

    private Double[] coordinates;

    public OpenlayersgeometryModel(String type, Double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
