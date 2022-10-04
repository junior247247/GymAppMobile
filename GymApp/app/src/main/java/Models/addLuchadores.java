package Models;

public class addLuchadores {
    private String id;
    private String idLuchador;
    private String idGym;
    private long timestamp;

    public  addLuchadores(){

    }

    public addLuchadores(String id, String idLuchador, String idGym,long timestamp) {
        this.id = id;
        this.idLuchador = idLuchador;
        this.idGym = idGym;
        this.timestamp=timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLuchador() {
        return idLuchador;
    }

    public void setIdLuchador(String idLuchador) {
        this.idLuchador = idLuchador;
    }

    public String getIdGym() {
        return idGym;
    }

    public void setIdGym(String idGym) {
        this.idGym = idGym;
    }
}
