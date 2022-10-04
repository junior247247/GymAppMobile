package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import Models.CrearVersus;
import Models.EventoAndLuchadores;
import adapters.AdapterCategorias;
import adapters.adapterListaLuchadores;

public class CrearVersusActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reciCleViewVersus;
    Button buttonCrearCombate;
    FirebaseFirestore firestore;
    adapters.adapterListaLuchadores adapterListaLuchadores;
    TextView textViewCategoria;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout linearLayoutCategorias;
    AdapterCategorias adapterCategorias;
    ImageView imageViewShowBottom;
    String idEvento="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_crear_versus);
        reciCleViewVersus=findViewById(R.id.reciCleViewVersus);
        textViewCategoria=findViewById(R.id.textViewCategoria);
        linearLayoutCategorias=findViewById(R.id.linearLayoutCategorias);
        imageViewShowBottom=findViewById(R.id.imageViewShowBottom);
        firestore=FirebaseFirestore.getInstance();
        reciCleViewVersus.setLayoutManager(new LinearLayoutManager(this));
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CREAR COMBATES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonCrearCombate=findViewById(R.id.buttonCrearCombate);
        idEvento=getIntent().getStringExtra("id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        linearLayoutCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowButtonSheetDialod();
            }
        });
        imageViewShowBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowButtonSheetDialod();
            }
        });
        buttonCrearCombate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list=adapters.adapterListaLuchadores.ids;
                if (list.size()==2){
                    if (!textViewCategoria.getText().equals("CATEGORIA")){
                    DocumentReference documentReference= firestore.collection("Combates").document();
                        CrearVersus crearVersus= new CrearVersus();
                        crearVersus.setIdEvento(idEvento);
                        crearVersus.setCategoria(textViewCategoria.getText().toString());
                        crearVersus.setId(documentReference.getId());
                        crearVersus.setIs(false);
                        crearVersus.setTimestamp(new Date().getTime());
                        crearVersus.setIdLuchador1(list.get(0));
                        crearVersus.setIdLuchador2(list.get(1));

                        firestore.collection("Combates").document().set(crearVersus);
                        for (int i=0; i<list.size();i++){
                            firestore.collection("EventoYluchadores").whereEqualTo("idEvento",idEvento).whereEqualTo("idLuchador",list.get(i)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.size()>0){
                                        List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot snapshot: documentSnapshotList){
                                            String id=snapshot.getId();
                                            Map<String,Object>map = new HashMap<>();
                                            map.put("isVersus",true);
                                            firestore.collection("EventoYluchadores").document(id).update(map);
                                        }
                                        adapters.adapterListaLuchadores.ids.clear();
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(CrearVersusActivity.this, "DEBES SELECIONAR LA CATEGORIA", Toast.LENGTH_SHORT).show();
                    }



                }else if (list.size()>2){
                    Toast.makeText(CrearVersusActivity.this, "SELECIONA UNICAMENTE DOS LUCHADORES", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void ShowButtonSheetDialod(){
        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        ArrayList<String> list= new ArrayList(){{
            add("PESO PAJA");
            add("PESO MOSCA");
            add("PESO GALLO");
            add("PESO PLUMA");
            add("PESO WELTER");
            add("PESO MEDIO");
            add("PESO SEMIPESADO");
            add("PESO PESADO");
            add("TODOS LOS PESO");
        }};
        adapterCategorias=new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {

                textViewCategoria.setText(dato);

                bottomSheetDialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterCategorias);


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

    @Override
    protected void onStart() {
        super.onStart();
        Query query=firestore.collection("EventoYluchadores").whereEqualTo("idEvento",idEvento).whereEqualTo("isVersus",false).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<EventoAndLuchadores> options= new FirestoreRecyclerOptions.Builder<EventoAndLuchadores>()
                .setQuery(query,EventoAndLuchadores.class)
                .build();
        adapterListaLuchadores= new adapterListaLuchadores(options,this);
        reciCleViewVersus.setAdapter(adapterListaLuchadores);
        adapterListaLuchadores.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( adapterListaLuchadores!=null){
            adapterListaLuchadores.stopListening();
        }
        if (adapters.adapterListaLuchadores.ids.size()>0){
            adapters.adapterListaLuchadores.ids.clear();
        }
    }
}