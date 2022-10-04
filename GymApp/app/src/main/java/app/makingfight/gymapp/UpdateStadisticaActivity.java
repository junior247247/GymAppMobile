package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.AdapterCategorias;
import dmax.dialog.SpotsDialog;

public class UpdateStadisticaActivity extends AppCompatActivity  {
    TextView textViewNombre,textViewCategoria,textViewCategoriaProOamateur;
    TextInputEditText textInputGanadas
            ,textInputPerdidas,textInputEmpates,textInputGanadasRancha;

    ImageView imageViewLuchador;
    Button btnActualizar,buttonRanking;
    Toolbar toolbar;
    String id="";
    AlertDialog alertDialog;
    LinearLayout linearLayoutCategorias,linearLyoutElmejorSeleccion;
    BottomSheetDialog bottomSheetDialog;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_update_stadistica);
        Bind();
        firestore=FirebaseFirestore.getInstance();
        id=getIntent().getStringExtra("id");
        getImageAndNameOfUser(id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        textViewCategoriaProOamateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogDivicion();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
        linearLayoutCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UpdateStadisticaActivity.this,RankingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UpdateStadisticaActivity.this).toBundle());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }
    void showBottomDialogDivicion(){
        View view=LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);
        RecyclerView recyclerView= view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> list= new ArrayList(){{add("Amateur");add("Pro");}};
        recyclerView.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {
                textViewCategoriaProOamateur.setText(dato);
                bottomSheetDialog.dismiss();
            }
        }));

        bottomSheetDialog.show();
    }




    void showBottomDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterCategorias(getCategory(), new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {
                textViewCategoria.setText(dato);
                bottomSheetDialog.dismiss();
            }
        }));
        bottomSheetDialog.show();
    }

    ArrayList<String>getCategory(){
        return new ArrayList<String>(){{
            add("PESO PAJA");
            add("PESO MOSCA");
            add("PESO GALLO");
            add("PESO PLUMA");
            add("PESO WELTER");
            add("PESO MEDIO");
            add("PESO SEMIPESADO");
            add("PESO PESADO");
        }};
    }

    void updateView(String id){
        Map<String,Object> map= new HashMap<>();
        map.put("view",true);
        firestore.collection("Solicitudes").document(id).update(map);
    }

    void getImageAndNameOfUser(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    String urlImge=documentSnapshot.getString("urlImage");
                    String ganadas=documentSnapshot.getString("ganadas");
                    String perdidas=documentSnapshot.getString("perdidas");
                    String empates=documentSnapshot.getString("empates");

                    String categoria=documentSnapshot.getString("categoria");
                    String divicion=documentSnapshot.getString("divicion");



                    textInputGanadas.setText(ganadas);
                    textInputPerdidas.setText(perdidas);
                    textInputEmpates.setText(empates);
                    textViewCategoria.setText(categoria);
                    textViewCategoriaProOamateur.setText(divicion);

                    textViewNombre.setText(name.toUpperCase());
                    Picasso.with(UpdateStadisticaActivity.this).load(urlImge).fit().placeholder(R.drawable.spiner).into(imageViewLuchador);
                        firestore.collection("ranchas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String ganada=documentSnapshot.getString("Ganadas");
                                        textInputGanadasRancha.setText(ganada);
                                    }
                            }
                        });

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

    void rachas(String ganada,String pedida,String empates){
        Map<String,Object> map= new HashMap<>();
        map.put("Ganadas",ganada);
        map.put("perdidas",pedida);
        map.put("empates",empates);
        map.put("idLuchador",id);
        firestore.collection("ranchas").document(id).update(map);

    }

    void Bind(){

        textInputGanadasRancha=findViewById(R.id.textInputGanadasRancha);
        buttonRanking=findViewById(R.id.buttonRanking);
        linearLayoutCategorias=findViewById(R.id.linearLayoutCategorias);
        textViewCategoria=findViewById(R.id.textViewCategoria);
        alertDialog= new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
        textInputEmpates=findViewById(R.id.textInputEmpates);
        textInputPerdidas=findViewById(R.id.textInputPerdidas);
        textInputGanadas=findViewById(R.id.textInputGanadas);



        textViewNombre=findViewById(R.id.textViewNombre);
        imageViewLuchador=findViewById(R.id.imageViewLuchador);
        textViewCategoriaProOamateur=findViewById(R.id.textViewCategoriaProOamateur);
        btnActualizar=findViewById(R.id.btnActualizar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ACTUALIZAR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void Update(){
        String categoria=textViewCategoria.getText().toString().trim();
            String ganadasRancha=textInputGanadasRancha.getText().toString().trim();
            String divicion=textViewCategoriaProOamateur.getText().toString().trim();
            String ganadas=textInputGanadas.getText().toString().trim();
            String perdidas=textInputPerdidas.getText().toString().trim();
            String empates=textInputEmpates.getText().toString().trim();


            if ( !ganadasRancha.isEmpty()  && !empates.isEmpty() && !perdidas.isEmpty() && !ganadas.isEmpty())
            {
                Map<String,Object> map= new HashMap<>();
                rachas(ganadasRancha,"","");
                map.put("empates",empates);
                map.put("ganadas",ganadas);
                map.put("perdidas",perdidas);
                map.put("divicion",divicion);
                map.put("categoria",categoria);
                updateView(id);
                alertDialog.show();
                firestore.collection("Luchadores").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()){
                            showToast("ESTADISTICAS ACTUALIZADAS");
                        }
                        finish();
                    }
                });
            }else{
                showToast("DEBES RELLENAR TODOS LOS CAMPOS");
            }



    }

    void  showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


}