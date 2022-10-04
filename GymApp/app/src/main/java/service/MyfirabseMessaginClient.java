package service;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

import Models.Messages;

public class MyfirabseMessaginClient extends FirebaseMessagingService {
    NotificationService notificationService;
    @Override
    public void onNewToken(@NonNull  String token) {
        super.onNewToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String,String> map=message.getData();
        String title=map.get("title");
        String msj=map.get("message");
        String titles= map.get("titles");
        String idChat=map.get("idChat");
        String idUser=map.get("idUser");
        String messages=map.get("messages");
        String idNotification=map.get("idNotification");
        String idEvento=map.get("idEvento");
        String name=map.get("name");
        String urlimage=map.get("urlimagen");
        if (title!=null && !title.isEmpty()){
            notificationService= new NotificationService(getApplicationContext());
            notificationService.getNotification(title,msj,urlimage);
        }else if (idChat!=null && !idChat.isEmpty()){
            Gson gson= new Gson();
            Messages[] messages1=gson.fromJson(messages,Messages[].class);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext()).load(urlimage).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                notificationService = new NotificationService(getApplicationContext());
                                Notification notification = notificationService.showNotificationPerson(bitmap, name, messages1, idChat, idUser);
                                notificationService.getNotificationManager().notify(10, notification);
                            }else{
                                notificationService = new NotificationService(getApplicationContext());
                                notificationService.OldVerion(bitmap,messages1,name,idChat,idUser);
                            }


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
            });
        }else if (idEvento!=null && !idEvento.isEmpty()){
            notificationService= new NotificationService(getApplicationContext());
            notificationService.showNotificationEvento(titles,msj,urlimage,idEvento);
        }



    }
}
