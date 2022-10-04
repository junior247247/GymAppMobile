package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Models.addLuchadores;
import adapters.AdapterLuchadoresGymsStand;

public class ListLuchadoresInGymActivity extends AppCompatActivity {
    RecyclerView recicleViewListLuchadores;
    Toolbar toolbar;
    AdapterLuchadoresGymsStand adapterLuchadoresDeGimnasio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_list_luchadores_in_gym);
        recicleViewListLuchadores=findViewById(R.id.recicleViewListLuchadores);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Luchadores");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query= FirebaseFirestore.getInstance().collection("GymAndLuchadores").whereEqualTo("idGym", FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<addLuchadores> options= new FirestoreRecyclerOptions.Builder<addLuchadores>().setQuery(query,addLuchadores.class).build();
        adapterLuchadoresDeGimnasio= new AdapterLuchadoresGymsStand(options,this,this);
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
    }


}