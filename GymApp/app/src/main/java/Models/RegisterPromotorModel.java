package Models;

public class RegisterPromotorModel {
    private String id;
    private String email;
    private String edad;
    private String cantidadDeEventos;
    private String ciudadesDeLosEventos;


    public RegisterPromotorModel(){

    }

    public RegisterPromotorModel(String id, String email, String edad, String cantidadDeEventos, String ciudadesDeLosEventos) {
        this.id = id;
        this.email = email;
        this.edad = edad;
        this.cantidadDeEventos = cantidadDeEventos;
        this.ciudadesDeLosEventos = ciudadesDeLosEventos;
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

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCantidadDeEventos() {
        return cantidadDeEventos;
    }

    public void setCantidadDeEventos(String cantidadDeEventos) {
        this.cantidadDeEventos = cantidadDeEventos;
    }

    public String getCiudadesDeLosEventos() {
        return ciudadesDeLosEventos;
    }

    public void setCiudadesDeLosEventos(String ciudadesDeLosEventos) {
        this.ciudadesDeLosEventos = ciudadesDeLosEventos;
    }
}
