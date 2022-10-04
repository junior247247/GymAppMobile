package Models;

import java.util.ArrayList;

public class Losmejores {
    private String id;
    private ArrayList<String> esmejor;
    private String idLuchador;
    private int posicion;
    private long timestamp;
    private String tipo;
    private String nombre;
    public Losmejores(){

    }

    public Losmejores(String id, ArrayList<String> esmejor, String idLuchador,long timestamp,int posicion,String tipo,String nombre) {
        this.id = id;
        this.esmejor = esmejor;
        this.idLuchador = idLuchador;
        this.timestamp=timestamp;
        this.posicion=posicion;
        this.tipo=tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<String> getEsmejor() {
        return esmejor;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setEsmejor(ArrayList<String> esmejor) {
        this.esmejor = esmejor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getIdLuchador() {
        return idLuchador;
    }

    public void setIdLuchador(String idLuchador) {
        this.idLuchador = idLuchador;
    }
}
