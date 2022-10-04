package Models;

public class Luchador {
    private String id;
    private String name;
    private String edad;
    private String Altura;
    private String urlImage;
    private String categoria;
    private String divicion;
    private String peso;
    private String ganadas;
    private String perdidas;
    private String empates;
    private String idGym;
    private String alias;
    private String pais;
    private String code;
    private String region;
    private String afiliacion;
    private String esmejor;
    private long timestamp;

    public Luchador() {

    }

    public Luchador(String id, String name, String edad, String altura, String urlImage, String categoria, String divicion, String peso, String ganadas, String perdidas, String empates, String idGym, String alias, String pais, String code, String region, String afiliacion, String esmejor, long timestamp) {
        this.id = id;
        this.name = name;
        this.edad = edad;
        this.Altura = altura;
        this.urlImage = urlImage;
        this.categoria = categoria;
        this.divicion = divicion;
        this.peso = peso;
        this.ganadas = ganadas;
        this.perdidas = perdidas;
        this.empates = empates;
        this.idGym = idGym;
        this.alias = alias;
        this.pais = pais;
        this.code = code;
        this.region = region;
        this.afiliacion = afiliacion;
        this.esmejor = esmejor;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getAltura() {
        return Altura;
    }

    public void setAltura(String altura) {
        Altura = altura;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDivicion() {
        return divicion;
    }

    public void setDivicion(String divicion) {
        this.divicion = divicion;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getGanadas() {
        return ganadas;
    }

    public void setGanadas(String ganadas) {
        this.ganadas = ganadas;
    }

    public String getPerdidas() {
        return perdidas;
    }

    public void setPerdidas(String perdidas) {
        this.perdidas = perdidas;
    }

    public String getEmpates() {
        return empates;
    }

    public void setEmpates(String empates) {
        this.empates = empates;
    }

    public String getIdGym() {
        return idGym;
    }

    public void setIdGym(String idGym) {
        this.idGym = idGym;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion(String afiliacion) {
        this.afiliacion = afiliacion;
    }

    public String getEsmejor() {
        return esmejor;
    }

    public void setEsmejor(String esmejor) {
        this.esmejor = esmejor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


