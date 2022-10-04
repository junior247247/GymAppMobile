package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import Models.Ranking;
import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.VS;
import Models.chats;
import dmax.dialog.SpotsDialog;
import fragments.BlankFragment;
import fragments.HomeFragment;
import fragments.NoticiasFragment;
import fragments.ResultadosFragment;
import fragments.VideosFragment;
import imageprovider.RealmChats;
import imageprovider.SaveImagerToFirebase;
import imageprovider.modelChats;

import imageprovider.modelMessages;
import io.realm.Realm;
import io.realm.RealmResults;
import service.TokenProvider;

public class HomeActivity extends AppCompatActivity {
    private static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Color.BLACK;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    BottomNavigationView buttonNavigationView;
    FirebaseAuth auth;
    NavigationView navigationView;
    FirebaseFirestore firebaseFirestore;
    SaveImagerToFirebase saveImagerToFirebase;
    AlertDialog alertDialog;
    ImageView imageViewNotification;
    CardView cardView;
    Realm realm;
    RealmChats results;
     RealmChats realmChats;
    RealmResults<RealmChats> listChats;
    String name1="";
    String name2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_GymApp);
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_home);
        firebaseFirestore=FirebaseFirestore.getInstance();
        Bind();
        BindListener();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new HomeFragment()).commit();
        saveImagerToFirebase= new SaveImagerToFirebase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    //setOnline(auth.getCurrentUser().getUid(),true);
       // getMessage();
    CrearVsPosibles();
    setRanking();
/*
        realm.beginTransaction();
        realm.delete(RealmChats.class);
        realm.commitTransaction();

 */




        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.resena:
                        Intent intent= new Intent(HomeActivity.this,ResenasActivity.class);
                        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                        break;
                    case R.id.chat:
                        Intent intent1= new Intent(HomeActivity.this,ListChatsActivity.class);
                        startActivity(intent1,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                }

                return true;
            }
        });





    }








    @Override
    protected void onStart() {
        super.onStart();
        createChat(FirebaseAuth.getInstance().getCurrentUser().getUid());
       // createChatRealm(auth.getCurrentUser().getUid());
       // creteChatInRealm();
        //getChatsList(auth.getCurrentUser().getUid());
    }

    void getChatsList(String id){
        firebaseFirestore.collection("chatslist").whereEqualTo("idDocument",id).whereEqualTo("isSesion",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    for (int i=0;i<queryDocumentSnapshots.size();i++){
                        String idChat=queryDocumentSnapshots.getDocuments().get(i).getString("idChat");
                        String idUser1=queryDocumentSnapshots.getDocuments().get(i).getString("idUser1");
                        String idUser2=queryDocumentSnapshots.getDocuments().get(i).getString("idUser2");
                        String id=queryDocumentSnapshots.getDocuments().get(i).getId();
                        RealmChats modelChats= new RealmChats(idUser1,idUser2,"","",idChat,auth.getCurrentUser().getUid());
                        realm.beginTransaction();
                        realm.insert(modelChats);
                        realm.commitTransaction();
                        Map<String,Object> map= new HashMap<>();
                        map.put("isSesion",true);
                        firebaseFirestore.collection("chatslist").document(id).update(map);
                    }
                }
            }
        });
    }


    void createChatOfSession(String id){

        firebaseFirestore.collection("chats1").whereEqualTo("idSesion",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
              if (value!=null){
                  if (value.size()>0){
                      Toast.makeText(HomeActivity.this, "Existe", Toast.LENGTH_SHORT).show();
                  }else{
                      Toast.makeText(HomeActivity.this, "No existe", Toast.LENGTH_SHORT).show();
                  }
              }
            }
        });




        firebaseFirestore.collection("chats1").whereEqualTo("idSesion",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        for (int i=0;i< queryDocumentSnapshots.size();i++){
                            String idChat=queryDocumentSnapshots.getDocuments().get(i).getString("id");
                            String idUser1=queryDocumentSnapshots.getDocuments().get(i).getString("idUser1");
                            String idUser2=queryDocumentSnapshots.getDocuments().get(i).getString("idUser2");
                            Map<String,Object> map= new HashMap<>();
                            map.put("idChat",idChat);
                            map.put("isSesion",false);
                            map.put("idDocument",id);
                            map.put("idUser1",idUser1);
                            map.put("idUser2",idUser2);
                            firebaseFirestore.collection("chatslist").document().set(map);
                        }
                    }else{
                        firebaseFirestore.collection("chats2").whereEqualTo("idSesion",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size()>0){
                                    for (int i=0; i<queryDocumentSnapshots.size();i++){
                                        String idChat=queryDocumentSnapshots.getDocuments().get(i).getString("id");
                                        String idUser1=queryDocumentSnapshots.getDocuments().get(i).getString("idUser1");
                                        String idUser2=queryDocumentSnapshots.getDocuments().get(i).getString("idUser2");
                                        Map<String,Object> map= new HashMap<>();
                                        map.put("idChat",idChat);
                                        map.put("isSesion",false);
                                        map.put("idDocument",id);
                                        map.put("idUser1",idUser1);
                                        map.put("idUser2",idUser2);
                                        firebaseFirestore.collection("chatslist").document().set(map);
                                    }

                                }
                            }
                        });
                    }
            }
        });

    }



    void getAllMessageSession(String id){
        firebaseFirestore.collection("Messages").whereEqualTo("idReceiber",id).whereEqualTo("idSender",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        for ( DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){
                            String idChat=snapshot.getString("idChat");
                            String idReceiber=snapshot.getString("idReceiber");
                            String message=snapshot.getString("message");
                            String idSender=snapshot.getString("idSender");
                            modelMessages modelMessages= new modelMessages(idSender,idReceiber,message,false,idChat);
                            realm.beginTransaction();
                            realm.insert(modelMessages);
                            realm.commitTransaction();
                        }
                    }
            }
        });
    }


    void getMessage(){
        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("idReceiber",auth.getCurrentUser().getUid()).whereEqualTo("isView",false).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){

                    for (DocumentSnapshot snapshot:value.getDocuments()){
                        String idDocument=snapshot.getId();

                        String idChat=snapshot.getString("idChat");
                        String idReceiber=snapshot.getString("idReceiber");
                        String message=snapshot.getString("message");
                        String idSender=snapshot.getString("idSender");
                        modelMessages modelMessages= new modelMessages(idSender,idReceiber,message,false,idChat);
                        realm.beginTransaction();
                        realm.insert(modelMessages);
                        realm.commitTransaction();


                        Map<String,Object> map= new HashMap<>();
                        map.put("isView",true);
                        firebaseFirestore.collection("Messages").document(idDocument).update(map);
                    }

                }
            }
        });



    }

    void creteChatInRealm(){


        firebaseFirestore.collection("chats1").whereEqualTo("insert",false).whereEqualTo("idSesion",auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (queryDocumentSnapshots!=null){

                        if (queryDocumentSnapshots.size()>0){

                            String idDocument=queryDocumentSnapshots.getDocuments().get(0).getId();
                            String idChat=queryDocumentSnapshots.getDocuments().get(0).getString("id");
                            String idUser1=queryDocumentSnapshots.getDocuments().get(0).getString("idUser1");
                            String idUser2=queryDocumentSnapshots.getDocuments().get(0).getString("idUser2");
                            String userName=queryDocumentSnapshots.getDocuments().get(0).getString("userName");
                            listChats=realm.where(RealmChats.class).equalTo("idChat",idDocument).findAll();
                            if (listChats.size()==0){
                                RealmChats modelChats= new RealmChats(idUser1,idUser2,userName,"",idDocument,auth.getCurrentUser().getUid());

                                realm.beginTransaction();
                                realm.insert(modelChats);
                                realm.commitTransaction();
                            }
                            Map<String,Object> map= new HashMap<>();
                            map.put("insert",true);

                            firebaseFirestore.collection("chats1").document(idDocument).update(map);



                        }else{

                            firebaseFirestore.collection("chats2").whereEqualTo("insert",false).whereEqualTo("idSesion",auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                    if (queryDocumentSnapshots!=null){
                                        if (queryDocumentSnapshots.size()>0){

                                            String idDocument=queryDocumentSnapshots.getDocuments().get(0).getId();
                                            String idChat=queryDocumentSnapshots.getDocuments().get(0).getString("id");
                                            String idUser1=queryDocumentSnapshots.getDocuments().get(0).getString("idUser1");
                                            String idUser2=queryDocumentSnapshots.getDocuments().get(0).getString("idUser2");
                                            String userName=queryDocumentSnapshots.getDocuments().get(0).getString("userName");

                                            listChats=realm.where(RealmChats.class).equalTo("idChat",idDocument).findAll();
                                            if (listChats.size()==0){
                                                RealmChats modelChats= new RealmChats(idUser1,idUser2,userName,"",idDocument,auth.getCurrentUser().getUid());
                                                realm.beginTransaction();
                                                realm.insert(modelChats);
                                                realm.commitTransaction();
                                            }
                                            Map<String,Object> map= new HashMap<>();

                                            map.put("insert",true);
                                            firebaseFirestore.collection("chats2").document(idDocument).update(map);



                                        }


                                    }
                                }
                            });

                        }



                    }
            }
        });

    }






    void createChatRealm(String id){
        firebaseFirestore.collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    firebaseFirestore.collection("UserAdmin").whereEqualTo("aprovacion","si").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            for (int i=0; i<list.size();i++){
                                String id=list.get(i).getId();






                                firebaseFirestore.collection("mainChats").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (!documentSnapshot.exists()){
                                                DocumentReference idChat=firebaseFirestore.collection("chats1").document();
                                                ArrayList<String>milista= new ArrayList<>();
                                                milista.add(id);
                                                milista.add(auth.getCurrentUser().getUid());
                                                modelChats  modelChats= new modelChats();
                                                modelChats.setId(idChat.getId());
                                                modelChats.setIdUser1(auth.getCurrentUser().getUid());
                                                modelChats.setIdUser2(id);
                                                modelChats.setIdSesion(auth.getCurrentUser().getUid());
                                                modelChats.setInsert(false);
                                                modelChats.setIds(milista);
                                                modelChats.setTimestamp(new Date().getTime());
                                                firebaseFirestore.collection("chats1").document(idChat.getId()).set(modelChats);
                                                modelChats.setIdSesion(id);
                                                firebaseFirestore.collection("chats2").document(idChat.getId()).set(modelChats);
                                                Map<String,Object> map= new HashMap<>();
                                                map.put("timestamp",new Date().getTime());
                                                firebaseFirestore.collection("mainChats").document(auth.getCurrentUser().getUid()).set(map);

                                            }


                                        }
                                    });










                            }
                        }
                    });
                }
            }
        });
    }






    void createChat(String id){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()){
                        FirebaseFirestore.getInstance().collection("UserAdmin").whereEqualTo("aprovacion","si").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                for (int i=0; i<list.size();i++){
                                    String id=list.get(i).getId();
                                    chats chats= new chats();
                                    Map<String,Object> map= new HashMap<>();

                                   /* ArrayList<String> list1= new ArrayList<>();
                                    list1.add(id);
                                    list1.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    map.put("idNotification",new Random(new Date().getTime()).nextInt());
                                    map.put("ids",list1);
                                    firebaseFirestore.collection("manger").document().set(map);

                                    */

                                    chats.setId(id+FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    ArrayList<String>milista= new ArrayList<>();
                                    milista.add(id);
                                    milista.add(FirebaseAuth.getInstance().getUid());
                                    chats.setIds(milista);
                                    chats.setTimestamp(new Date().getTime());
                                    chats.setIdUser1(id);
                                    chats.setIdUser2(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                   FirebaseFirestore.getInstance().collection("chats").document(chats.getIdUser1()+chats.getIdUser2()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                       @Override
                                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (!documentSnapshot.exists()){
                                                FirebaseFirestore.getInstance().collection("chats").document(chats.getIdUser1()+chats.getIdUser2()).set(chats);

                                            }
                                       }
                                   });



                                }
                            }
                        });
                    }
            }
        });
    }


    void setOnline(String id,boolean isOnline){
        Map<String,Object> map= new HashMap<>();
        map.put("is online",isOnline);
        firebaseFirestore.collection("online").document(auth.getCurrentUser().getUid()).set(map);
    }

    void CrearVsPosibles(){

         FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("isAcceted","SI").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    List<DocumentSnapshot> list=value.getDocuments();
                    if (list.size()>1){
                        for (int i=0;i<list.size()-1;i++){
                            String idEvento1=list.get(i).getString("idEvento");
                            String idEvento2=list.get(i+1).getString("idEvento");
                            if (idEvento1.equals(idEvento2)){
                                String id=list.get(i).getString("idLuchador");
                                String id2=list.get(i+1).getString("idLuchador");
                                crearVs(id,id2,idEvento1);
                            }

                        }
                    }
                }
            }
        });


    }

    void crearVs(String id1,String id2,String idevento){

        //entre 6 y 10 kilos
        FirebaseFirestore.getInstance().collection("Luchadores").document(id1).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String caregoria1=documentSnapshot.getString("categoria");
                    String peso1=documentSnapshot.getString("peso");
                    int peso1Interger=Integer.parseInt(peso1);
                    String divicion1=documentSnapshot.getString("divicion").toUpperCase();
                    FirebaseFirestore.getInstance().collection("Luchadores").document(id2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String categoria2=documentSnapshot.getString("categoria");
                                String peso2=documentSnapshot.getString("peso");
                                int peso2Interger=Integer.parseInt(peso2);
                                int resulst=0;
                                String divicion2=documentSnapshot.getString("divicion").toUpperCase();
                                if (peso1Interger>peso2Interger){
                                    resulst=peso1Interger-peso2Interger;
                                }else if (peso2Interger>peso1Interger){
                                    resulst=peso2Interger-peso1Interger;
                                }else{
                                    resulst=2;
                                }
                                if (caregoria1.equals(categoria2) && divicion1.equals(divicion2)){
                                    if (resulst>0 || resulst<10){
                                        VS vs= new VS();

                                        vs.setId(id1+id2);
                                        vs.setIdUser1(id1);
                                        vs.setIdEvento(idevento);
                                        vs.setIdUser2(id2);
                                        vs.setTimestamp(new Date().getTime());
                                        FirebaseFirestore.getInstance().collection("VS").document(id1+id2).set(vs);

                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    void Bind(){

        View view= LayoutInflater.from(HomeActivity.this).inflate(R.layout.header_view_toolbar,null);
        // imageViewNotification=view.findViewById(R.id.imageViewNotification);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.homeandroid);
        getSupportActionBar().setTitle("Making Fight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setCustomView(view);
        cardView=view.findViewById(R.id.carViewNotification);
        starListenr();
        isAdminToolbarChange();





     view.findViewById(R.id.imageViewNotification).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent= new Intent(HomeActivity.this,NotificationActivity.class);
             Bundle bundle=ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle();
             startActivity(intent,bundle);


         }
     });






        drawerLayout=findViewById(R.id.drawerLayout);
        auth=FirebaseAuth.getInstance();
        buttonNavigationView=findViewById(R.id.buttonNavigationView);
        TokenProvider tokenProvider= new TokenProvider();
        tokenProvider.CreateToken(auth.getCurrentUser().getUid());
        alertDialog=new SpotsDialog.Builder().setContext(this).setMessage("SUBIENDO...").build();
        //starListenr();
    }





    void starListenr(){
        FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("isAcceted","NO").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    if (value.getDocuments().size()>0){
                        cardView.setVisibility(View.VISIBLE);
                    }else{
                        cardView.setVisibility(View.GONE);
                    }

                }else{

                }
            }
        });
    }

    void BindListener(){




        buttonNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()){
                  case R.id.home:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new HomeFragment()).commit();
                      break;
                  case R.id.atletas:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new BlankFragment()).commit();
                      break;
                  case R.id.video:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new VideosFragment()).commit();
                      break;
                  case R.id.news:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new NoticiasFragment()).commit();
                      break;
                  case R.id.result:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmenContainer,new ResultadosFragment()).commit();
                      break;

              }
              return true;
          }
      });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.cerrarSesion:
               /* createChatOfSession(auth.getCurrentUser().getUid());
                realm.beginTransaction();
                realm.delete(RealmChats.class);
                realm.commitTransaction();

                */
                sinOut();
                break;
            case R.id.agregarLuchadores:
                Intent intent2= new Intent(HomeActivity.this,AddLuchadorActivity.class);
                Bundle bundle2=ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(intent2,bundle2);
                break;
            case R.id.miLIstadeLuchadores:
                Intent intentList= new Intent(HomeActivity.this,ListLuchadoresInGymActivity.class);
                Bundle bundleList=ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle();
                startActivity(intentList,bundleList);
                break;
            case R.id.cerrarSesionUserStand:
                sinOut();
                break;

            case R.id.creaerEvento:
                Intent intent= new Intent(HomeActivity.this,CreateEventosActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle();
                startActivity(intent,bundle);
                break;
            case R.id.cerrarSesionAdmin:
              /*  createChatOfSession(auth.getCurrentUser().getUid());
                Map<String,Object> map= new HashMap<>();
                map.put("id",auth.getCurrentUser().getUid());
                firebaseFirestore.collection("chatslist").document(auth.getCurrentUser().getUid()).set(map);

                Toast.makeText(this, "cerro", Toast.LENGTH_SHORT).show();



                realm.beginTransaction();
                realm.delete(RealmChats.class);
                realm.commitTransaction();

               */
                sinOut();
                break;

            case R.id.subirVideo:
                Intent intentvIDEO=new Intent(HomeActivity.this,SubirVideoActivity.class);
                Bundle bundle1=ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(intentvIDEO,bundle1);
                break;

            case R.id.crearNoticias:
                Intent intent1= new Intent(HomeActivity.this,NoticiasActivity.class);
                startActivity(intent1,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                break;

            case R.id.solicitarCambioCategoria:
                    Intent intent4= new Intent(HomeActivity.this,SolicitarCambioCategoriaActivity.class);
                    startActivity(intent4,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                break;
            case R.id.cerrarSesionLuchador:
                    sinOut();
                break;
            case R.id.solicitudesDeCambios:
                Intent intent5= new Intent(HomeActivity.this,CambiosSolicitudesActivity.class);
                startActivity(intent5,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                break;

            case R.id.miPerfil:
                Intent intent3= new Intent(HomeActivity.this,ShowProfileActivity.class);
                startActivity(intent3,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                break;

            case R.id.miPerfilGym:
                String id=auth.getCurrentUser().getUid();
                Intent intent6 = new Intent(HomeActivity.this,ProfileGymActivity.class);
                intent6.putExtra("id",id);
                startActivity(intent6,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    void sinOut(){
        auth.signOut();
        Intent intent= new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isGymTheUser(menu);
        isAdmin(menu);
        isGoogleLogin(menu);
        isUserStand(menu);
        isLuchador(menu);


        return super.onCreateOptionsMenu(menu);
    }

    void createRankingList(String id,String region){
        firebaseFirestore.collection("Losmejores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    ArrayList<String> list1=(ArrayList<String>) documentSnapshot.get("esmejor");
                    list1.add(region);
                    Map<String,Object>map= new HashMap<>();
                    map.put("esmejor",list1);
                    firebaseFirestore.collection("Losmejores").document(id).update(map);
                }
            }
        });

    }


    void DowmLuchadores(String region,String idGanador,long positionChange){
        firebaseFirestore.collection("Ranking").whereEqualTo("region",region).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot document:list){
                        if (!idGanador.equals(document.getString("idLuchador"))){

                            long posicion=document.getLong("posiciontion");
                            if (positionChange<=posicion){
                                String id=document.getId();
                                posicion=posicion+1;
                                Map<String,Object> map= new HashMap<>();
                                map.put("posiciontion",posicion);
                                firebaseFirestore.collection("Ranking").document(id).update(map);
                            }



                        }
                    }
                }
            }
        });
    }

    void createRanking(String idLuchador,String region,String name,long posicion,long posicionPerdedor,String idGanador){


            firebaseFirestore.collection("Ranking").whereEqualTo("idLuchador",idGanador).whereEqualTo("region",region).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size()==0){
                            Ranking ranking= new Ranking();
                            ranking.setIdLuchador(idGanador);
                            ranking.setRegion(region);
                            ranking.setName(name);
                            ranking.setTimestamp(new Date().getTime());
                            ranking.setPosiciontion(posicion);
                            firebaseFirestore.collection("Ranking").document().set(ranking);
                            createRankingList(idGanador,region);
                            DowmLuchadores(region,idGanador,posicionPerdedor);
                        }
                }
            });






    }

    void setRanking(){
        firebaseFirestore.collection("ResultadosPeleas").whereEqualTo("isView",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     if (queryDocumentSnapshots.size()>0){
                         for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                             String id=snapshot.getId();
                             Map<String,Object> map= new HashMap<>();
                             map.put("isView",true);
                             firebaseFirestore.collection("ResultadosPeleas").document(id).update(map);
                             String idGanador=snapshot.getString("idGanador");
                             String idPerdedor=snapshot.getString("idPerdedor");
                            firebaseFirestore.collection("Ranking").whereEqualTo("idLuchador",idGanador).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.size()>0){
                                    String nameGanador=queryDocumentSnapshots.getDocuments().get(0).getString("name");
                                        firebaseFirestore.collection("Ranking").whereEqualTo("idLuchador",idPerdedor).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (queryDocumentSnapshots.size()>0){
                                                    List<DocumentSnapshot> rankingPerdedor=queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot snapshot1:rankingPerdedor){
                                                            String region=snapshot1.getString("region");
                                                            long posicionPerdedor=snapshot1.getLong("posiciontion");
                                                            createRanking(idPerdedor,region,nameGanador,posicionPerdedor,posicionPerdedor,idGanador);
                                                           // Toast.makeText(HomeActivity.this, "Entro", Toast.LENGTH_SHORT).show();

                                                        }





                                                    }

                                                }

                                        });
                                    }
                                }
                            });
                         }
                     }
            }
        });
    }
    void isGymTheUser(Menu menu){
        firebaseFirestore.collection("Gyms").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    getMenuInflater().inflate(R.menu.menu_gym_register,menu);
                }
            }
        });
    }

    void isAdmin(Menu menu){
        firebaseFirestore.collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        if (documentSnapshot.getString("aprovacion").equals("si")){
                            getMenuInflater().inflate(R.menu.menu_admin,menu);
                        }else{
                            getMenuInflater().inflate(R.menu.menu_user_standard,menu);
                        }


                    }
            }
        });

    }

    void isAdminToolbarChange(){
        firebaseFirestore.collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("aprovacion").equals("si")){


                        getSupportActionBar().setTitle("");
                        getSupportActionBar().setDisplayShowCustomEnabled(true);


                      //  starListenr();





                    }


                }
            }
        });

    }

    void isGoogleLogin(Menu menu){
        firebaseFirestore.collection("Gyms").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()){
                       firebaseFirestore.collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                           @Override
                           public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!documentSnapshot.exists()){
                                    firebaseFirestore.collection("Luchadores").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (!documentSnapshot.exists()){
                                                firebaseFirestore.collection("UserStand").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (!documentSnapshot.exists()){
                                                            getMenuInflater().inflate(R.menu.menu_user_standard,menu);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                           }
                       });
                    }
            }
        });
    }

    void isLuchador(Menu menu){
        firebaseFirestore.collection("Luchadores").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    getMenuInflater().inflate(R.menu.menu_luchador,menu);

                }
            }
        });
    }
    void isUserStand(Menu menu){
        firebaseFirestore.collection("UserStand").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    getMenuInflater().inflate(R.menu.menu_user_standard,menu);

                }
            }
        });
    }



}