package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.EventoAndLuchadores;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RetroFitConfiguration;

public class ShowInfoPeleaActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textViewFecha,textViewHoraEvento,textViewHoraCareo,textViewHoraPrimerPelea,textViewCategoria,textViewReglas,
    textViewPremios,textViewLugar,textViewTitleEvento,textViewFechaLimite;
    Toolbar toolbar;
    Button buttonunirme,buttonListadepeleadores,buttonVs,buttonCrearCombate;
    AlertDialog alertDialog;
    String idEvento="";
    String validating="";
    FirebaseFirestore firestore;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    androidx.appcompat.app.AlertDialog dialog;
    RetroFitConfiguration retroFitConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_show_info_pelea);
        firestore=FirebaseFirestore.getInstance();
        buttonCrearCombate=findViewById(R.id.buttonCrearCombate);
        Bind();
       // showButtoIfIsGym();
        checkIfExisteGym();
        checkIfExisteLuchador();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        if (getIntent().getExtras()!=null){
            Upload(getIntent().getStringExtra("id"));
            idEvento=getIntent().getStringExtra("id");
            HideButton(idEvento);
        }
        buttonVs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ShowInfoPeleaActivity.this,VSActivity.class);
                intent.putExtra("id",idEvento);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ShowInfoPeleaActivity.this).toBundle());
            }
        });


        buttonListadepeleadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ShowInfoPeleaActivity.this,LsitLuchadoresEventoActivity.class);
                Bundle bundle=ActivityOptions.makeSceneTransitionAnimation(ShowInfoPeleaActivity.this).toBundle();
                intent.putExtra("id",idEvento);
                intent.putExtra("title",textViewTitleEvento.getText().toString());
                startActivity(intent,bundle);
            }
        });
        googleSining();

        hideButtonVs();
        showButtomAdmin(auth.getCurrentUser().getUid());
    }
    public  void showButtomAdmin(String id){
        firestore.collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    buttonCrearCombate.setVisibility(View.VISIBLE);
                    buttonCrearCombate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShowInfoPeleaActivity.this,CrearVersusActivity.class);
                            intent.putExtra("id",idEvento);
                            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ShowInfoPeleaActivity.this).toBundle());

                        }
                    });
                }else{
                    buttonCrearCombate.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    void hideButtonVs(){
            firestore.collection("UserAdmin").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        buttonVs.setVisibility(View.VISIBLE);
                    }else{
                        buttonVs.setVisibility(View.GONE);
                    }
                }
            });
    }

    void Bind(){
        textViewFechaLimite=findViewById(R.id.textViewFechaLimite);
        buttonVs=findViewById(R.id.buttonVs);
        buttonListadepeleadores=findViewById(R.id.buttonListadepeleadores);
        imageView=findViewById(R.id.imageViewLuchador);
        textViewFecha=findViewById(R.id.textViewFecha);
        textViewHoraEvento=findViewById(R.id.textViewHoraEvento);
        textViewHoraCareo=findViewById(R.id.textViewHoraCareo);
        textViewHoraPrimerPelea=findViewById(R.id.textViewHoraPrimerPelea);
        textViewCategoria=findViewById(R.id.textViewCategoria);
        textViewReglas=findViewById(R.id.textViewReglas);
        textViewPremios=findViewById(R.id.textViewPremios);
        textViewLugar=findViewById(R.id.textViewLugar);
        textViewTitleEvento=findViewById(R.id.textViewTitleEvento);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonunirme=findViewById(R.id.buttonunirme);
        getSupportActionBar().setTitle("EVENTO");
        alertDialog=new SpotsDialog.Builder().setMessage("ESPERE UN MOMENTO").setContext(this).build();
    }

    void showAlert(String mensaje){
        new AlertDialog.Builder(this).setTitle("ALERTA PARA UNIRSE AL EVENTO")
                .setMessage("FALTAN "+mensaje+" DIAS PARA FINALIZAR LA INSCRIPCION A EL EVEMTO")
                .show();
    }
    void HideButton(String id){

        firestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            if ( documentSnapshot.exists()){

                String fecha;
                fecha=documentSnapshot.getString("fechaLimite");
                String all=fecha.replace("/","-");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1 = null;
                try {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    d1 = df.parse(all+" 13:31:40");
                    Date d2 = df.parse(timeStamp);
                    long diff = d1.getTime() - d2.getTime();
                    long days = diff / (1000 * 60 * 60 * 24);
                    if (days<0){
                        buttonunirme.setVisibility(View.GONE);
                    }else{
                        buttonunirme.setVisibility(View.VISIBLE);
                    }

                    if (days<=10 && days>0){
                        showAlert(days+"");
                    }


                } catch (ParseException e) {
                    Toast.makeText(ShowInfoPeleaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            }
        });
    }
    void Upload(String id){
        firestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    textViewTitleEvento.setText(documentSnapshot.getString("nombreEvento"));
                    textViewCategoria.setText(documentSnapshot.getString("categoria"));
                    textViewFecha.setText(documentSnapshot.getString("fecha"));
                    textViewLugar.setText(documentSnapshot.getString("lugar"));
                    textViewPremios.setText(documentSnapshot.getString("premios"));
                    textViewReglas.setText(documentSnapshot.getString("reglas"));
                    Picasso.with(ShowInfoPeleaActivity.this).load(documentSnapshot.getString("urlImage")).placeholder(R.drawable.spiner).fit().into(imageView);
                    textViewHoraCareo.setText(documentSnapshot.getString("horaCareo"));
                    textViewHoraEvento.setText(documentSnapshot.getString("horaEvento"));
                    textViewFechaLimite.setText(documentSnapshot.getString("fechaLimite"));
                    textViewHoraPrimerPelea.setText(documentSnapshot.getString("horaPrimerPelea"));

                }
            }
        });
    }

  void  checkIfExisteLuchador(){
      firestore.collection("Luchadores").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        buttonunirme.setVisibility(View.VISIBLE);
                        buttonunirme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                esLuchador();
                            }
                        });
                    }
            }
        });
  }
  void checkIfExisteGym(){
      firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    buttonunirme.setVisibility(View.VISIBLE);
                    buttonunirme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                showButtoIfIsGym();



                        }
                    });

                }
            }
        });
  }

    void esLuchador(){
        new AlertDialog.Builder(ShowInfoPeleaActivity.this)
                .setTitle("¿REALMENETE DECEAS ENVIAR LA SOLICITUD PARA ESTE EVENTO?")
                .setMessage("")
                .setNegativeButton("NO",null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        firestore.collection("Eventos").document(idEvento).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    firestore.collection("EventoYluchadores").whereEqualTo("idEvento",idEvento).whereEqualTo("idLuchador",FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (queryDocumentSnapshots.size()<=0){
                                                alertDialog.show();
                                                EventoAndLuchadores andLuchadores= new EventoAndLuchadores();
                                                DocumentReference documentReference=firestore.collection("EventoYluchadores").document();
                                                andLuchadores.setId(documentReference.getId());
                                                andLuchadores.setIdEvento(getIntent().getStringExtra("id"));
                                                andLuchadores.setIdGym("");
                                                andLuchadores.setisVersus(false);
                                                andLuchadores.setTimestamp(new Date().getTime());
                                                andLuchadores.setIsAcceted("NO");
                                                andLuchadores.setIdLuchador(auth.getCurrentUser().getUid());
                                                firestore.collection("EventoYluchadores").document().set(andLuchadores).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        alertDialog.dismiss();
                                                        if (task.isSuccessful()){
                                                            showToas("SOLICITUD PARA EL EVENTO ENVIADA");
                                                            firestore.collection("UserAdmin").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                                                    if (value!=null){
                                                                        if (value.size()>0){
                                                                            for(int i=0; i<value.size();i++){
                                                                                String permiso=value.getDocuments().get(i).getString("aprovacion");

                                                                                if (permiso.equals("si")){
                                                                                    String id=value.getDocuments().get(i).getId();

                                                                                    UserTokenAdmin(id);


                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            });

                                                        }
                                                    }
                                                });

                                            }else{
                                                Toast.makeText(ShowInfoPeleaActivity.this, "YA HAS ENVIADO TU SOLICITUD", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                        });


                    }
                }).show();

    }


    void showToas(String msg){
        Toast toast=Toast.makeText(this,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    void configNotification(String token, Map<String,String> data){
        FrmBody frmBody= new FrmBody(token,"high","4500s",data);
        retroFitConfiguration=new RetroFitConfiguration();
        retroFitConfiguration.senNotification(frmBody).enqueue(new Callback<FrmResponse>() {
            @Override
            public void onResponse(Call<FrmResponse> call, Response<FrmResponse> response) {
                if (response.body()!=null){

                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }

    void googleSining(){
        firestore.collection("Gyms").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    firestore.collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!documentSnapshot.exists()){
                                firestore.collection("Luchadores").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()){
                                            buttonunirme.setVisibility(View.GONE);
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

    void chekisUserStansd(){
        firestore.collection("UserStand").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    buttonunirme.setVisibility(View.GONE);
                }
            }
        });
    }
    void UserTokenAdmin(String id){
        firestore.collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    firestore.collection("Tokens").document(documentSnapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String token=documentSnapshot.getString("token");
                                firestore.collection("Eventos").document(getIntent().getStringExtra("id")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String urlImage=documentSnapshot.getString("urlImage");
                                        String evento=documentSnapshot.getString("nombreEvento");
                                        Map<String,String> map= new HashMap<>();
                                        map.put("title","NUEVA SOLICITUD DE EVENTO");
                                        map.put("message",evento);
                                        map.put("urlimagen",urlImage);
                                        map.put("idEvento",idEvento);
                                        map.put("idChat","");
                                        map.put("idUser","");



                                        map.put("titles","");

                                        map.put("messages","");
                                        map.put("name","");

                                        map.put("idNotification","");

                                        configNotification(token,map);
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });
    }

    void showButtoIfIsGym(){
        firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Intent intent= new Intent(ShowInfoPeleaActivity.this,ListLuchadoresActivity.class);
                    Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(ShowInfoPeleaActivity.this).toBundle();
                    intent.putExtra("id",getIntent().getStringExtra("id"));
                    startActivity(intent,bundle);

                }
            }
        });
    }


}