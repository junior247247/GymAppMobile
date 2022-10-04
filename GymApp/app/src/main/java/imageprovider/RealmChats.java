package imageprovider;

import androidx.annotation.NonNull;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class RealmChats extends RealmObject {

    @PrimaryKey
    @Nonnull
    private   int id;
    @NonNull
    private String idUser1;
    @NonNull
    private String idUser2;
    @NonNull
    private String nameUser;
    @NonNull
    private String urlImage;
    @Nonnull
    private String idChat;
    @NonNull
    private String idSession;

    public RealmChats(){

    }

    public RealmChats(  String idUser1,  String idUser2,  String nameUser,  String urlImage,String idChat,String idSession) {
        this.id = MyAplication.idChat.getAndIncrement()+1;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.nameUser = nameUser;
        this.urlImage = urlImage;
        this.idChat=idChat;
        this.idSession=idSession;
    }


    public String getIdSession() {
        return idSession;
    }

    public void setIdSession( String idSession) {
        this.idSession = idSession;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat( String idChat) {
        this.idChat = idChat;
    }



    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(@NonNull String idUser1) {
        this.idUser1 = idUser1;
    }


    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(@NonNull String idUser2) {
        this.idUser2 = idUser2;
    }


    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(@NonNull String nameUser) {
        this.nameUser = nameUser;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage( String urlImage) {
        this.urlImage = urlImage;
    }


}
