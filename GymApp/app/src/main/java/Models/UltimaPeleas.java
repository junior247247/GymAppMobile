package Models;

import java.util.ArrayList;

public class UltimaPeleas {
    private String id;
    private String nameLuchador;
    private String nameOponente;
    private String ganadasLuchador;
    private String perdidasLuchador;
    private String empatesLuchador;
    private String ganadasOponente;
    private String perdidasOponente;
    private String empatesOponentes;
    private String referencia;
    private String idGanador;
    private String duration;
    private String tipo;
    private String categoria;
    private String peso;
    private String idPerdedor;
    private String fecha;
    private ArrayList<String> ids;
    private String ganoCombate;
    private boolean esempate;
    private long timestamp;

    public UltimaPeleas(){

    }

    public UltimaPeleas(String fecha,String ganoCombate,String categoria,String peso,String duration,String tipo,String nameOponente,String id, String nameLuchador, String ganadasLuchador, String perdidasLuchador, String empatesLuchador, String ganadasOponente, String perdidasOponente, String empatesOponentes, long timestamp,String referencia,String idGanador,ArrayList<String> ids,String idPerdedor,boolean esempate) {
        this.id = id;
        this.tipo=tipo;
        this.fecha=fecha;
        this.ganoCombate=ganoCombate;
        this.duration=duration;
        this.nameLuchador = nameLuchador;
        this.ganadasLuchador = ganadasLuchador;
        this.perdidasLuchador = perdidasLuchador;
        this.empatesLuchador = empatesLuchador;
        this.ganadasOponente = ganadasOponente;
        this.perdidasOponente = perdidasOponente;
        this.empatesOponentes = empatesOponentes;
        this.timestamp = timestamp;
        this.nameOponente=nameOponente;
        this.referencia=referencia;
        this.peso=peso;
        this.categoria=categoria;
        this.idPerdedor=idPerdedor;
        this.idGanador=idGanador;
        this.ids=ids;
        this.esempate=esempate;
    }

    public void setEsempate(boolean esempate) {
        this.esempate = esempate;
    }
    public  boolean getEsempate(){
        return  esempate;
    }

    public String getIdPerdedor() {
        return idPerdedor;
    }

    public void setIdPerdedor(String idPerdedor) {
        this.idPerdedor = idPerdedor;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public String getIdGanador() {
        return idGanador;
    }

    public void setIdGanador(String idGanador) {
        this.idGanador = idGanador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGanoCombate() {
        return ganoCombate;
    }

    public void setGanoCombate(String ganoCombate) {
        this.ganoCombate = ganoCombate;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNameOponente() {
        return nameOponente;
    }

    public void setNameOponente(String nameOponente) {
        this.nameOponente = nameOponente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameLuchador() {
        return nameLuchador;
    }

    public void setNameLuchador(String nameLuchador) {
        this.nameLuchador = nameLuchador;
    }

    public String getGanadasLuchador() {
        return ganadasLuchador;
    }

    public void setGanadasLuchador(String ganadasLuchador) {
        this.ganadasLuchador = ganadasLuchador;
    }

    public String getPerdidasLuchador() {
        return perdidasLuchador;
    }

    public void setPerdidasLuchador(String perdidasLuchador) {
        this.perdidasLuchador = perdidasLuchador;
    }

    public String getEmpatesLuchador() {
        return empatesLuchador;
    }

    public void setEmpatesLuchador(String empatesLuchador) {
        this.empatesLuchador = empatesLuchador;
    }

    public String getGanadasOponente() {
        return ganadasOponente;
    }

    public void setGanadasOponente(String ganadasOponente) {
        this.ganadasOponente = ganadasOponente;
    }

    public String getPerdidasOponente() {
        return perdidasOponente;
    }

    public void setPerdidasOponente(String perdidasOponente) {
        this.perdidasOponente = perdidasOponente;
    }

    public String getEmpatesOponentes() {
        return empatesOponentes;
    }

    public void setEmpatesOponentes(String empatesOponentes) {
        this.empatesOponentes = empatesOponentes;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
