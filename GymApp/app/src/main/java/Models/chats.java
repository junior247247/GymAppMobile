package Models;

import java.util.ArrayList;

public class chats {
    private String id;
    private String idUser1;
    private String idUser2;
    private ArrayList<String> ids;
    private long timestamp;
    private boolean isWriting;
    public chats(){

    }

    public chats(String id, String idUser1, String idUser2, ArrayList<String> ids, long timestamp, boolean isWriting) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.ids = ids;
        this.timestamp = timestamp;
        this.isWriting = isWriting;
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

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWriting() {
        return isWriting;
    }

    public void setWriting(boolean writing) {
        isWriting = writing;
    }
}
