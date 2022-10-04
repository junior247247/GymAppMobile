package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Models.Ranking;
import adapters.AdapterRanking;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import Models.UltimaPeleas;
import adapters.AdapterCircuitos;

public class LuchadoresActivity extends AppCompatActivity {
    TextView textViewAlias,textViewNombre,textViewVictorias,textViewDerrotas,textViewEmpates,textViewPeso,textViewPromedioSumision
            ,textViewName2,textViewEdad,textViewAltura
            ,textViewRanchaVictoria,textViewPesoCorporal,textViewRanking,posicionRanking;
    ImageView imageViewLuchador,imageViewFlag;
    Toolbar toolbar;
    ImageButton imagebuttonAmateur,imageButtonPro,imageButtonRanking;
    LinearLayout layoutHiddenAmateur,layoutHiddenPro,collapseAmateur,collapsePro,collapseRanking,layoutHiddenRanking;
    CardView carviewAmateur,cardViewPro,cardViewrANKING;
    RecyclerView recicleViewAmateur,recilceViewPRO,recicleViewRankgins;
    FirebaseFirestore firestore;
    AdapterCircuitos adapterCircuitos;
    AdapterCircuitos adapterCircuitosPro;
    String id="";
    AdapterRanking adapterRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_luchadores);
        Bind();
        id=getIntent().getStringExtra("id");
        firestore=FirebaseFirestore.getInstance();
        if (getIntent().getExtras()!=null){
            getData(getIntent().getStringExtra("id"));
            getRamchas(getIntent().getStringExtra("id"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        posicionRanking=findViewById(R.id.posicionRanking);
        textViewRanking=findViewById(R.id.textViewRanking);
        layoutHiddenRanking=findViewById(R.id.layoutHiddenRanking);
        collapseRanking=findViewById(R.id.collapseRanking);
        cardViewrANKING=findViewById(R.id.cardViewrANKING);
        imageButtonRanking=findViewById(R.id.imageButtonRanking);
        recicleViewRankgins=findViewById(R.id.recicleViewRankgins);
        recicleViewRankgins.setLayoutManager(new LinearLayoutManager(this));

        collapseRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutHiddenRanking.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardViewrANKING,
                            new AutoTransition());
                    layoutHiddenRanking.setVisibility(View.GONE);
                    imageButtonRanking.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardViewrANKING,
                            new AutoTransition());
                    layoutHiddenRanking.setVisibility(View.VISIBLE);
                    imageButtonRanking.setImageResource(R.drawable.up);
                }
            }
        });
        imageButtonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutHiddenRanking.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardViewrANKING,
                            new AutoTransition());
                    layoutHiddenRanking.setVisibility(View.GONE);
                    imageButtonRanking.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardViewrANKING,
                            new AutoTransition());
                    layoutHiddenRanking.setVisibility(View.VISIBLE);
                    imageButtonRanking.setImageResource(R.drawable.up);
                }
            }
        });

        imagebuttonAmateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutHiddenAmateur.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(carviewAmateur,
                            new AutoTransition());
                    layoutHiddenAmateur.setVisibility(View.GONE);
                    imagebuttonAmateur.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(carviewAmateur,
                            new AutoTransition());
                    layoutHiddenAmateur.setVisibility(View.VISIBLE);
                    imagebuttonAmateur.setImageResource(R.drawable.up);
                }
            }
        });
        collapseAmateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (layoutHiddenAmateur.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(carviewAmateur,
                            new AutoTransition());
                    layoutHiddenAmateur.setVisibility(View.GONE);
                    imagebuttonAmateur.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(carviewAmateur,
                            new AutoTransition());
                    layoutHiddenAmateur.setVisibility(View.VISIBLE);
                    imagebuttonAmateur.setImageResource(R.drawable.up);
                }

            }
        });

        imageButtonPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutHiddenPro.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardViewPro,
                            new AutoTransition());
                    layoutHiddenPro.setVisibility(View.GONE);
                    imageButtonPro.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardViewPro,
                            new AutoTransition());
                    layoutHiddenPro.setVisibility(View.VISIBLE);
                    imageButtonPro.setImageResource(R.drawable.up);
                }
            }
        });



        collapsePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (layoutHiddenPro.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardViewPro,
                            new AutoTransition());
                    layoutHiddenPro.setVisibility(View.GONE);
                    imageButtonPro.setImageResource(R.drawable.down);
                }

                else {

                    TransitionManager.beginDelayedTransition(cardViewPro,
                            new AutoTransition());
                    layoutHiddenPro.setVisibility(View.VISIBLE);
                    imageButtonPro.setImageResource(R.drawable.up);
                }

            }
        });

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
    void getRamchas(String id){
        firestore.collection("ranchas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String pedidas=documentSnapshot.getString("perdidas");
                    String ganadas=documentSnapshot.getString("Ganadas");
                    String empates=documentSnapshot.getString("empates");
                    if (Integer.parseInt(ganadas)>10){
                        textViewRanchaVictoria.setText("INVICTO");
                    }else if (Integer.parseInt(ganadas)>=3){
                        textViewRanchaVictoria.setText("RACHA ACTUAL: "+ganadas);
                    }else{
                        textViewRanchaVictoria.setText("");
                    }


                }
            }
        });
    }
    void Bind(){
        recilceViewPRO=findViewById(R.id.recilceViewPRO);
        collapsePro=findViewById(R.id.collapsePro);
        cardViewPro=findViewById(R.id.cardViewPro);
        layoutHiddenPro=findViewById(R.id.layoutHiddenPro);
        imageButtonPro=findViewById(R.id.imageButtonPro);
        layoutHiddenAmateur=findViewById(R.id.layoutHiddenAmateur);
        textViewRanchaVictoria=findViewById(R.id.textViewRanchaVictoria);
        imageViewFlag=findViewById(R.id.imageViewFlag);
        textViewAltura=findViewById(R.id.textViewAltura);
        imagebuttonAmateur=findViewById(R.id.imagebuttonAmateur);
        imageViewLuchador=findViewById(R.id.imageViewLuchador);
        textViewAlias=findViewById(R.id.textViewAlias);
        textViewNombre=findViewById(R.id.textViewNombre);
        textViewVictorias=findViewById(R.id.textViewVictorias);
        textViewDerrotas=findViewById(R.id.textViewDerrotas);
        textViewEmpates=findViewById(R.id.textViewEmpates);
        textViewPeso=findViewById(R.id.textViewPeso);
        collapseAmateur=findViewById(R.id.collapseAmateur);
        carviewAmateur=findViewById(R.id.carviewAmateur);
        textViewPesoCorporal=findViewById(R.id.textViewPesoCorporal);
        textViewName2=findViewById(R.id.textViewName2);
        textViewEdad=findViewById(R.id.textViewEdad);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LUCHADOR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recicleViewAmateur=findViewById(R.id.recicleViewAmateur);
    }

    void getData(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String ganadas=documentSnapshot.getString("ganadas");
                    String empates=documentSnapshot.getString("empates");
                    String perdidas=documentSnapshot.getString("perdidas");
                    String name=documentSnapshot.getString("name");
                    String alias=documentSnapshot.getString("alias");
                    String urlImage=documentSnapshot.getString("urlImage");
                    String peso=documentSnapshot.getString("categoria");
                    String pesoCorpo=documentSnapshot.getString("peso");
                    textViewPesoCorporal.setText("PESO: "+pesoCorpo+" Lbs");
                    String edad=documentSnapshot.getString("edad");
                    String altura=documentSnapshot.getString("altura");
                    String debut=documentSnapshot.getString("debutEnelOctagono");
                    String divicion=documentSnapshot.getString("divicion");

                    textViewAltura.setText(altura+"`");
                    textViewEdad.setText(edad);
                    textViewName2.setText(name.toUpperCase());




                    textViewPeso.setText(divicion.toUpperCase()+" "+peso.toUpperCase());
                    textViewAlias.setText(alias.toUpperCase());
                    textViewNombre.setText(name.toUpperCase());
                    Picasso.with(LuchadoresActivity.this).load(urlImage).fit().placeholder(R.drawable.spiner).into(imageViewLuchador);
                    if (ganadas!=null){
                        textViewVictorias.setText(ganadas);
                    }else{
                        textViewVictorias.setText("0");
                    }
                    if (empates!=null){
                        textViewEmpates.setText(empates);
                    }else{
                        textViewEmpates.setText("0");
                    }
                    if (perdidas!=null){
                        textViewDerrotas.setText(perdidas);
                    }else{
                        textViewDerrotas.setText("0");
                    }
                    String code=documentSnapshot.getString("code");
                    String url="https://countryflagsapi.com/png/"+code;
                    Picasso.with(LuchadoresActivity.this).load(url).into(imageViewFlag);

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=firestore.collection("UltimaPeleas").whereEqualTo("categoria","AMATEUR").whereArrayContains("ids",id).orderBy("timestamp", Query.Direction.DESCENDING);
       Query queryPro=firestore.collection("UltimaPeleas").whereEqualTo("categoria","PRO").whereArrayContains("ids",id).orderBy("timestamp", Query.Direction.DESCENDING);
            Query queryRanking=firestore.collection("Ranking").whereEqualTo("idLuchador",getIntent().getStringExtra("id")).orderBy("posiciontion", Query.Direction.ASCENDING);

         FirestoreRecyclerOptions<Ranking> optionsRanking=new FirestoreRecyclerOptions.Builder<Ranking>().setQuery(queryRanking,Ranking.class).build();
        adapterRanking= new AdapterRanking(optionsRanking);
        recicleViewRankgins.setAdapter(adapterRanking);
        adapterRanking.startListening();


       FirestoreRecyclerOptions<UltimaPeleas> optionsPro= new FirestoreRecyclerOptions.Builder<UltimaPeleas>().setQuery(queryPro,UltimaPeleas.class).build();
        adapterCircuitosPro= new AdapterCircuitos(optionsPro,this,getIntent().getStringExtra("id"));
        recilceViewPRO.setLayoutManager(new LinearLayoutManager(this));
        recilceViewPRO.setAdapter(adapterCircuitosPro);
        adapterCircuitosPro.startListening();

        FirestoreRecyclerOptions<UltimaPeleas>options= new FirestoreRecyclerOptions.Builder<UltimaPeleas>().setQuery(query,UltimaPeleas.class).build();
        adapterCircuitos= new AdapterCircuitos(options,this,getIntent().getStringExtra("id"));
        recicleViewAmateur.setLayoutManager(new LinearLayoutManager(this));
        recicleViewAmateur.setAdapter(adapterCircuitos);
        adapterCircuitos.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapterCircuitos!=null){
            adapterCircuitos.stopListening();
            adapterCircuitosPro.stopListening();
        }
    }
}