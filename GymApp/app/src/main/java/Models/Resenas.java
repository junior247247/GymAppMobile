package Models;

public class Resenas {
    private String id;
    private String messge;
    private String idUser;
    private long timestamp;
    public  Resenas(){}
    public Resenas(String id, String messge, String idUser,long timestamp) {
        this.id = id;
        this.messge = messge;
        this.idUser = idUser;
        this.timestamp=timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessge() {
        return messge;
    }

    public void setMessge(String messge) {
        this.messge = messge;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
