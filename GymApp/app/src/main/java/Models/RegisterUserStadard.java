package Models;

public class RegisterUserStadard {
    private String id;
    private String name;
    private String email;
    private String permiso;
    public RegisterUserStadard(){

    }

    public RegisterUserStadard(String id, String name, String email,String permiso) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.permiso=permiso;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
