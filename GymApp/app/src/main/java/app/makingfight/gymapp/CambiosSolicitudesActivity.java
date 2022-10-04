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

import Models.solicitudLuchador;
import adapters.solicitudAdapter;

public class CambiosSolicitudesActivity extends AppCompatActivity {
    RecyclerView recicleViewSolicitudes;
    Toolbar toolbar;
    solicitudAdapter solicitudAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_cambios_solicitudes);
        Bind();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    void Bind(){
        recicleViewSolicitudes=findViewById(R.id.recicleViewSolicitudes);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Solicitudes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query= FirebaseFirestore.getInstance().collection("Solicitudes").whereEqualTo("view",false).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<solicitudLuchador> options= new FirestoreRecyclerOptions.Builder<solicitudLuchador>()
                .setQuery(query,solicitudLuchador.class).build();
        solicitudAdapter= new solicitudAdapter(options,this,this);
        recicleViewSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        recicleViewSolicitudes.setAdapter(solicitudAdapter);
        solicitudAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (solicitudAdapter!=null){
            solicitudAdapter.stopListening();
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