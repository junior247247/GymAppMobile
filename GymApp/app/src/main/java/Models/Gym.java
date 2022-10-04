package Models;

import java.util.ArrayList;

public class Gym {
    private String id;
    private String lugar;
    private String desde;
    private String hasta;
    private String artesmarciales;
    private String facebook;
    private String instagram;
    private String twitter;
    private String tiktok;
    private String urlImagePortada;
    private String urlImageProfile;
    private String nameGym;
    private long timestamp;
    ArrayList<String> luchadores;
    public Gym(){

    }

    public Gym(ArrayList<String> list,String id, String lugar, String desde, String hasta, String artesmarciales, String facebook, String instagram, String twitter, String urlImagePortada, String urlImageProfile, String nameGym,long timestamp,String tiktok) {
        this.id = id;
        this.lugar = lugar;
        this.desde = desde;
        this.hasta = hasta;
        this.artesmarciales = artesmarciales;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
        this.urlImagePortada = urlImagePortada;
        this.urlImageProfile = urlImageProfile;
        this.nameGym = nameGym;
        this.luchadores=list;
        this.timestamp=timestamp;
        this.tiktok=tiktok;
    }

    public String getTiktok() {
        return tiktok;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getLuchadores() {
        return luchadores;
    }

    public void setLuchadores(ArrayList<String> luchadores) {
        this.luchadores = luchadores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getArtesmarciales() {
        return artesmarciales;
    }

    public void setArtesmarciales(String artesmarciales) {
        this.artesmarciales = artesmarciales;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getUrlImagePortada() {
        return urlImagePortada;
    }

    public void setUrlImagePortada(String urlImagePortada) {
        this.urlImagePortada = urlImagePortada;
    }

    public String getUrlImageProfile() {
        return urlImageProfile;
    }

    public void setUrlImageProfile(String urlImageProfile) {
        this.urlImageProfile = urlImageProfile;
    }

    public String getNameGym() {
        return nameGym;
    }

    public void setNameGym(String nameGym) {
        this.nameGym = nameGym;
    }
}
