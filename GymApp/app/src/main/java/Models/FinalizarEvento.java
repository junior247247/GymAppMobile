package Models;

public class FinalizarEvento {
    private String id;
    private String idEvento;
    private String resultados;
    private String lugar;
    private String fecha;
    private long timestamp;
    private boolean isFinal;



    public FinalizarEvento(){

    }

    public FinalizarEvento(String id, String idEvento, String resultados,String lugar,String fecha,long timestamp ,boolean isFinal) {
        this.id = id;
        this.idEvento = idEvento;
        this.resultados = resultados;
        this.lugar=lugar;
        this.fecha=fecha;
        this.timestamp=timestamp;
        this.isFinal=isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
    public  boolean getIsFinal(){
        return  isFinal;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }
}
