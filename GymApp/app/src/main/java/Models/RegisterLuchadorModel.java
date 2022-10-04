package Models;

import android.os.ParcelUuid;

public class RegisterLuchadorModel {
    private String id;
    private String email;
    private String name;
    private String edad;
    private String Altura;
    private String urlImage;
    private String debutEnelOctagono;
    private String alcanzar;
    private String precisionGolpeando;
    private String golpesClaves;
    private String golpesClaves2;
    private String GolpesClavesAbsovidosPorMin;
    private String precicionGolpeando;
    private String promedioDerribos;
    private String defensaDerriboPorciento;
    private String promedioSumision;
    private String categoria;
    private String divicion;
    private String precicionAgarre;
    private String peso;
    private String ganadas;
    private String perdidas;
    private String empates;

    public  RegisterLuchadorModel(){

    }

    public RegisterLuchadorModel(String ganadas,String perdidas,String empates,String peso,String precicionAgarre,String id, String email, String name, String edad, String altura, String urlImage, String debutEnelOctagono, String alcanzar, String precisionGolpeando, String golpesClaves, String golpesClaves2, String golpesClavesAbsovidosPorMin, String precicionGolpeando, String promedioDerribos, String defensaDerriboPorciento, String promedioSumision, String categoria, String divicion) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.edad = edad;
        this.Altura = altura;
        this.urlImage = urlImage;
        this.debutEnelOctagono = debutEnelOctagono;
        this.alcanzar = alcanzar;
        this.precisionGolpeando = precisionGolpeando;
        this.golpesClaves = golpesClaves;
        this.golpesClaves2 = golpesClaves2;
        this.GolpesClavesAbsovidosPorMin = golpesClavesAbsovidosPorMin;
        this.precicionGolpeando = precicionGolpeando;
        this.promedioDerribos = promedioDerribos;
        this.defensaDerriboPorciento = defensaDerriboPorciento;
        this.promedioSumision = promedioSumision;
        this.categoria = categoria;
        this.divicion = divicion;
        this.precicionAgarre=precicionAgarre;
        this.peso=peso;
        this.ganadas=ganadas;
        this.perdidas=perdidas;
        this.empates=empates;
    }

    public void setPerdidas(String perdidas) {
        this.perdidas = perdidas;
    }

    public String getPerdidas() {
        return perdidas;
    }

    public void setGanadas(String ganadas) {
        this.ganadas = ganadas;
    }

    public String getGanadas() {
        return ganadas;
    }

    public void setEmpates(String empates) {
        this.empates = empates;
    }

    public String getEmpates() {
        return empates;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setPrecicionAgarre(String precicionAgarre) {
        this.precicionAgarre = precicionAgarre;
    }

    public String getPrecicionAgarre() {
        return precicionAgarre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDebutEnelOctagono() {
        return debutEnelOctagono;
    }

    public void setDebutEnelOctagono(String debutEnelOctagono) {
        this.debutEnelOctagono = debutEnelOctagono;
    }

    public String getAlcanzar() {
        return alcanzar;
    }

    public void setAlcanzar(String alcanzar) {
        this.alcanzar = alcanzar;
    }

    public String getPrecisionGolpeando() {
        return precisionGolpeando;
    }

    public void setPrecisionGolpeando(String precisionGolpeando) {
        this.precisionGolpeando = precisionGolpeando;
    }

    public String getGolpesClaves() {
        return golpesClaves;
    }

    public void setGolpesClaves(String golpesClaves) {
        this.golpesClaves = golpesClaves;
    }

    public String getGolpesClaves2() {
        return golpesClaves2;
    }

    public void setGolpesClaves2(String golpesClaves2) {
        this.golpesClaves2 = golpesClaves2;
    }

    public String getGolpesClavesAbsovidosPorMin() {
        return GolpesClavesAbsovidosPorMin;
    }

    public void setGolpesClavesAbsovidosPorMin(String golpesClavesAbsovidosPorMin) {
        GolpesClavesAbsovidosPorMin = golpesClavesAbsovidosPorMin;
    }

    public String getPrecicionGolpeando() {
        return precicionGolpeando;
    }

    public void setPrecicionGolpeando(String precicionGolpeando) {
        this.precicionGolpeando = precicionGolpeando;
    }

    public String getPromedioDerribos() {
        return promedioDerribos;
    }

    public void setPromedioDerribos(String promedioDerribos) {
        this.promedioDerribos = promedioDerribos;
    }

    public String getDefensaDerriboPorciento() {
        return defensaDerriboPorciento;
    }

    public void setDefensaDerriboPorciento(String defensaDerriboPorciento) {
        this.defensaDerriboPorciento = defensaDerriboPorciento;
    }

    public String getPromedioSumision() {
        return promedioSumision;
    }

    public void setPromedioSumision(String promedioSumision) {
        this.promedioSumision = promedioSumision;
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
}
