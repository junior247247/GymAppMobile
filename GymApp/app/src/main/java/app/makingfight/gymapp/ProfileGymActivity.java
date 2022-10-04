package app.makingfight.gymapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import Models.addLuchadores;
import adapters.AdapterLuchadoresGymsStand;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileGymActivity extends AppCompatActivity {
        String id="";
        RecyclerView recicleViewListLuchadores;
        FirebaseFirestore firestore;
        AdapterLuchadoresGymsStand adapterLuchadoresDeGimnasio;
        TextView textViewDesdeGym,textViewLugar,textViewNameGym,textViewFacebook,textViewArtesMarciales,textViewTiktok,textViewTwitter,textViewInstagram;
        ImageView imageViewPortada;
        CircleImageView circleOImageViewGym;
        Button btnEditarProfile;
        View viewFacebook,viewTwitter,viewTiktok,viewInstagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_profile_gym);
        id=getIntent().getStringExtra("id");
        firestore=FirebaseFirestore.getInstance();
        viewFacebook=findViewById(R.id.viewFacebook);
        viewTwitter=findViewById(R.id.viewTwitter);
        viewTiktok=findViewById(R.id.viewTiktok);
        viewInstagram=findViewById(R.id.viewInstagram);
        recicleViewListLuchadores=findViewById(R.id.recicleViewListLuchadores);
        textViewDesdeGym=findViewById(R.id.textViewDesdeGym);
        btnEditarProfile=findViewById(R.id.btnEditarProfile);
        textViewLugar=findViewById(R.id.textViewLugar);
        circleOImageViewGym=findViewById(R.id.circleOImageViewGym);
        imageViewPortada=findViewById(R.id.imageViewPortada);
        textViewNameGym=findViewById(R.id.textViewNameGym);
        textViewInstagram=findViewById(R.id.textViewInstagram);
        textViewFacebook=findViewById(R.id.textViewFacebook);
        textViewTwitter=findViewById(R.id.textViewTwitter);
        textViewTiktok=findViewById(R.id.textViewTiktok);
        textViewArtesMarciales=findViewById(R.id.textViewArtesMarciales);

        recicleViewListLuchadores.setLayoutManager(new LinearLayoutManager(this));
        if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            btnEditarProfile.setVisibility(View.VISIBLE);
        }else{
            btnEditarProfile.setVisibility(View.GONE);
        }
        btnEditarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileGymActivity.this,UpdateProfileGymActivity.class);
                intent.putExtra("id",id);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ProfileGymActivity.this).toBundle());
            }
        });
        getData(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    void getData(String id){

        firestore.collection("Gyms").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (documentSnapshot!=null){
                    if (documentSnapshot.exists()){

                        String portada=documentSnapshot.getString("urlImagePortada");
                        String profile=documentSnapshot.getString("urlImageProfile");
                        String instagram=documentSnapshot.getString("instagram");
                        String facebook=documentSnapshot.getString("facebook");
                        String twitter=documentSnapshot.getString("twitter");
                        String tiktok=documentSnapshot.getString("tiktok");



                        if (!tiktok.trim().isEmpty()){
                            if (tiktok.length()>=8){
                                if (tiktok.substring(0,8).trim().equals("https://")){
                                    textViewTiktok.setTextColor(getResources().getColor(R.color.red));
                                    viewTiktok.setVisibility(View.VISIBLE);
                                    textViewTiktok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url=tiktok;
                                            Intent intent= new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(url));
                                            startActivity(intent);
                                        }
                                    });
                                }else{
                                    viewTiktok.setVisibility(View.GONE);
                                    textViewTiktok.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (tiktok.length()>=15){
                                    textViewTiktok.setText(tiktok.substring(0,15)+"...");
                                }else{
                                    textViewTiktok.setText(tiktok);
                                }


                            }

                        }else{
                            textViewTiktok.setText("Tiktok");

                        }

                        if (!instagram.trim().isEmpty()){
                            if (instagram.length()>=8){
                                if (instagram.substring(0,8).trim().equals("https://")){
                                    textViewInstagram.setTextColor(getResources().getColor(R.color.red));
                                    viewInstagram.setVisibility(View.VISIBLE);
                                    textViewInstagram.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url=instagram;
                                            Intent intent= new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(url));
                                            startActivity(intent);
                                        }
                                    });
                                }else{
                                    viewInstagram.setVisibility(View.GONE);
                                    textViewInstagram.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (instagram.length()>=15){
                                    textViewInstagram.setText(instagram.substring(0,15)+"...");
                                }else{
                                    textViewInstagram.setText(instagram);
                                }
                            }

                        }else{
                            textViewInstagram.setText("Instagram");
                        }



                        if (!facebook.trim().isEmpty()){


                            if (instagram.length()>=8){
                                if (instagram.substring(0,8).trim().equals("https://")){
                                    textViewFacebook.setTextColor(getResources().getColor(R.color.red));
                                    viewFacebook.setVisibility(View.VISIBLE);
                                    textViewFacebook.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url=facebook;
                                            Intent intent= new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(url));
                                            startActivity(intent);
                                        }
                                    });
                                    if (facebook.length()>=15){
                                        textViewFacebook.setText(facebook.substring(0,15)+"...");
                                    }else{
                                        textViewFacebook.setText(facebook);
                                    }


                                }else{
                                    viewFacebook.setVisibility(View.GONE);
                                    textViewFacebook.setTextColor(getResources().getColor(R.color.white));
                                }
                            }




                        }else{
                            textViewFacebook.setText("Facebook");
                        }

                        if (!twitter.trim().isEmpty()){
                            if (instagram.length()>=8){
                                if (instagram.substring(0,8).trim().equals("https://")){
                                    textViewTwitter.setTextColor(getResources().getColor(R.color.red));
                                    viewFacebook.setVisibility(View.VISIBLE);
                                    viewTwitter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url=twitter;
                                            Intent intent= new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(url));
                                            startActivity(intent);
                                        }
                                    });
                                }else{
                                    viewTwitter.setVisibility(View.GONE);
                                    textViewTwitter.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (twitter.length()>=15){
                                    textViewTwitter.setText(twitter.substring(0,15)+"...");
                                }else{
                                    textViewTwitter.setText(twitter);
                                }
                            }
                        }else{
                            textViewTwitter.setText("Twitter");
                        }
                        String name=documentSnapshot.getString("nameGym");
                        String desde=documentSnapshot.getString("desde");
                        String hasta =documentSnapshot.getString("hasta");
                        String lugar=documentSnapshot.getString("lugar");
                        String artesmarciaes=documentSnapshot.getString("artesmarciales");
                        textViewNameGym.setText(name.toUpperCase());
                        textViewLugar.setText(lugar.toUpperCase());
                        textViewArtesMarciales.setText(artesmarciaes.toUpperCase());
                        textViewDesdeGym.setText(desde+" - "+hasta);

                        Picasso.with(ProfileGymActivity.this).load(profile).placeholder(R.drawable.spiner).fit().into(circleOImageViewGym);
                        Picasso.with(ProfileGymActivity.this).load(portada).placeholder(R.drawable.spiner).fit().into(imageViewPortada);

                    }
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query= firestore.collection("GymAndLuchadores").whereEqualTo("idGym",id).orderBy("timestamp", Query.Direction.DESCENDING);
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