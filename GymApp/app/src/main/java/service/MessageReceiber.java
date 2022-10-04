package service;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Messages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageReceiber extends BroadcastReceiver {
    int idNotification=10;
    String idChat="";
    String idUser="";
    String idManager="";
    RetroFitConfiguration retroFitConfiguration;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        idChat=intent.getStringExtra("idChat");
        idUser=intent.getStringExtra("idUser");
        String msj=getMessageText(intent).toString();
        createMessage(msj);


        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
         manager.cancel(idNotification);
    }

    void createMessage(String msj){
        Messages messages= new Messages();
        DocumentReference documentReference= firestore.collection("Messages").document();
        messages.setId(documentReference.getId());
        messages.setIdChat(idChat);
        messages.setIdSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
        messages.setIdReceiber(idUser);
        messages.setTimestamp(new Date().getTime());
        messages.setMessage(msj);
        messages.setView(false);
        firestore.collection("Messages").document().set(messages);
        creteNofication(messages);
    }

    private CharSequence getMessageText (Intent intent){
        Bundle bundle= RemoteInput.getResultsFromIntent(intent);
        if (bundle!=null){
            return  bundle.getCharSequence(NotificationService.notification_replay);
        }else {
            return null;
        }
    }
    void creteNofication(Messages message){
        firestore.collection("Messages").whereEqualTo("idChat",idChat).whereEqualTo("isView",false).whereEqualTo("idReceiber",idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Messages> messsageArrayList = new ArrayList<>();
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    if (d.exists()){
                        Messages messsage=d.toObject(Messages.class);
                        messsageArrayList.add(messsage);
                    }
                }
                if (messsageArrayList.size()==0){
                    messsageArrayList.add(message);
                }

                Collections.reverse(messsageArrayList);
                Gson gson= new Gson();
                String messages=gson.toJson(messsageArrayList);
                Map<String,String> map= new HashMap<>();

                firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String name=documentSnapshot.getString("name");
                            String urlImage=documentSnapshot.getString("urlImg");
                            map.put("idChat",idChat);
                            map.put("idUser",idUser);
                            map.put("messages",messages);
                            map.put("name",name);
                            map.put("urlimagen",urlImage);
                            map.put("idNotification",idManager);
                            firestore.collection("Tokens").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String token=documentSnapshot.getString("token");
                                        configNotification(token,map);
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

    }





    void configNotification(String token, Map<String,String> data){
        FrmBody frmBody= new FrmBody(token,"high","4500s",data);
        retroFitConfiguration=new RetroFitConfiguration();
        retroFitConfiguration.senNotification(frmBody).enqueue(new Callback<FrmResponse>() {
            @Override
            public void onResponse(Call<FrmResponse> call, Response<FrmResponse> response) {
                if (response.body()!=null){
                    if (response.body().getSuccess()==1){

                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }
}
