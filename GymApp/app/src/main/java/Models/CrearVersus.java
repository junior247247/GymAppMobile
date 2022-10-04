package Models;

public class CrearVersus {
    private String id;
    private String idLuchador1;
    private String idLuchador2;
    private String idEvento;
    private String categoria;
    private boolean is;
    private  long timestamp;
    public CrearVersus(){

    }

    public CrearVersus(String id, String idLuchador1, String idLuchador2, String idEvento,String cetegoria,long timestamp,boolean is) {
        this.id = id;
        this.idLuchador1 = idLuchador1;
        this.idLuchador2 = idLuchador2;
        this.idEvento = idEvento;
        this.categoria=cetegoria;
        this.is=is;
        this.timestamp=timestamp;
    }

    public void setIs(boolean is) {
        this.is = is;
    }
    public  boolean getIs(){
        return is;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLuchador1() {
        return idLuchador1;
    }

    public void setIdLuchador1(String idLuchador1) {
        this.idLuchador1 = idLuchador1;
    }

    public String getIdLuchador2() {
        return idLuchador2;
    }

    public void setIdLuchador2(String idLuchador2) {
        this.idLuchador2 = idLuchador2;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}
