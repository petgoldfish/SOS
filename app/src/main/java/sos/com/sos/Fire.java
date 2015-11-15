package sos.com.sos;


public class Fire {

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    String lat;

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    String lon;

    public Fire(){
        this.lat=null;
        this.lon=null;
    }

    public Fire(String lat, String lon){
        this.lat=lat;
        this.lon=lon;
    }
}
