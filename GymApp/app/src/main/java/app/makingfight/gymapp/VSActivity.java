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

import Models.VS;
import adapters.AdapterVs;

public class VSActivity extends AppCompatActivity {
    RecyclerView recicleViewVS;
    AdapterVs adapterVs;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_vsactivity);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("POSIBLES COMBATES");
        recicleViewVS=findViewById(R.id.recicleViewVS);
        recicleViewVS.setLayoutManager(new LinearLayoutManager(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
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
        Query query= FirebaseFirestore.getInstance().collection("VS").whereEqualTo("idEvento",getIntent().getStringExtra("id")).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<VS>options= new FirestoreRecyclerOptions.Builder<VS>().setQuery(query,VS.class).build();
        adapterVs= new AdapterVs(options,this,this);
        recicleViewVS.setAdapter(adapterVs);
        adapterVs.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapterVs!=null){
            adapterVs.stopListening();
        }
    }
}