package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import Models.ResultadosPeleas;
import adapters.AdapterResultadosPeleas;
import app.makingfight.gymapp.R;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ResultActivity extends AppCompatActivity {
    ImageView imageViewEvento;
    Toolbar toolbar;
    RecyclerView recicleViewResultados;
    FirebaseFirestore firestore;
    AdapterResultadosPeleas adapterResultadosPeleas;
    String idEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
       getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_result);
        Bind();
        idEvento=getIntent().getStringExtra("id");
        getMessage(getIntent().getStringExtra("id"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

    }

    void Bind(){
        firestore=FirebaseFirestore.getInstance();
        recicleViewResultados=findViewById(R.id.recicleViewResultados);
        imageViewEvento=findViewById(R.id.imageViewEvento);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESULTADOS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void getMessage(String id){
        firestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                  if (documentSnapshot.exists()) {
                      String urlImage=documentSnapshot.getString("urlImage");
                      Picasso.with(ResultActivity.this).load(urlImage).fit().placeholder(R.drawable.spiner).into(imageViewEvento);
                      String title=documentSnapshot.getString("nombreEvento");

                  }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=firestore.collection("ResultadosPeleas").whereEqualTo("idEvento",idEvento).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ResultadosPeleas> options= new FirestoreRecyclerOptions.Builder<ResultadosPeleas>
                ()
                .setQuery(query,ResultadosPeleas.class)
                .build();
        recicleViewResultados.setLayoutManager(new LinearLayoutManager(this));
        adapterResultadosPeleas=new AdapterResultadosPeleas(options,this,this);
        recicleViewResultados.setAdapter(adapterResultadosPeleas);
        adapterResultadosPeleas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( adapterResultadosPeleas!=null){
            adapterResultadosPeleas.stopListening();
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