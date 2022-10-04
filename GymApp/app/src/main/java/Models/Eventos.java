package Models;

public class Eventos {
    private String id;
    private String nombreEvento;
    private String horaEvento;
    private String horaCareo;
    private String horaPrimerPelea;
    private String categoria;
    private String reglas;
    private String premios;
    private String fecha;
    private String UrlImage;
    private String emision;
    private String lugar;
    private String fechaLimite;
    private long timestamp;

    public Eventos(){

    }

    public Eventos(String fechaLimite,String id, long timestamp,String lugar,String emision,String nombreEvento, String horaEvento, String horaCareo, String horaPrimerPelea, String categoria, String reglas, String premios, String fecha, String urlImage) {
        this.nombreEvento = nombreEvento;
        this.horaEvento = horaEvento;
        this.horaCareo = horaCareo;
        this.horaPrimerPelea = horaPrimerPelea;
        this.categoria = categoria;
        this.reglas = reglas;
        this.premios = premios;
        this.fecha = fecha;
        this.UrlImage = urlImage;
        this.emision=emision;
        this.lugar=lugar;
        this.timestamp=timestamp;
        this.id =id ;
        this.fechaLimite=fechaLimite;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getHoraCareo() {
        return horaCareo;
    }

    public void setHoraCareo(String horaCareo) {
        this.horaCareo = horaCareo;
    }

    public String getHoraPrimerPelea() {
        return horaPrimerPelea;
    }

    public void setHoraPrimerPelea(String horaPrimerPelea) {
        this.horaPrimerPelea = horaPrimerPelea;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getReglas() {
        return reglas;
    }

    public void setReglas(String reglas) {
        this.reglas = reglas;
    }

    public String getPremios() {
        return premios;
    }

    public void setPremios(String premios) {
        this.premios = premios;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }
}
