package Models;

public class EventoAndLuchadores {
    private String id;
    private String idGym;
    private String idLuchador;
    private String idEvento;
    private String isAcceted;
    private boolean isVersus;
    private long timestamp;

    public EventoAndLuchadores(){

    }

    public EventoAndLuchadores(String id, String idGym, String idLuchador, String idEvento,String isAcceted,long timestamp,boolean isVersus) {
        this.id = id;
        this.idGym = idGym;
        this.idLuchador = idLuchador;
        this.idEvento = idEvento;
        this.isAcceted=isAcceted;
        this.timestamp=timestamp;
        this.isVersus=isVersus;
    }

    public void setisVersus(boolean versus) {
        isVersus = versus;
    }
    public  boolean getIsVersus(){
        return  isVersus;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsAcceted() {
        return isAcceted;
    }

    public void setIsAcceted(String isAcceted) {
        this.isAcceted = isAcceted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGym() {
        return idGym;
    }

    public void setIdGym(String idGym) {
        this.idGym = idGym;
    }

    public String getIdLuchador() {
        return idLuchador;
    }

    public void setIdLuchador(String idLuchador) {
        this.idLuchador = idLuchador;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}
