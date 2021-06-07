package yzh.model;


import java.io.Serializable;
import java.util.Date;

public class CuaceHeigh implements Serializable {
    private String stationid;

    private Date datetime;

    private Integer validtime;

    private Integer fcstlevel;

    private Double concDust;

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

    public Integer getFcstlevel() {
        return fcstlevel;
    }

    public void setFcstlevel(Integer fcstlevel) {
        this.fcstlevel = fcstlevel;
    }

    public Double getConcDust() {
        return concDust;
    }

    public void setConcDust(Double concDust) {
        this.concDust = concDust;
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
        sb.append(", fcstlevel=").append(fcstlevel);
        sb.append(", concDust=").append(concDust);
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
        CuaceHeigh other = (CuaceHeigh) that;
        return (this.getStationid() == null ? other.getStationid() == null : this.getStationid().equals(other.getStationid()))
            && (this.getDatetime() == null ? other.getDatetime() == null : this.getDatetime().equals(other.getDatetime()))
            && (this.getValidtime() == null ? other.getValidtime() == null : this.getValidtime().equals(other.getValidtime()))
            && (this.getFcstlevel() == null ? other.getFcstlevel() == null : this.getFcstlevel().equals(other.getFcstlevel()))
            && (this.getConcDust() == null ? other.getConcDust() == null : this.getConcDust().equals(other.getConcDust()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStationid() == null) ? 0 : getStationid().hashCode());
        result = prime * result + ((getDatetime() == null) ? 0 : getDatetime().hashCode());
        result = prime * result + ((getValidtime() == null) ? 0 : getValidtime().hashCode());
        result = prime * result + ((getFcstlevel() == null) ? 0 : getFcstlevel().hashCode());
        result = prime * result + ((getConcDust() == null) ? 0 : getConcDust().hashCode());
        return result;
    }
}