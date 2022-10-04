package Models;

public class UserAdmin {
    private String id;
    private String nombre;
    private String permiso;
    private String aprovacion;
    private String email;
    public  UserAdmin(){

    }
    public UserAdmin(String id, String nombre, String permiso, String aprovacion,String email) {
        this.id = id;
        this.nombre = nombre;
        this.permiso = permiso;
        this.aprovacion = aprovacion;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getAprovacion() {
        return aprovacion;
    }

    public void setAprovacion(String aprovacion) {
        this.aprovacion = aprovacion;
    }
}
