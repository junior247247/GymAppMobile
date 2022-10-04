package app.makingfight.gymapp;

public class EventoYfotos {
    private  String idEvento;
    private String idCaganador;
    private String idPerdedor;
    private long tipo;
    private  boolean esEmpate;
    private  long timestamp;


    public  EventoYfotos(){

    }

    public EventoYfotos( String idEvento, String idCaganador, String idPerdedor, long tipo,long timestamp,boolean esEmpate) {
        this.idEvento = idEvento;
        this.idCaganador = idCaganador;
        this.idPerdedor = idPerdedor;
        this.tipo = tipo;
        this.timestamp=timestamp;
        this.esEmpate=esEmpate;
    }

    public void setEsEmpate(boolean esEmpate) {
        this.esEmpate = esEmpate;
    }

    public boolean isEsEmpate() {
        return esEmpate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getIdCaganador() {
        return idCaganador;
    }

    public void setIdCaganador(String idCaganador) {
        this.idCaganador = idCaganador;
    }

    public String getIdPerdedor() {
        return idPerdedor;
    }

    public void setIdPerdedor(String idPerdedor) {
        this.idPerdedor = idPerdedor;
    }

    public long getTipo() {
        return tipo;
    }

    public void setTipo(long tipo) {
        this.tipo = tipo;
    }
}
