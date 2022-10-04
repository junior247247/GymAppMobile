package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.EventoAndLuchadores;
import Models.addLuchadores;
import adapters.AdapterLuchadoresDeGimnasio;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RetroFitConfiguration;

public class ListLuchadoresActivity extends AppCompatActivity implements AdapterLuchadoresDeGimnasio.setCountListener{
        RecyclerView recicleViewListLuchadores;
        Toolbar toolbar;
        Button buttonAgregar;
        TextView textViewCount;
       AlertDialog alertDialog;
       String idEvento;
      RetroFitConfiguration retroFitConfiguration;
    AdapterLuchadoresDeGimnasio adapterLuchadoresDeGimnasio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_list_luchadores);
        Bind();
        if (getIntent().getExtras()!=null){
             idEvento=getIntent().getStringExtra("id");
            buttonAgregar.setVisibility(View.VISIBLE);
            buttonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ListLuchadoresActivity.this).setTitle("¿Realmente (deseas) inscribir estos luchadores a este evento?")
                            .setMessage("UNA VEZ ENVIADA LA SOLICITUD, EL EQUIPO DE MAKING FIGHT ESTARÁ AL PENDIENTE DE DICHA SOLICITUD")
                            .setNegativeButton("NO",null)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ArrayList<String> ids=AdapterLuchadoresDeGimnasio.ids;
                                    int size=ids.size();
                                    if (ids.size()>0){
                                        for (int j=0;j<ids.size();j++){
                                            BindLuchadoresConEvento(idEvento,FirebaseAuth.getInstance().getCurrentUser().getUid(),ids.get(j));
                                            if (size==j+1){
                                                FirebaseFirestore.getInstance().collection("UserAdmin").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        List<DocumentSnapshot> list= queryDocumentSnapshots.getDocuments();

                                                        for (DocumentSnapshot documentSnapshot: list){
                                                            String id=documentSnapshot.getId();
                                                            UserTokenAdmin(id);
                                                        }
                                                    }
                                                });

                                                showToas("SOLICITUD PARA EL EVENTO ENVIADA");

                                                AdapterLuchadoresDeGimnasio.ids.clear();
                                                finish();
                                            }
                                        }
                                    }else{
                                        showToas("DEBES DE SELECIONAR ALGUN LUCHADOR DE TU GIMNASIO");
                                    }
                                }
                            })
                            .show();

                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

    }


    void BindLuchadoresConEvento(String idEvento,String idGym,String idLuchador){
        EventoAndLuchadores andLuchadores= new EventoAndLuchadores();
        DocumentReference documentReference=FirebaseFirestore.getInstance().collection("EventoYluchadores").document();
        andLuchadores.setId(documentReference.getId());
        andLuchadores.setIdEvento(idEvento);
        andLuchadores.setIdGym(idGym);
        andLuchadores.setisVersus(false);
        andLuchadores.setTimestamp(new Date().getTime());
        andLuchadores.setIsAcceted("NO");
        andLuchadores.setIdLuchador(idLuchador);

        FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("idLuchador",idLuchador).whereEqualTo("idEvento",idEvento).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()==0){
                    //if (!queryDocumentSnapshots.getDocuments().get(0).exists()){
                        FirebaseFirestore.getInstance().collection("EventoYluchadores").document().set(andLuchadores);
                    //}
                }
            }
        });

    }



    void UserTokenAdmin(String id){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    FirebaseFirestore.getInstance().collection("Tokens").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String token=documentSnapshot.getString("token");
                                FirebaseFirestore.getInstance().collection("Eventos").document(idEvento).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String urlImage=documentSnapshot.getString("urlImage");
                                        String evento=documentSnapshot.getString("nombreEvento");
                                        Map<String,String> map= new HashMap<>();
                                        map.put("title",evento);
                                        map.put("message","NUEVA SOLICITUD DE EVENTO");
                                        map.put("urlimagen",urlImage);
                                        map.put("idUser","");
                                        map.put("idChat","");



                                        map.put("titles","");
                                        map.put("idEvento","");
                                        map.put("messages","");
                                        map.put("name","");
                                        map.put("urlimagen",urlImage);
                                        map.put("idNotification","");

                                        configNotification(token,map);
                                    }
                                });
                            }else{
                                Toast.makeText(ListLuchadoresActivity.this, "No existe el token", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
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
                        Toast.makeText(ListLuchadoresActivity.this, "Se envio la notification", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ListLuchadoresActivity.this, "No se envio", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }


    void Bind(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Luchadores");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recicleViewListLuchadores=findViewById(R.id.recicleViewListLuchadores);
        buttonAgregar=findViewById(R.id.buttonAgregar);
        textViewCount=findViewById(R.id.itmesSlecetion);
        alertDialog= new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=FirebaseFirestore.getInstance().collection("GymAndLuchadores").whereEqualTo("idGym", FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<addLuchadores> options= new FirestoreRecyclerOptions.Builder<addLuchadores>().setQuery(query,addLuchadores.class).build();
        adapterLuchadoresDeGimnasio= new AdapterLuchadoresDeGimnasio(options,this,this,"",this);
        recicleViewListLuchadores.setLayoutManager(new LinearLayoutManager(this));
        recicleViewListLuchadores.setAdapter(adapterLuchadoresDeGimnasio);
        adapterLuchadoresDeGimnasio.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapterLuchadoresDeGimnasio!=null){
            adapterLuchadoresDeGimnasio.stopListening();
        }
        if (AdapterLuchadoresDeGimnasio.ids.size()>0){
            AdapterLuchadoresDeGimnasio.ids.clear();
        }
    }

    @Override
    public void CountItmen(int count,String id) {
        textViewCount.setText(""+count);
    }
    void showToas(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}