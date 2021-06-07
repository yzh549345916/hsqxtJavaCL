package yzh.model;

import java.io.Serializable;
import java.util.Date;


public class CuaceSurface implements Serializable {
    private String stationid;

    private Date datetime;

    private Integer validtime;

    private Double aod550Dust;

    private Double ddepoDust;

    private Double dfluxDust;

    private Double loadDust;

    private Double sconcDust;

    private Double u10;

    private Double v10;

    private Double wdepoDust;

    private static final long serialVersionUID = 1L;

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid == null ? null : stationid.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getValidtime() {
        return validtime;
    }

    public void setValidtime(Integer validtime) {
        this.validtime = validtime;
    }

    public Double getAod550Dust() {
        return aod550Dust;
    }

    public void setAod550Dust(Double aod550Dust) {
        this.aod550Dust = aod550Dust;
    }

    public Double getDdepoDust() {
        return ddepoDust;
    }

    public void setDdepoDust(Double ddepoDust) {
        this.ddepoDust = ddepoDust;
    }

    public Double getDfluxDust() {
        return dfluxDust;
    }

    public void setDfluxDust(Double dfluxDust) {
        this.dfluxDust = dfluxDust;
    }

    public Double getLoadDust() {
        return loadDust;
    }

    public void setLoadDust(Double loadDust) {
        this.loadDust = loadDust;
    }

    public Double getSconcDust() {
        return sconcDust;
    }

    public void setSconcDust(Double sconcDust) {
        this.sconcDust = sconcDust;
    }

    public Double getU10() {
        return u10;
    }

    public void setU10(Double u10) {
        this.u10 = u10;
    }

    public Double getV10() {
        return v10;
    }

    public void setV10(Double v10) {
        this.v10 = v10;
    }

    public Double getWdepoDust() {
        return wdepoDust;
    }

    public void setWdepoDust(Double wdepoDust) {
        this.wdepoDust = wdepoDust;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", stationid=").append(stationid);
        sb.append(", datetime=").append(datetime);
        sb.append(", validtime=").append(validtime);
        sb.append(", aod550Dust=").append(aod550Dust);
        sb.append(", ddepoDust=").append(ddepoDust);
        sb.append(", dfluxDust=").append(dfluxDust);
        sb.append(", loadDust=").append(loadDust);
        sb.append(", sconcDust=").append(sconcDust);
        sb.append(", u10=").append(u10);
        sb.append(", v10=").append(v10);
        sb.append(", wdepoDust=").append(wdepoDust);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CuaceSurface other = (CuaceSurface) that;
        return (this.getStationid() == null ? other.getStationid() == null : this.getStationid().equals(other.getStationid()))
            && (this.getDatetime() == null ? other.getDatetime() == null : this.getDatetime().equals(other.getDatetime()))
            && (this.getValidtime() == null ? other.getValidtime() == null : this.getValidtime().equals(other.getValidtime()))
            && (this.getAod550Dust() == null ? other.getAod550Dust() == null : this.getAod550Dust().equals(other.getAod550Dust()))
            && (this.getDdepoDust() == null ? other.getDdepoDust() == null : this.getDdepoDust().equals(other.getDdepoDust()))
            && (this.getDfluxDust() == null ? other.getDfluxDust() == null : this.getDfluxDust().equals(other.getDfluxDust()))
            && (this.getLoadDust() == null ? other.getLoadDust() == null : this.getLoadDust().equals(other.getLoadDust()))
            && (this.getSconcDust() == null ? other.getSconcDust() == null : this.getSconcDust().equals(other.getSconcDust()))
            && (this.getU10() == null ? other.getU10() == null : this.getU10().equals(other.getU10()))
            && (this.getV10() == null ? other.getV10() == null : this.getV10().equals(other.getV10()))
            && (this.getWdepoDust() == null ? other.getWdepoDust() == null : this.getWdepoDust().equals(other.getWdepoDust()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStationid() == null) ? 0 : getStationid().hashCode());
        result = prime * result + ((getDatetime() == null) ? 0 : getDatetime().hashCode());
        result = prime * result + ((getValidtime() == null) ? 0 : getValidtime().hashCode());
        result = prime * result + ((getAod550Dust() == null) ? 0 : getAod550Dust().hashCode());
        result = prime * result + ((getDdepoDust() == null) ? 0 : getDdepoDust().hashCode());
        result = prime * result + ((getDfluxDust() == null) ? 0 : getDfluxDust().hashCode());
        result = prime * result + ((getLoadDust() == null) ? 0 : getLoadDust().hashCode());
        result = prime * result + ((getSconcDust() == null) ? 0 : getSconcDust().hashCode());
        result = prime * result + ((getU10() == null) ? 0 : getU10().hashCode());
        result = prime * result + ((getV10() == null) ? 0 : getV10().hashCode());
        result = prime * result + ((getWdepoDust() == null) ? 0 : getWdepoDust().hashCode());
        return result;
    }
}