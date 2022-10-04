package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DetailNoticiasActivity extends AppCompatActivity {
        ImageView imageViewNoticia;
        Toolbar toolbar;
        TextView textViewTittleNoticia,textViewNoticia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_detail_noticias);
        Bind();
        getNoticia(getIntent().getStringExtra("id"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }
    void Bind(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NOTICIA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageViewNoticia=findViewById(R.id.imageViewNoticia);
        textViewTittleNoticia=findViewById(R.id.textViewTittleNoticia);
        textViewNoticia=findViewById(R.id.textViewNoticia);

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

    void getNoticia(String id){
        FirebaseFirestore.getInstance().collection("Noticias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String image=documentSnapshot.getString("imageUrl");
                    String title=documentSnapshot.getString("titelNoticia");
                    String noticia=documentSnapshot.getString("noticia");
                    Picasso.with(DetailNoticiasActivity.this).load(image).fit().placeholder(R.drawable.spiner).into(imageViewNoticia);
                    textViewTittleNoticia.setText(title.toUpperCase());
                    textViewNoticia.setText(noticia);
                }
            }
        });
    }
}