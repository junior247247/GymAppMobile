package imageprovider;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class modelMessages extends RealmObject {

    @PrimaryKey
    private  int id;
    @NonNull
    private String idSender;
    @NonNull
    private String idReceiber;
    @NonNull
    private RealmList<RealmChats> chats;
    @NonNull
    private String message;

    @NonNull
    private boolean Send;
    @NonNull
    private String idChat;
    public modelMessages(){

    }

    public modelMessages(String idSender, String idReceiber,  String message,boolean Send,String idChat) {
        this.id = MyAplication.idMessage.incrementAndGet();
        this.idSender = idSender;
        this.idReceiber = idReceiber;
        this.chats = chats= new RealmList<>();
        this.message = message;
        this.Send=Send;
        this.idChat=idChat;
    }


    public String getIdChat() {
        return idChat;
    }

    public void setIdChat( String idChat) {
        this.idChat = idChat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  boolean getSend(){
        return  Send;
    }

    public  void setSend(boolean send){
        this.Send=send;
    }

    public int getId() {
        return id;
    }


    public String getIdReceiber() {
        return idReceiber;
    }

    public void setIdReceiber(String idReceiber) {
        this.idReceiber = idReceiber;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public RealmList<RealmChats> getChats() {
        return chats;
    }

    public void setChats( RealmList<RealmChats> chats) {
        this.chats = chats;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage( String message) {
        this.message = message;
    }


}
