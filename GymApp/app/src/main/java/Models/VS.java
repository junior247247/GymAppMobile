package Models;

public class VS {
    private String id;
    private String idUser1;
    private String idUser2;
    private String idEvento;
    private long timestamp;
    public  VS(){

    }

    public VS(String id, String idUser1, String idUser2, long timestamp,String idEvento) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.timestamp=timestamp;
        this.idEvento=idEvento;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
