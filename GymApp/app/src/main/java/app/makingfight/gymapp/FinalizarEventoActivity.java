package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Models.CrearVersus;
import adapters.AdapterCombates;
import app.makingfight.gymapp.R;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.FinalizarEvento;

public class FinalizarEventoActivity extends AppCompatActivity {
    EditText editextReultados;
    Button buttonFinlaziar;
    TextView textViewTitleEvento;
    Toolbar toolbar;
    RecyclerView recicleViewFinalizar;
    FirebaseFirestore firestore;
    String idEvento="";
    AdapterCombates adapterCombates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        firestore=FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_finalizar_evento);
        Bind();
        recicleViewFinalizar=findViewById(R.id.recicleViewFinalizar);
        idEvento=getIntent().getStringExtra("id");
        getTittle(idEvento);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        buttonFinlaziar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (adapterCombates.getItemCount()<=0){
                        DocumentReference reference=firestore.collection("EventoFinalizado").document();
                        FinalizarEvento model= new FinalizarEvento();
                        model.setId(reference.getId());
                        model.setIdEvento(getIntent().getStringExtra("id"));
                        model.setLugar(getIntent().getStringExtra("lugar"));
                        model.setFecha(getIntent().getStringExtra("fecha"));
                        model.setTimestamp(new Date().getTime());
                        firestore.collection("EventoFinalizado").document(model.getId()).set(model);
                        Map<String,Object> map= new HashMap<>();
                        map.put("emision","FINALIZADO");
                        firestore.collection("Eventos").document(model.getIdEvento()).update(map);
                        create();
                        finish();
                        showToast("Evento Finalizado");
                    }else{
                        showToast("Completa los campos de los combates");
                    }


            }
        });
    }


    void UpdateEntro(){
        firestore.collection("ResultadosPeleas").whereEqualTo("entro",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){

                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot snapshot:list){
                        String idDocument=snapshot.getId();
                        Map<String,Object> map= new HashMap<>();
                        map.put("entro",true);
                        firestore.collection("ResultadosPeleas").document(idDocument).update(map);
                    }




                }
            }
        });
    }

    void createS(){

    }



    void create(){
        firestore.collection("ResultadosPeleas").whereEqualTo("entro",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    String idGanador="";
                    String idPerdedor="";
                    boolean esEmpate=false;
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    EventoYfotos eventoYfotos= new EventoYfotos();
                    eventoYfotos.setTipo(1);
                    eventoYfotos.setTimestamp(new Date().getTime());
                    eventoYfotos.setIdEvento(idEvento);
                    eventoYfotos.setIdPerdedor("");
                    eventoYfotos.setIdCaganador("");

                    firestore.collection("EventoYfotos").document().set(eventoYfotos);

                    for(DocumentSnapshot snapshot:list){
                        idGanador=snapshot.getString("idGanador");
                        idPerdedor=snapshot.getString("idPerdedor");
                        esEmpate=snapshot.getBoolean("esEmpate");
                        eventoYfotos.setTimestamp(new Date().getTime());
                        eventoYfotos.setTipo(2);
                        eventoYfotos.setIdCaganador(idGanador);
                        eventoYfotos.setIdPerdedor(idPerdedor);
                        eventoYfotos.setEsEmpate(esEmpate);
                        firestore.collection("EventoYfotos").document().set(eventoYfotos);

                    }

                    UpdateEntro();

                }
            }
        });
    }


    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    void getTittle(String id){
        firestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String nombre=documentSnapshot.getString("nombreEvento");
                    textViewTitleEvento.setText(nombre.toUpperCase());
                }
            }
        });
    }

    void Bind(){
        toolbar=findViewById(R.id.toolbar);
        buttonFinlaziar=findViewById(R.id.buttonFinlaziar);
        textViewTitleEvento=findViewById(R.id.textViewTitleEvento);
        recicleViewFinalizar=findViewById(R.id.recicleViewFinalizar);
        recicleViewFinalizar.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FINALIZAR EVENTO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=firestore.collection("Combates").whereEqualTo("is",false).whereEqualTo("idEvento",idEvento).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CrearVersus>options = new  FirestoreRecyclerOptions.Builder<CrearVersus>()
                .setQuery(query,CrearVersus.class)
                .build();
        adapterCombates= new AdapterCombates(options,this,this);
        recicleViewFinalizar.setLayoutManager(new LinearLayoutManager(this));
        recicleViewFinalizar.setAdapter(adapterCombates);
        adapterCombates.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapterCombates!=null){
            adapterCombates.stopListening();
        }
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
}