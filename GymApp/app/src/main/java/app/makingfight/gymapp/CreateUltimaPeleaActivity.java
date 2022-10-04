package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import Models.UltimaPeleas;
import adapters.AdapterCategorias;

public class CreateUltimaPeleaActivity extends AppCompatActivity  {
    Toolbar toolbar;
    TextView textViewPeso,textViewFecha,textViewCategoria;
    EditText editextNombreLuchador,editextGanadasLuchador,editextPerdidasLuchador,editextEmpatesLuchador,editextnombreOponente,
    editextGanadasOponente,editextPerdidasOponente,editextEmpatesOponente,editextReferencia,editextDuration,editextTipo;
    Button buttonGuardar;
    AlertDialog  alertDialog;
    BottomSheetDialog bottomSheetDialog;
    FirebaseFirestore firestore;
    Switch ganadaSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_create_ultima_pelea);
        firestore=FirebaseFirestore.getInstance();
        Bind();
        getUser(getIntent().getStringExtra("id"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        textViewPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBotomShetDialog();
            }
        });
    buttonGuardar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            create(getIntent().getStringExtra("id"));
        }
    });

    textViewFecha.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showAlert();
        }
    });

        textViewCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showPesos();
            }
        });
        ganadaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ganadaSwitch.setText("SI");
                }else{
                    ganadaSwitch.setText("NO");
                }
            }
        });

    }
    void getUser(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    editextNombreLuchador.setText(name);
                    String ganadas=documentSnapshot.getString("ganadas");
                    String perdidas=documentSnapshot.getString("perdidas");
                    String empates=documentSnapshot.getString("empates");
                    if (!ganadas.isEmpty()){
                        editextGanadasLuchador.setText(ganadas);
                    }else{
                        editextGanadasLuchador.setText("0");
                    }
                    if (!perdidas.isEmpty()){
                        editextPerdidasLuchador.setText(perdidas);
                    }else{
                        editextPerdidasLuchador.setText("0");
                    }
                    if (!empates.isEmpty()){
                        editextEmpatesLuchador.setText(empates);
                    }else{
                        editextEmpatesLuchador.setText("0");
                    }
                }
            }
        });
    }


    void Bind(){
        ganadaSwitch=findViewById(R.id.ganadaSwitch);
        textViewCategoria=findViewById(R.id.textViewCategoria);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewPeso=findViewById(R.id.textViewPeso);
        textViewFecha=findViewById(R.id.textViewFecha);
        editextNombreLuchador=findViewById(R.id.editextNombreLuchador);
        editextGanadasLuchador=findViewById(R.id.editextGanadasLuchador);
        editextPerdidasLuchador=findViewById(R.id.editextPerdidasLuchador);
        editextEmpatesLuchador=findViewById(R.id.editextEmpatesLuchador);
        editextnombreOponente=findViewById(R.id.editextnombreOponente);
        editextGanadasOponente=findViewById(R.id.editextGanadasOponente);
        editextPerdidasOponente=findViewById(R.id.editextPerdidasOponente);
        editextEmpatesOponente=findViewById(R.id.editextEmpatesOponente);
        editextReferencia=findViewById(R.id.editextReferencia);
        buttonGuardar=findViewById(R.id.buttonGuardar);
        editextTipo=findViewById(R.id.editextTipo);
        editextDuration=findViewById(R.id.editextDuration);
    }

    void showPesos(){
        View view=LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);
        ArrayList<String> list= new ArrayList(){{add("AMATEUR");add("PRO");}};
        recyclerView.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {
                textViewCategoria.setText(dato);
                bottomSheetDialog.dismiss();
            }
        }));
        bottomSheetDialog.show();
    }

    void showBotomShetDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        recyclerView.setAdapter(new AdapterCategorias(getCategory(), new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {
                textViewPeso.setText(dato);
                bottomSheetDialog.dismiss();
            }
        }));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
    void  showAlert(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_calendar,null);
        alertDialog= new AlertDialog.Builder(this).setTitle("SELECCIONAR FECHA").setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

        CalendarView calendarView=view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String fecha=day+"/"+month+"/"+year;
                textViewFecha.setText(fecha);
            }
        });
    }
    ArrayList<String> getCategory(){
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

    void create(String id){
        String ganada="";
        if (ganadaSwitch.isChecked()){
            ganada="SI";
        }else{
            ganada="NO";
        }
        String categoria=textViewCategoria.getText().toString().trim();
        String tipo=editextTipo.getText().toString().trim();
        String duration=editextDuration.getText().toString().trim();
        String nameLuchador=editextNombreLuchador.getText().toString().trim();
        String nameOponente=editextnombreOponente.getText().toString().trim();
        String peso=textViewPeso.getText().toString().trim();
        String referencia=editextReferencia.getText().toString().trim();
        String fecha=textViewFecha.getText().toString().trim();
        String ganadasLuchador=editextGanadasLuchador.getText().toString().trim();
        String perdidasLuchador=editextPerdidasLuchador.getText().toString().trim();
        String empatesLuchador=editextEmpatesLuchador.getText().toString().trim();
        String ganadasOponente=editextGanadasOponente.getText().toString().trim();
        String perdidaOponente=editextPerdidasOponente.getText().toString().trim();
        String empateOponentes=editextEmpatesOponente.getText().toString().trim();
        if (!categoria.equals("CATEGORIA") && !duration.isEmpty() && !tipo.isEmpty() && !nameOponente.isEmpty() && !nameLuchador.isEmpty() && !peso.equals("PESO") && !fecha.equals("FECHA") && !referencia.isEmpty() && !ganadasLuchador.isEmpty()
        && !perdidasLuchador.isEmpty() && !empatesLuchador.isEmpty() && !ganadasOponente.isEmpty() && !perdidaOponente.isEmpty()
        && !empateOponentes.isEmpty()){
            UltimaPeleas model= new UltimaPeleas();
            model.setId(id);
            model.setGanadasLuchador(ganadasLuchador);
            model.setPerdidasLuchador(perdidasLuchador);
            model.setEmpatesLuchador(empatesLuchador);
            model.setGanadasOponente(ganadasOponente);
            model.setPerdidasOponente(perdidaOponente);
            model.setEmpatesOponentes(empateOponentes);
            model.setNameLuchador(nameLuchador.toUpperCase());
            model.setNameOponente(nameOponente.toUpperCase());
            model.setDuration(duration);
            model.setTipo(tipo);
            model.setPeso(peso);
            model.setGanoCombate(ganada);
            model.setFecha(fecha);
            model.setReferencia(referencia.toUpperCase());
            model.setTimestamp(new Date().getTime());
            model.setCategoria(categoria);

            firestore.collection("UltimaPeleas").document().set(model);
            Toast.makeText(this, "REGISTRO COMPLETADO CON EXITO", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast toast=Toast.makeText(this,"COMPLETA TODOS LOS CAMPOS",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
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