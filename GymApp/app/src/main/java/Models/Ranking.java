package Models;

public class Ranking {
    private String idLuchador;
    private String region;
    private long position;
    private String name;
    private  long timestamp;


    public  Ranking(){

    }


    public Ranking(String idLuchador, String mejor, long posiciontion, long timestamp,String name) {
        this.idLuchador = idLuchador;
        this.region = mejor;
        this.position = posiciontion;
        this.timestamp = timestamp;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdLuchador() {
        return idLuchador;
    }

    public void setIdLuchador(String idLuchador) {
        this.idLuchador = idLuchador;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPosiciontion() {
        return position;
    }

    public void setPosiciontion(long posiciontion) {
        this.position = posiciontion;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
