package imageprovider;

import java.util.ArrayList;

public class modelChats {
    private String id;
    private String idUser1;
    private String idUser2;
    private long timestamp;
    private String nameUser;
    private String idSesion;
    private ArrayList<String> ids;
    private boolean isInsert;

    public modelChats(){

    }
    public modelChats(String id, String idUser1, String idUser2, long timestamp, ArrayList<String> ids, boolean isInsert,String nameUser,String idSesion) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.timestamp = timestamp;
        this.ids = ids;
        this.isInsert = isInsert;
        this.nameUser=nameUser;
        this.idSesion=idSesion;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public boolean isInsert() {
        return isInsert;
    }

    public void setInsert(boolean insert) {
        isInsert = insert;
    }
}
