package Models;

public class ResultadosPeleas {

    private String id;
    private  String idEvento;
    private String idLuchador1;
    private String idLuchador2;
    private String idGanador;
    private String idPerdedor;
    private String description;
    private String categoria;
    private String decicion;
    private boolean isView;
    private boolean esEmpate;
    private long timestamp;
    private boolean entro;

    public  ResultadosPeleas(){

    }

    public ResultadosPeleas(boolean entro,String idEvento, String idLuchador1, String idLuchador2, String description, long timestamp,String idGanador,String categoria,String idPerdedor,boolean isView,boolean esEmpate,String id,String decicion) {
        this.idEvento = idEvento;
        this.idLuchador1 = idLuchador1;
        this.idLuchador2 = idLuchador2;
        this.description = description;
        this.timestamp = timestamp;
        this.idGanador=idGanador;
        this.categoria=categoria;
        this.idPerdedor=idPerdedor;
        this.isView=isView;
        this.esEmpate=esEmpate;
        this.id=id;
        this.decicion=decicion;
        this.entro=entro;

    }

    public void setEntro(boolean entro) {
        this.entro = entro;
    }

    public  boolean getEntro(){
        return  entro;
    }

    public String getDecicion() {
        return decicion;
    }

    public void setDecicion(String decicion) {
        this.decicion = decicion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEsEmpate(boolean esEmpate) {
        this.esEmpate = esEmpate;
    }
    public  boolean getEsEmpate(){
        return  esEmpate;
    }

    public void setIsView(boolean view) {
        isView = view;
    }
    public  boolean getIsView(){
        return isView;
    }

    public String getIdPerdedor() {
        return idPerdedor;
    }

    public void setIdPerdedor(String idPerdedor) {
        this.idPerdedor = idPerdedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdGanador() {
        return idGanador;
    }

    public void setIdGanador(String idGanador) {
        this.idGanador = idGanador;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
