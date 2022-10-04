package Models;

import com.google.type.DateTime;

import java.util.Date;
import java.util.TimeZone;

public class Messages {
    private  String id;
    private String idSender;
    private String idChat;
    private String idReceiber;
    private String message;
    private boolean isView;
    private long timestamp;
    private boolean visto;


    public Messages(){

    }

    public Messages(String id, String idSender, String idChat, String idReceiber,String message,long timestamp,boolean isView,boolean visto) {
        this.id = id;
        this.idSender = idSender;
        this.idChat = idChat;
        this.message=message;
        this.idReceiber = idReceiber;
        this.timestamp=timestamp;
        this.isView=isView;
        this.visto=visto;

    }



    public void setVisto(boolean visto) {
        this.visto = visto;
    }
    public boolean getVisto(){
        return visto;
    }

    public boolean getIsView(){
            return isView;
    }

    public void setView(boolean view) {
        isView = view;
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

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getIdReceiber() {
        return idReceiber;
    }

    public void setIdReceiber(String idReceiber) {
        this.idReceiber = idReceiber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
