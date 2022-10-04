package app.makingfight.gymapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class ShowProfileActivity extends AppCompatActivity {
    ImageView imageViewProfile,imageViewBandera;
    TextView textViewAlias,textViewNombreLuchador,textViewPais,textViewEdad,textViewAltura,textViewRegion,textViewPeso,textViewAfiliacion;
    Button buttonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_show_profile);
        Bind();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        getUserRealTime(FirebaseAuth.getInstance().getCurrentUser().getUid());
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ShowProfileActivity.this,UpdateProfileActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ShowProfileActivity.this).toBundle());
            }
        });
    }
    void Bind(){
        textViewAfiliacion=findViewById(R.id.textViewAfiliacion);
        imageViewProfile=findViewById(R.id.imageViewProfile);
        imageViewBandera=findViewById(R.id.imageViewBandera);
        buttonEditar=findViewById(R.id.buttonEditar);
        textViewAlias=findViewById(R.id.textViewAlias);
        textViewNombreLuchador=findViewById(R.id.textViewNombreLuchador);
        textViewPais=findViewById(R.id.textViewPais);
        textViewEdad=findViewById(R.id.textViewEdad);
        textViewAltura=findViewById(R.id.textViewAltura);
        textViewPeso=findViewById(R.id.textViewPeso);
        textViewRegion=findViewById(R.id.textViewRegion);
    }


    void getUserRealTime(String id){
        FirebaseFirestore.getInstance().collection("Luchadores").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (documentSnapshot!=null){
                    if (documentSnapshot.exists()){

                        String url=documentSnapshot.getString("urlImage");
                        String name=documentSnapshot.getString("name");
                        String region=documentSnapshot.getString("region");
                        String pais=documentSnapshot.getString("pais");
                        String alias=documentSnapshot.getString("alias");
                        String code=documentSnapshot.getString("code");
                        String peso=documentSnapshot.getString("peso");
                        String altura=documentSnapshot.getString("altura");
                        String afilacion =documentSnapshot.getString("afiliacion");
                        String debut=documentSnapshot.getString("debutEnelOctagono");
                        String edad=documentSnapshot.getString("edad");
                        textViewEdad.setText("EDAD:"+edad);
                        textViewAfiliacion.setText("AFILIACION:"+afilacion.toUpperCase());
                        textViewNombreLuchador.setText(name.toUpperCase());
                        textViewAlias.setText(alias.toUpperCase());
                        textViewAltura.setText("ALTURA:"+altura);
                        textViewPeso.setText("PESO:"+peso);
                        textViewRegion.setText("REGION:"+region);
                        textViewPais.setText(pais);
                        String urlFlag="https://countryflagsapi.com/png/"+code;
                        Picasso.with(ShowProfileActivity.this).load(urlFlag).fit().into(imageViewBandera);
                        Picasso.with(ShowProfileActivity.this).load(url).fit().into(imageViewProfile);


                    }
                }
            }
        });
    }

    void getUser(String id){
        FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String url=documentSnapshot.getString("urlImage");
                    String name=documentSnapshot.getString("name");
                    String region=documentSnapshot.getString("region");
                    String pais=documentSnapshot.getString("pais");
                    String alias=documentSnapshot.getString("alias");
                    String code=documentSnapshot.getString("code");
                    String peso=documentSnapshot.getString("peso");
                    String altura=documentSnapshot.getString("altura");
                    String afilacion =documentSnapshot.getString("afiliacion");
                    String edad=documentSnapshot.getString("edad");
                    textViewEdad.setText("Edad:"+edad);
                    textViewAfiliacion.setText("Afiliación:"+afilacion.toUpperCase());
                    textViewNombreLuchador.setText(name.toUpperCase());
                    textViewAlias.setText(alias.toUpperCase());
                    textViewAltura.setText("Altura:"+altura);
                    textViewPeso.setText("Peso:"+peso);
                    textViewRegion.setText("Region:"+region);
                    textViewPais.setText(pais);
                    String urlFlag="https://countryflagsapi.com/png/"+code;
                    Picasso.with(ShowProfileActivity.this).load(urlFlag).fit().into(imageViewBandera);
                    Picasso.with(ShowProfileActivity.this).load(url).fit().into(imageViewProfile);
                }
            }
        });
    }
}