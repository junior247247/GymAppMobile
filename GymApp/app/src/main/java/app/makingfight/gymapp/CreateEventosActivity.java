package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Eventos;
import adapters.AdapterCategorias;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RetroFitConfiguration;

public class CreateEventosActivity extends AppCompatActivity {
    AlertDialog alertDialog,alertDialogLoading;
    TextInputEditText textInputNombreDeElEvento,textInputHoraDeElEvento,textInputMinDeElevento,textInputhoraDeCareo,textInputMinDeCareo,textInpurHoraPrimerCombate
            ,textInputMinPrimerCombate,textInputPremios,textInputLugarDeElEvento;
    Spinner spinnerDeElEvento,spinnerDeElCareo,spinnerPrimerCombate;
    TextView textViewFecha,textViewCategoria,textViewReglas,textViewFechaLimite;
    AdapterCategorias  adapterCategorias;
    Button buttonSelecionarFecha,buttonSelecionarCategoriaDeElEvento,buttonReglas,buttonCrearEvento,buttonSelecionarFechalIMITE;
   Toolbar toolbar;
    BottomSheetDialog bottomSheetDialog;
    SaveImagerToFirebase saveImagerToFirebase;
    ImageView imageViewEvento;
    final int REQUEST_CODE_PORTADA=10;
    FirebaseFirestore firebaseFirestore;
    RetroFitConfiguration retroFitConfiguration;
    FirebaseAuth auth;
    boolean isImage=false;
    byte[] img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_create_eventos);
            BindUid();
            SetListener();
        setSpinners();
        getImageReosucer();
        if (getIntent().getExtras()!=null){
            Upload(getIntent().getStringExtra("id"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

    }
    void updateNotImage(String id){

        String lugar=textInputLugarDeElEvento.getText().toString().trim();
        String nombreEvento=textInputNombreDeElEvento.getText().toString().trim();
        String horaEvento=textInputHoraDeElEvento.getText().toString().trim();
        String minEvento=textInputMinDeElevento.getText().toString().trim();
        String horaCareo=textInputhoraDeCareo.getText().toString().trim();
        String minCareo=textInputMinDeCareo.getText().toString().trim();
        String horaPrimerComabate=textInpurHoraPrimerCombate.getText().toString().trim();
        String minPrimerCombate=textInputMinPrimerCombate.getText().toString().trim();
        String premios=textInputPremios.getText().toString().trim();
        String reglas=textViewReglas.getText().toString().trim();
        String fecha=textViewFecha.getText().toString().trim();
        String spinerEvento=spinnerDeElEvento.getSelectedItem().toString();
        String spinerCareo=spinnerDeElEvento.getSelectedItem().toString();
        String spinerprimerCombate=spinnerPrimerCombate.getSelectedItem().toString();
        String categoria=textViewCategoria.getText().toString().trim();
        String horaDelEventoCompleta=horaEvento+":"+minEvento+":"+spinerEvento;
        String horaDelPrimerGombateCompleta=horaPrimerComabate+":"+minPrimerCombate+":"+spinerprimerCombate;
        String horaDelCareoCompleta=horaCareo+":"+minCareo+":"+spinerCareo;
        String fechaLimite=textViewFechaLimite.getText().toString().trim();

        int horaInterger=Integer.parseInt(horaCareo);
        int minCareoIntergeer=Integer.parseInt(minCareo);

        int horaeventoInterger=Integer.parseInt(horaEvento);
        int minEventoCreo=Integer.parseInt(minEvento);

        int horaPrimerCombateInteger=Integer.parseInt(horaPrimerComabate);
        int minPrimerCombateInteger=Integer.parseInt(minPrimerCombate);

        if (!fechaLimite.isEmpty() && !nombreEvento.isEmpty() && !horaCareo.isEmpty() && !minCareo.isEmpty() && !horaEvento.isEmpty() && !minEvento.isEmpty() &&
                !horaPrimerComabate.isEmpty() && !minPrimerCombate.isEmpty() && !lugar.isEmpty() && !reglas.equals("REGLAS") && !fecha.equals("00/00/0000") &&
                !categoria.equals("----------")){
            if (horaInterger>0 && horaInterger<=23 && minCareoIntergeer <60 && horaeventoInterger>0 && horaeventoInterger<=23 && minEventoCreo <60 && horaPrimerCombateInteger>0 && horaPrimerCombateInteger<=23 && minPrimerCombateInteger<60 ) {

                alertDialogLoading.show();
                Eventos eventos = new Eventos();
                eventos.setNombreEvento(nombreEvento);
                eventos.setCategoria(categoria);
                eventos.setHoraCareo(horaDelCareoCompleta);
                eventos.setHoraEvento(horaDelEventoCompleta);
                eventos.setHoraPrimerPelea(horaDelPrimerGombateCompleta);
                eventos.setFecha(fecha);
                eventos.setReglas(reglas);
                eventos.setPremios(premios);
                eventos.setFechaLimite(fechaLimite);
                eventos.setEmision("EN EMISION");
                eventos.setLugar(lugar);
                Map<String,Object> map= new HashMap<>();
                map.put("categoria",eventos.getCategoria());
                map.put("fecha",eventos.getFecha());
                map.put("fechaLimite",eventos.getFechaLimite());
                map.put("horaCareo",eventos.getHoraCareo());
                map.put("horaEvento",eventos.getHoraEvento());
                map.put("horaPrimerPelea",eventos.getHoraPrimerPelea());
                map.put("lugar",eventos.getLugar());
                map.put("nombreEvento",eventos.getNombreEvento());
                map.put("premios",eventos.getPremios());
                map.put("reglas",eventos.getReglas());
                firebaseFirestore.collection("Eventos").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        alertDialogLoading.dismiss();
                        showToast("DATOS ACTUALIZADOS");
                        finish();
                    }
                });

            }else{
                showToast("HORA NO VALIDA");
            }

        }else{
            showToast("COMPLETA TODOS LOS CAMPOS");
        }

    }
    void  updateWithImage(String id){
        String lugar=textInputLugarDeElEvento.getText().toString().trim();
        String nombreEvento=textInputNombreDeElEvento.getText().toString().trim();
        String horaEvento=textInputHoraDeElEvento.getText().toString().trim();
        String minEvento=textInputMinDeElevento.getText().toString().trim();
        String horaCareo=textInputhoraDeCareo.getText().toString().trim();
        String minCareo=textInputMinDeCareo.getText().toString().trim();
        String horaPrimerComabate=textInpurHoraPrimerCombate.getText().toString().trim();
        String minPrimerCombate=textInputMinPrimerCombate.getText().toString().trim();
        String premios=textInputPremios.getText().toString().trim();
        String reglas=textViewReglas.getText().toString().trim();
        String fecha=textViewFecha.getText().toString().trim();
        String spinerEvento=spinnerDeElEvento.getSelectedItem().toString();
        String spinerCareo=spinnerDeElEvento.getSelectedItem().toString();
        String spinerprimerCombate=spinnerPrimerCombate.getSelectedItem().toString();
        String categoria=textViewCategoria.getText().toString().trim();
        String horaDelEventoCompleta=horaEvento+":"+minEvento+":"+spinerEvento;
        String horaDelPrimerGombateCompleta=horaPrimerComabate+":"+minPrimerCombate+":"+spinerprimerCombate;
        String horaDelCareoCompleta=horaCareo+":"+minCareo+":"+spinerCareo;
        String fechaLimite=textViewFechaLimite.getText().toString().trim();

        int horaInterger=Integer.parseInt(horaCareo);
        int minCareoIntergeer=Integer.parseInt(minCareo);

        int horaeventoInterger=Integer.parseInt(horaEvento);
        int minEventoCreo=Integer.parseInt(minEvento);

        int horaPrimerCombateInteger=Integer.parseInt(horaPrimerComabate);
        int minPrimerCombateInteger=Integer.parseInt(minPrimerCombate);

        if (!fechaLimite.isEmpty() && !nombreEvento.isEmpty() && !horaCareo.isEmpty() && !minCareo.isEmpty() && !horaEvento.isEmpty() && !minEvento.isEmpty() &&
                !horaPrimerComabate.isEmpty() && !minPrimerCombate.isEmpty() && !lugar.isEmpty() && !reglas.equals("REGLAS") && !fecha.equals("00/00/0000") &&
                !categoria.equals("----------")){
            if (horaInterger>0 && horaInterger<=23 && minCareoIntergeer <60 && horaeventoInterger>0 && horaeventoInterger<=23 && minEventoCreo <60 && horaPrimerCombateInteger>0 && horaPrimerCombateInteger<=23 && minPrimerCombateInteger<60 ){
                alertDialogLoading.show();
                saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        alertDialogLoading.dismiss();
                        if (task.isSuccessful()){
                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    Eventos eventos= new Eventos();
                                    DocumentReference documentReference=firebaseFirestore.collection("Eventos").document();
                                    eventos.setId(documentReference.getId());
                                    eventos.setUrlImage(url);
                                    eventos.setNombreEvento(nombreEvento);
                                    eventos.setCategoria(categoria);
                                    eventos.setHoraCareo(horaDelCareoCompleta);
                                    eventos.setHoraEvento(horaDelEventoCompleta);
                                    eventos.setHoraPrimerPelea(horaDelPrimerGombateCompleta);
                                    eventos.setFecha(fecha);
                                    eventos.setReglas(reglas);
                                    eventos.setPremios(premios);
                                    eventos.setFechaLimite(fechaLimite);
                                    eventos.setEmision("EN EMISION");
                                    eventos.setLugar(lugar);
                                    eventos.setTimestamp(new Date().getTime());
                                    Map<String,Object> map= new HashMap<>();
                                    map.put("fechaLimite",eventos.getFechaLimite());
                                    map.put("categoria",eventos.getCategoria());
                                    map.put("fecha",eventos.getFecha());
                                    map.put("horaCareo",eventos.getHoraCareo());
                                    map.put("horaEvento",eventos.getHoraEvento());
                                    map.put("horaPrimerPelea",eventos.getHoraPrimerPelea());
                                    map.put("lugar",eventos.getLugar());
                                    map.put("nombreEvento",eventos.getNombreEvento());
                                    map.put("premios",eventos.getPremios());
                                    map.put("reglas",eventos.getReglas());
                                    map.put("urlImage",eventos.getUrlImage());
                                    firebaseFirestore.collection("Eventos").document(id).update(map);
                                    showToast("DATOS ACTUALIZADOS");
                                    finish();
                                }
                            });
                        }
                    }
                });
            }else{
                showToast("hora invalida");
            }
        }else{
            showToast("completa todos los campos");
        }


    }
    void Upload(String id){
        firebaseFirestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    textInputNombreDeElEvento.setText(documentSnapshot.getString("nombreEvento"));
                    textViewCategoria.setText(documentSnapshot.getString("categoria"));
                    textViewFecha.setText(documentSnapshot.getString("fecha"));
                    textViewFechaLimite.setText(documentSnapshot.getString("fechaLimite"));
                    textInputLugarDeElEvento.setText(documentSnapshot.getString("lugar"));
                    textInputPremios.setText(documentSnapshot.getString("premios"));
                    textViewReglas.setText(documentSnapshot.getString("reglas"));
                    Picasso.with(CreateEventosActivity.this).load(documentSnapshot.getString("urlImage")).placeholder(R.drawable.spiner).into(imageViewEvento);

                    if (documentSnapshot.getString("horaCareo").length()==7){
                        textInputhoraDeCareo.setText(documentSnapshot.getString("horaCareo").substring(0,1));
                        textInputMinDeCareo.setText(documentSnapshot.getString("horaCareo").substring(2,4));

                    }else{
                        textInputhoraDeCareo.setText(documentSnapshot.getString("horaCareo").substring(0,2));
                        textInputMinDeCareo.setText(documentSnapshot.getString("horaCareo").substring(3,5));
                    }

                    if (documentSnapshot.getString("horaEvento").length()==7){
                        textInputHoraDeElEvento.setText(documentSnapshot.getString("horaEvento").substring(0,1));
                        textInputMinDeElevento.setText(documentSnapshot.getString("horaEvento").substring(2,4));
                    }else{
                        textInputHoraDeElEvento.setText(documentSnapshot.getString("horaEvento").substring(0,2));
                        textInputMinDeElevento.setText(documentSnapshot.getString("horaEvento").substring(3,5));
                    }

                    if (documentSnapshot.getString("horaPrimerPelea").length()==7){
                        textInpurHoraPrimerCombate.setText(documentSnapshot.getString("horaPrimerPelea").substring(0,1));
                        textInputMinPrimerCombate.setText(documentSnapshot.getString("horaPrimerPelea").substring(2,4));
                    }else{
                        textInpurHoraPrimerCombate.setText(documentSnapshot.getString("horaPrimerPelea").substring(0,2));
                        textInputMinPrimerCombate.setText(documentSnapshot.getString("horaPrimerPelea").substring(3,5));
                    }







                }
            }
        });
    }

    void BindUid(){
        auth=FirebaseAuth.getInstance();
        textViewFechaLimite=findViewById(R.id.textViewFechaLimite);
        buttonSelecionarFechalIMITE=findViewById(R.id.buttonSelecionarFechalIMITE);
        retroFitConfiguration=new RetroFitConfiguration();
        alertDialogLoading=new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
        firebaseFirestore=FirebaseFirestore.getInstance();
        saveImagerToFirebase=new SaveImagerToFirebase();
        imageViewEvento=findViewById(R.id.imageViewEvento);
        textInputNombreDeElEvento=findViewById(R.id.textInputNombreDeElEvento);
        textInputHoraDeElEvento=findViewById(R.id.textInputHoraDeElEvento);
        textInputMinDeElevento=findViewById(R.id.textInputMinDeElevento);
        textInputhoraDeCareo=findViewById(R.id.textInputhoraDeCareo);
        textInputMinDeCareo=findViewById(R.id.textInputMinDeCareo);
        textInpurHoraPrimerCombate=findViewById(R.id.textInpurHoraPrimerCombate);
        textInputMinPrimerCombate=findViewById(R.id.textInputMinPrimerCombate);
        textInputPremios=findViewById(R.id.textInputPremios);
        textViewReglas=findViewById(R.id.textViewReglas);
        textViewFecha=findViewById(R.id.textViewFecha);
        spinnerDeElEvento=findViewById(R.id.spinnerDeElEvento);
        spinnerDeElCareo=findViewById(R.id.spinnerDeElCareo);
        spinnerPrimerCombate=findViewById(R.id.spinnerPrimerCombate);
        buttonSelecionarFecha=findViewById(R.id.buttonSelecionarFecha);
        buttonSelecionarCategoriaDeElEvento=findViewById(R.id.buttonSelecionarCategoriaDeElEvento);
        buttonReglas=findViewById(R.id.buttonReglas);
        buttonCrearEvento=findViewById(R.id.buttonCrearEvento);
        textInputLugarDeElEvento=findViewById(R.id.textInputLugarDeElEvento);
        textViewCategoria=findViewById(R.id.textViewCategoria);
        toolbar=findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CREAR EVENTO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    void createEvento(){
        String lugar=textInputLugarDeElEvento.getText().toString().trim();
        String nombreEvento=textInputNombreDeElEvento.getText().toString().trim();
        String horaEvento=textInputHoraDeElEvento.getText().toString().trim();
        String minEvento=textInputMinDeElevento.getText().toString().trim();
        String horaCareo=textInputhoraDeCareo.getText().toString().trim();
        String minCareo=textInputMinDeCareo.getText().toString().trim();
        String horaPrimerComabate=textInpurHoraPrimerCombate.getText().toString().trim();
        String minPrimerCombate=textInputMinPrimerCombate.getText().toString().trim();
        String premios=textInputPremios.getText().toString().trim();
        String reglas=textViewReglas.getText().toString().trim();
        String fecha=textViewFecha.getText().toString().trim();
        String spinerEvento=spinnerDeElEvento.getSelectedItem().toString();
        String spinerCareo=spinnerDeElEvento.getSelectedItem().toString();
        String spinerprimerCombate=spinnerPrimerCombate.getSelectedItem().toString();
        String categoria=textViewCategoria.getText().toString().trim();
        String horaDelEventoCompleta=horaEvento+":"+minEvento+":"+spinerEvento;
        String horaDelPrimerGombateCompleta=horaPrimerComabate+":"+minPrimerCombate+":"+spinerprimerCombate;
        String horaDelCareoCompleta=horaCareo+":"+minCareo+":"+spinerCareo;
        String fechaLimite=textViewFechaLimite.getText().toString().trim();
        int horaInterger=Integer.parseInt(horaCareo);
        int minCareoIntergeer=Integer.parseInt(minCareo);

        int horaeventoInterger=Integer.parseInt(horaEvento);
        int minEventoCreo=Integer.parseInt(minEvento);

        int horaPrimerCombateInteger=Integer.parseInt(horaPrimerComabate);
        int minPrimerCombateInteger=Integer.parseInt(minPrimerCombate);

        if ( !fechaLimite.isEmpty() && !nombreEvento.isEmpty() && !horaCareo.isEmpty() && !minCareo.isEmpty() && !horaEvento.isEmpty() && !minEvento.isEmpty() &&
        !horaPrimerComabate.isEmpty() && !minPrimerCombate.isEmpty() && !lugar.isEmpty() && !reglas.equals("REGLAS") && !fecha.equals("00/00/0000") &&
        !categoria.equals("----------")){
            if (horaInterger>0 && horaInterger<=23 && minCareoIntergeer <60 && horaeventoInterger>0 && horaeventoInterger<=23 && minEventoCreo <60 && horaPrimerCombateInteger>0 && horaPrimerCombateInteger<=23 && minPrimerCombateInteger<60 ){
                alertDialogLoading.show();
                saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                        alertDialogLoading.dismiss();
                        if (task.isSuccessful()){
                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    Eventos eventos= new Eventos();
                                    DocumentReference documentReference=firebaseFirestore.collection("Eventos").document();
                                    eventos.setId(documentReference.getId());
                                    eventos.setUrlImage(url);
                                    eventos.setNombreEvento(nombreEvento);
                                    eventos.setCategoria(categoria);
                                    eventos.setHoraCareo(horaDelCareoCompleta);
                                    eventos.setHoraEvento(horaDelEventoCompleta);
                                    eventos.setHoraPrimerPelea(horaDelPrimerGombateCompleta);
                                    eventos.setFecha(fecha);
                                    eventos.setReglas(reglas);
                                    eventos.setFechaLimite(fechaLimite);
                                    eventos.setPremios(premios);
                                    eventos.setEmision("EN EMISION");
                                    eventos.setLugar(lugar);
                                    eventos.setTimestamp(new Date().getTime());
                                    firebaseFirestore.collection("Eventos").document(documentReference.getId()).set(eventos);
                                    SendNotification(nombreEvento.toUpperCase(),categoria,url,eventos.getId());
                                    finish();
                                }
                            });
                        }
                    }
                });

            }else{
                showToast("FORNATO DE HORA NO VALIDO");
            }


        }else{
            showToast("DATOS INOMPLETOS");
        }

    }

    void setSpinners(){
        ArrayList<String> list=new ArrayList(){{add("AM");add("PM");}};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinnerDeElCareo.setAdapter(arrayAdapter);
        spinnerDeElEvento.setAdapter(arrayAdapter);
        spinnerPrimerCombate.setAdapter(arrayAdapter);

    }

    void showToast(String msj){
        Toast  toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    void SetListener(){


        buttonSelecionarFechalIMITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSecondAlert();
            }
        });

        buttonCrearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getExtras()!=null && isImage){
                    updateWithImage(getIntent().getStringExtra("id"));
                }else if (getIntent().getExtras()!=null && !isImage){
                    updateNotImage(getIntent().getStringExtra("id"));
                }else{
                    createEvento();
                }

            }
        });

        buttonSelecionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        buttonSelecionarCategoriaDeElEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowButtonSheetDialod();
            }
        });

        buttonReglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondBottonShetDialog();
            }
        });

        imageViewEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(CreateEventosActivity.this,imageViewEvento);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                intent.setType("image/jpg/png");
                                startActivityForResult(intent,REQUEST_CODE_PORTADA);
                                return true;
                            case R.id.eliminar:
                                getImageReosucer();
                                return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }
    void SendNotification(String title,String message,String urlEvneto,String idEvento){
        getAllTokens().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                  if (value!=null){
                      List<DocumentSnapshot> list=value.getDocuments();
                      for (int i=0;i<list.size();i++){
                          if (!list.get(i).getId().equals(auth.getCurrentUser().getUid())){

                              String token=list.get(i).getString("token");
                              Map<String,String> map= new HashMap<>();
                              map.put("title","");
                              map.put("titles",title);
                              map.put("idEvento",idEvento);
                              map.put("message",message);
                              map.put("urlimagen",urlEvneto);
                              map.put("idChat","");
                              map.put("idUser","");
                              configNotification(token,map);



                          }

                      }
                  }

            }
        });
    }
    void configNotification(String token, Map<String,String> data){
        FrmBody frmBody= new FrmBody(token,"high","4500s",data);
        retroFitConfiguration=new RetroFitConfiguration();
        retroFitConfiguration.senNotification(frmBody).enqueue(new Callback<FrmResponse>() {
            @Override
            public void onResponse(Call<FrmResponse> call, Response<FrmResponse> response) {
                if (response.body()!=null){
                    if (response.body().getSuccess()==1){

                    }
                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }
    void SecondBottonShetDialog(){
        bottomSheetDialog=new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view=LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        ArrayList<String> list= new ArrayList(){{
            add("MMA");
            add("CombatGround");
            add("a convenir");
        }};
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {
                textViewReglas.setText(dato);
                bottomSheetDialog.dismiss();
            }
        }));

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
    void  getImageReosucer(){
        imageViewEvento.setImageResource(R.drawable.foto_portada);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)imageViewEvento.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        img=outputStream.toByteArray();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PORTADA:
                if (resultCode== Activity.RESULT_OK){
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        imageViewEvento.setImageBitmap(bitmap);
                        img=outputStream.toByteArray();
                        isImage=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void ShowButtonSheetDialod(){
         bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view=LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView  recyclerView=view.findViewById(R.id.recicleViewCategorias);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        ArrayList<String> list= new ArrayList(){{
            add("PESO PAJA");
            add("PESO MOSCA");
            add("PESO GALLO");
            add("PESO PLUMA");
            add("PESO WELTER");
            add("PESO MEDIO");
            add("PESO SEMIPESADO");
            add("PESO PESADO");
            add("TODOS LOS PESOS");
        }};
        adapterCategorias=new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {

                textViewCategoria.setText(dato);
                bottomSheetDialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterCategorias);


    }

    void  showSecondAlert(){
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
                String fecha=year+"/"+month+"/"+day;
                textViewFechaLimite.setText(fecha);
            }
        });
    }
    void  showAlert(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_calendar,null);
        alertDialog= new AlertDialog.Builder(this).setTitle("SELECCIOAR FECHA").setView(view)
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
                String fecha=year+"/"+month+"/"+day;
                textViewFecha.setText(fecha);
            }
        });
    }

    Query getAllTokens(){
        return firebaseFirestore.collection("Tokens").orderBy("timestamp", Query.Direction.DESCENDING);
    }
}