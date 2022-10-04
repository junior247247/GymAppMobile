package app.makingfight.gymapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import adapters.MessageAdapterRealm;
import app.makingfight.gymapp.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Messages;
import adapters.MessageAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import imageprovider.modelMessages;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RetroFitConfiguration;

public class ChatActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<modelMessages>> {
    Toolbar toolbar;
    String idChat="";
    String idUser="";
    FirebaseAuth auth;
    EditText editextMessge;
    ImageView imageViewSend,imageViewBack;
    RetroFitConfiguration retroFitConfiguration;
    RecyclerView recicleViewMessages;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    CircleImageView circleViewUser;
    FirebaseFirestore firestore;
    TextView textViewUserName;
    TextView textViewEnlinea;
    Realm realm;
    String idManager="";
    RealmResults<modelMessages> modelMessages;
    MessageAdapterRealm messageAdapterRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        auth=FirebaseAuth.getInstance();
        editextMessge=findViewById(R.id.editextMessge);
        imageViewSend=findViewById(R.id.imageViewSend);
        firestore=FirebaseFirestore.getInstance();
        recicleViewMessages=findViewById(R.id.recicleViewMessages);
        linearLayoutManager= new LinearLayoutManager(this);
        recicleViewMessages.setLayoutManager(linearLayoutManager);
         realm=Realm.getDefaultInstance();
        idChat=getIntent().getStringExtra("idChat");
        idUser=getIntent().getStringExtra("idUser");
        modelMessages=realm.where(modelMessages.class).equalTo("idChat",idChat).findAll();
        messageAdapterRealm= new MessageAdapterRealm(this,modelMessages);
        recicleViewMessages.setAdapter(messageAdapterRealm);
        modelMessages.addChangeListener(this);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayoutManager.setStackFromEnd(true);
        viewMessage();

        getUser(idUser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        getAllMessge();

        getMessages();

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editextMessge.getText().toString().trim().isEmpty()){
                    createMessage(editextMessge.getText().toString().trim());
                    editextMessge.setText("");
                }
            }
        });
        imageViewBack=findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


      //  getManger(idUser);
    }

    void  getAllMessge(){
        firestore.collection("Messages").whereEqualTo("idChat",idChat).whereEqualTo("isView",true).orderBy("timestamp", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        if (ChatActivity.this.modelMessages.size()==0){

                       for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                           String idChat=snapshot.getString("idChat");
                           String idSender=snapshot.getString("idSender");
                           String idReceiber=snapshot.getString("idReceiber");
                           String message=snapshot.getString("message");
                           modelMessages modelMessages= new modelMessages(idSender,idReceiber,message,false,idChat);
                           if (ChatActivity.this.modelMessages.size()>0){
                               if (!ChatActivity.this.modelMessages.get(ChatActivity.this.modelMessages.size()-1).getMessage().equals(message)){
                                   realm.beginTransaction();
                                   realm.insert(modelMessages);
                                   realm.commitTransaction();
                               }
                           }else{
                               realm.beginTransaction();
                               realm.insert(modelMessages);
                               realm.commitTransaction();
                           }

                       }
                        }

                    }
            }
        });
    }



    void getMessages(){
        firestore.collection("Messages").whereEqualTo("idReceiber",auth.getCurrentUser().getUid()).whereEqualTo("idChat",idChat).whereEqualTo("isView",false).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    int size=value.size();
                    if (size>0) {
                        for (int i = 0; i < size; i++) {
                            DocumentSnapshot snapshot = value.getDocuments().get(i);
                            String idDocument = snapshot.getId();

                            String idChat = snapshot.getString("idChat");
                            String idReceiber = snapshot.getString("idReceiber");
                            String message = snapshot.getString("message");
                            String idSender = snapshot.getString("idSender");

                            modelMessages modelMessages = new modelMessages(idSender, idReceiber, message, false, idChat);
                            if (ChatActivity.this.modelMessages.size() > 0) {
                                if (!ChatActivity.this.modelMessages.get(ChatActivity.this.modelMessages.size() - 1).getMessage().equals(message)) {
                                    realm.beginTransaction();
                                    realm.insert(modelMessages);
                                    realm.commitTransaction();
                                }
                            } else {
                                realm.beginTransaction();
                                realm.insert(modelMessages);
                                realm.commitTransaction();
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("isView", true);
                            firestore.collection("Messages").document(idDocument).update(map);
                        }
                    }


                }
            }
        });



    }


    void getManger(String id){
        firestore.collection("manger").whereArrayContains("ids",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    idManager=queryDocumentSnapshots.getDocuments().get(0).getString("idNotification");
                }
            }
        });
    }


    void getUser(String id){
        toolbar=findViewById(R.id.toolbar);
        View view= LayoutInflater.from(this).inflate(R.layout.chat_header_layout,null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        firestore.collection("online").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    if (value.exists()){
                        boolean isOnline=value.getBoolean("is online");
                        textViewEnlinea=view.findViewById(R.id.textViewEnlinea);
                        if (isOnline){
                            textViewEnlinea.setText("en linea");
                        }else{
                            textViewEnlinea.setVisibility(View.GONE);
                        }
                    }
                }



            }
        });
        firestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    String urlImge=documentSnapshot.getString("urlImg");
                    circleViewUser=view.findViewById(R.id.circleViewUser);
                    textViewUserName=view.findViewById(R.id.textViewUserName);
                    Picasso.with(ChatActivity.this).load(urlImge).placeholder(R.drawable.profile).into(circleViewUser);
                    textViewUserName.setText(name);
                }
            }
        });
    }




  /*  @Override
    protected void onStart() {
        super.onStart();
        Query query=firestore.collection("Messages").whereEqualTo("idChat",idChat).orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Messages>options= new FirestoreRecyclerOptions.Builder<Messages>().setQuery(query,Messages.class).build();
        messageAdapter= new MessageAdapter(options,this);
        ViewrMessageHelper.updateOnline(true,this);
        messageAdapter.startListening();
        recicleViewMessages.setAdapter(messageAdapter);


        .registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                viewMessage();
                int numberMessaje=messageAdapter.getItemCount();
                int lastMessagePosition=linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if ( lastMessagePosition==-1 || positionStart>=numberMessaje-1 && lastMessagePosition==positionStart-1)
                    recicleViewMessages.scrollToPosition(positionStart);

            }
        });


    }

   */





    void viewMessage(){
        firestore.collection("Messages").whereEqualTo("idChat",idChat).whereEqualTo("idReceiber",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots!=null){
                    if (queryDocumentSnapshots.size()>0){
                        for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                            Map<String,Object>map= new HashMap<>();
                            map.put("visto",true);
                            firestore.collection("Messages").document(snapshot.getId()).update(map);
                        }


                    }
                }


            }
        });
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
                            String id=documentSnapshot.getString("id");
                            map.put("idChat",idChat);
                            map.put("idUser",id);
                            map.put("titles","");
                            map.put("idEvento","");
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
                        Toast.makeText(ChatActivity.this, "notification no enviada", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }
/*
    @Override
    protected void onStop() {
        super.onStop();
        if ( messageAdapter!=null){
            messageAdapter.stopListening();


        }
        ViewrMessageHelper.updateOnline(false,this);
    }

 */

    void createMessage(String msj){

        Messages messages= new Messages();
        DocumentReference documentReference= firestore.collection("Messages").document();
        messages.setId(documentReference.getId());
        messages.setIdChat(idChat);
        messages.setIdSender(auth.getCurrentUser().getUid());
        messages.setIdReceiber(idUser);
        messages.setTimestamp(new Date().getTime());
        messages.setMessage(msj);
        messages.setView(false);
        messages.setVisto(false);
        modelMessages modelMessages= new modelMessages(auth.getCurrentUser().getUid(),idUser,msj,false,idChat);
        if (ChatActivity.this.modelMessages.size()>0){
            if (!this.modelMessages.get(this.modelMessages.size()-1).getMessage().equals(msj)){
                realm.beginTransaction();
                realm.insert(modelMessages);
                realm.commitTransaction();
            }
        }else{
            realm.beginTransaction();
            realm.insert(modelMessages);
            realm.commitTransaction();
        }


        firestore.collection("Messages").document().set(messages);
        creteNofication(messages);
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapterRealm.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            viewMessage();
                int numberMessaje=messageAdapterRealm.getItemCount();
                int lastMessagePosition=linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if ( lastMessagePosition==-1 || positionStart>=numberMessaje-1 && lastMessagePosition==positionStart-1)
                    recicleViewMessages.scrollToPosition(positionStart);
            }
        });
    }

    @Override
    public void onChange(RealmResults<imageprovider.modelMessages> realmResults) {
        messageAdapterRealm.notifyDataSetChanged();
        int position=messageAdapterRealm.getItemCount();
        recicleViewMessages.scrollToPosition(position-1);
        viewMessage();


    }
}