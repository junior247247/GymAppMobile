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

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Models.EventoAndLuchadores;
import adapters.AdapterLuchadoresEvento;

public class LsitLuchadoresEventoActivity extends AppCompatActivity {
    RecyclerView recicleViewPeleadores;
    Toolbar toolbar;
    String idEvento="";
    AdapterLuchadoresEvento adapterLuchadoresEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_lsit_luchadores_evento);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        recicleViewPeleadores=findViewById(R.id.recicleViewPeleadores);
        recicleViewPeleadores.setLayoutManager(new LinearLayoutManager(this));
        idEvento=getIntent().getStringExtra("id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapterLuchadoresEvento!=null){
            adapterLuchadoresEvento.stopListening();
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

    @Override
    protected void onStart() {
        super.onStart();



        Query query= FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("isAcceted","SI").whereEqualTo("idEvento",idEvento).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<EventoAndLuchadores> options = new FirestoreRecyclerOptions.Builder<EventoAndLuchadores>()
                .setQuery(query,EventoAndLuchadores.class)
                .build();
        adapterLuchadoresEvento= new AdapterLuchadoresEvento(options,this,this);
        recicleViewPeleadores.setAdapter(adapterLuchadoresEvento);
        adapterLuchadoresEvento.startListening();
    }
}