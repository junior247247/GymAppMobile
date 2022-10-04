package service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.RemoteInput;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import app.makingfight.gymapp.ChatActivity;
import app.makingfight.gymapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import Models.Messages;
import app.makingfight.gymapp.ShowInfoPeleaActivity;

public class NotificationService extends ContextWrapper {
    private String channel_id="channel";
    private int channel_id2=0;
    private String name="GymApp";
    private String channel_person="personChanle";
    private NotificationManager notificationManager=null;
    private Context context;
    public static final String notification_replay="notificationreplay";

    public NotificationService(Context context){
        super(context);
        this.context=context;
    }

    public NotificationManager getNotificationManager(){
        if (notificationManager==null){
            notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }



    public void showNotificationEvento(String title,String message,String image,String idEvento){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            RemoteViews layout_one=new RemoteViews(context.getPackageName(),R.layout.layout_one_custom);
            RemoteViews layout_two=new RemoteViews(context.getPackageName(),R.layout.layout_two_custom);

            layout_one.setTextViewText(R.id.textViewTitleOne,title);
            layout_one.setTextViewText(R.id.textViewDescriptionNotification,message);
            layout_two.setTextViewText(R.id.textViewTittleNotification,title);
            layout_two.setTextViewText(R.id.textViewDescriptionNotification,message);
            Intent intent= new Intent(context, ShowInfoPeleaActivity.class);
            intent.putExtra("id",idEvento);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,new Random(1000).nextInt(),intent,PendingIntent.FLAG_MUTABLE);

            layout_one.setImageViewBitmap(R.id.imageViewNotificationOne,getBitmapFromURL(image));
            layout_two.setImageViewBitmap(R.id.iamgeViewNotificationTwo, getBitmapFromURL(image));

            NotificationChannel channel= new NotificationChannel(channel_id+"EVENTO",name,NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{500,500,500,500,500});
            getNotificationManager().createNotificationChannel(channel);
            Notification notification= new  Notification.Builder(context,channel_id+"EVENTO")
                    .setSmallIcon(R.drawable.iconsplash)
                    .setContentText(message)
                   .setCustomContentView(layout_one)
                    .setCustomBigContentView(layout_two)
                    .setContentTitle(title)
                    .setAutoCancel(true)


                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.DecoratedCustomViewStyle())
                    .build();


            getNotificationManager().notify(5,notification);
        }else{
            showNotificationOldVersion(title,message,image);
        }


    }


    public void getNotification(String title,String message,String image){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            RemoteViews layout_one=new RemoteViews(context.getPackageName(),R.layout.layout_one_custom);
            RemoteViews layout_two=new RemoteViews(context.getPackageName(),R.layout.layout_two_custom);

            layout_one.setTextViewText(R.id.textViewTitleOne,title);
            layout_one.setTextViewText(R.id.textViewDescriptionNotification,message);
            layout_two.setTextViewText(R.id.textViewTittleNotification,title);
            layout_two.setTextViewText(R.id.textViewDescriptionNotification,message);

            layout_one.setImageViewBitmap(R.id.imageViewNotificationOne,getBitmapFromURL(image));
            layout_two.setImageViewBitmap(R.id.iamgeViewNotificationTwo, getBitmapFromURL(image));

            NotificationChannel channel= new NotificationChannel(channel_id,name,NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{500,500,500,500,500});
            getNotificationManager().createNotificationChannel(channel);
            Notification notification= new  Notification.Builder(context,channel_id)
                    .setSmallIcon(R.drawable.iconsplash)
                    .setContentText(message)
                    .setCustomContentView(layout_one)
                    .setCustomBigContentView(layout_two)
                    .setContentTitle(title)
                    .setStyle(new Notification.DecoratedCustomViewStyle())
                    .build();


            getNotificationManager().notify(5,notification);
        }else{
            showNotificationOldVersion(title,message,image);
        }


    }

    public Notification showNotificationPerson(Bitmap imgProfile, String username, Messages [] messages,String idChat,String idUser){
        Notification notification=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {

            Person person1= new Person.Builder()
                    .setName(username)
                    .setIcon(Icon.createWithBitmap(imgProfile))
                    .build();

            Notification.MessagingStyle messagingStyle= new Notification.MessagingStyle(person1);

            for (Messages m:messages){
                messagingStyle.addMessage(m.getMessage(),m.getTimestamp(),person1);
            }


            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.iconsplash);

            Random random=new Random();
            int n=random.nextInt(100000000);
            Intent intentMessage =new Intent(this,MessageReceiber.class);
            intentMessage.putExtra("idChat",idChat);
            intentMessage.putExtra("idUser",idUser);


            PendingIntent pendingMessage=PendingIntent.getBroadcast(this,n,intentMessage,PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteInput remoteInput=new RemoteInput.Builder(notification_replay).setLabel("Escribe un mensaje...").build();
            Notification.Action action= new Notification.Action.Builder(R.drawable.ic_msj,"Responder",pendingMessage).addRemoteInput(remoteInput).build();

            Intent intent= new Intent(context, ChatActivity.class);
            intent.putExtra("idChat",idChat);
            intent.putExtra("idUser",idUser);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent= PendingIntent.getActivity(context,n,intent,PendingIntent.FLAG_UPDATE_CURRENT);



            NotificationChannel notificationChannel= new NotificationChannel(channel_person+"151","person",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setVibrationPattern(new long[]{500,500,500,500});
            notificationChannel.enableLights(true);
            getNotificationManager().createNotificationChannel(notificationChannel);


            notification= new Notification.Builder(getApplicationContext(),channel_person+"151")
                    .setVibrate(new long[]{500,500,500,500})
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.ic_msj)
                    .setStyle(messagingStyle)
                    .setContentText("asd")
                    .setContentIntent(pendingIntent)
                    .setContentTitle("adads")
                    .addAction(action)
                    .setAutoCancel(true)
                    .build();


        }
        return notification;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public void OldVerion(Bitmap img, Messages[] messages ,String userNmae,String idChat,String idUser){

      String msg="";
    NotificationCompat.MessagingStyle messagingStyle= new NotificationCompat.MessagingStyle("NAME USAER");
    for (int i=0; i<messages.length;i++){
        messagingStyle.addMessage(messages[i].getMessage(),messages[i].getTimestamp(),userNmae);
        msg=msg+messages[i].getMessage();
    }



    Intent intent= new Intent(context,ChatActivity.class);
    intent.putExtra("idChat",idChat);
    intent.putExtra("idUser",idUser);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent=PendingIntent.getActivity(context,new Random(1000).nextInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT);



     RemoteViews layout_one=new RemoteViews(context.getPackageName(),R.layout.layout_custom_caht_notification);

        layout_one.setImageViewBitmap(R.id.imageViewChat,img);
            layout_one.setTextViewText(R.id.textViewMessage,msg);
            layout_one.setTextViewText(R.id.textViewUserName,userNmae);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,channel_id+"1535")

                .setCustomContentView(layout_one)
                .setSmallIcon(R.drawable.iconsplash)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(messagingStyle);


        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(4,builder.build());
    }

    public void showNotificationOldVersion(String title,String message,String image){
        RemoteViews layout_one=new RemoteViews(context.getPackageName(),R.layout.layout_one_custom);
        RemoteViews layout_two=new RemoteViews(context.getPackageName(),R.layout.layout_two_custom);

        layout_one.setTextViewText(R.id.textViewTitleOne,title);
        layout_one.setTextViewText(R.id.textViewDescriptionNotification,message);
        layout_two.setTextViewText(R.id.textViewTittleNotification,title);
        layout_two.setTextViewText(R.id.textViewDescriptionNotification,message);
        layout_one.setImageViewBitmap(R.id.imageViewNotificationOne,getBitmapFromURL(image));
        layout_two.setImageViewBitmap(R.id.iamgeViewNotificationTwo, getBitmapFromURL(image));
        Bitmap bitmap=null;




        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.iconsplash)
                .setCustomContentView(layout_one)
                .setCustomBigContentView(layout_two)
                .setStyle(  new NotificationCompat.DecoratedCustomViewStyle())
                ;
        Random random= new Random(new Date().getTime());
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(3,builder.build());
    }

}
