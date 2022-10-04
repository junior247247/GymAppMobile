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
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import Models.Resenas;
import adapters.ResenaAdapter;

public class ResenasActivity extends AppCompatActivity {
    EditText editextResena;
    ImageButton buttonSend;
    RecyclerView recicleViewResenas;
    ResenaAdapter resenaAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_resenas);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reseñas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editextResena=findViewById(R.id.editextResena);
        buttonSend=findViewById(R.id.buttonSend);
        recicleViewResenas=findViewById(R.id.recicleViewResenas);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editextResena.getText().toString().trim().isEmpty()){
                    create(FirebaseAuth.getInstance().getCurrentUser().getUid(),editextResena.getText().toString());
                    editextResena.setText("");
                }
            }
        });
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
        Query query=FirebaseFirestore.getInstance().collection("Resenas").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Resenas> options= new FirestoreRecyclerOptions.Builder<Resenas>().setQuery(query,Resenas.class).build();
        recicleViewResenas.setLayoutManager(new LinearLayoutManager(this));
        resenaAdapter= new ResenaAdapter(options);
        recicleViewResenas.setAdapter(resenaAdapter);
        resenaAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (resenaAdapter!=null){
            resenaAdapter.stopListening();
        }
    }

    void create(String id, String message){
        Resenas resenas= new Resenas();
        DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Resenas").document();
        resenas.setId(documentReference.getId());
        resenas.setMessge(message);
        resenas.setTimestamp(new Date().getTime());
        resenas.setIdUser(id);
        FirebaseFirestore.getInstance().collection("Resenas").document().set(resenas);
    }
}