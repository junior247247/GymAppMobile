package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Luchador;
import Models.Paises;
import Models.addLuchadores;
import adapters.AdapterPaises;
import adapters.AdapterRegiones;
import authprovider.AuthenticatorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class AddLuchadorActivity extends AppCompatActivity implements AdapterRegiones.onCliCLisetener,AdapterPaises.onItemClick{
    Toolbar toolbar;
    TextInputEditText textInputEmailLuchador,textInputPassword,textInputPasswordConfirmLuchador,textInputUserNameLuchador,textInputEdad
            ,textImputAltura
            ,
            textInputPeso,textInputAlias,textInputAfiliacion
                    ;
    String url="https://firebasestorage.googleapis.com/v0/b/makingfightfinal.appspot.com/o/countries.json?alt=media&token=4ebaee3d-e4d3-4830-819c-9ad09c383292";
    TextView textViewPais,textViewRegion;
    LinearLayout linearLayoutPaisLuchador,linearLayoutRgionLuchador;
    final  int REQUEST_CODE_PORTADA=10;
     byte img[];
    CircleImageView circleOImageViewGymProfile;
    Button buttonRegisterLuchador;
    SaveImagerToFirebase saveImagerToFirebase;
    AlertDialog alertDialog;
    String imgUr="";
    RecyclerView recyclerViewCategorias;
    FirebaseFirestore firestore;
    String region="",pais="",code="";
    ArrayList<Paises>paisesArrayList= new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    String id="";
    AuthenticatorProvider authenticatorProvider;
    boolean isChangeImage=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_add_luchador);
        firestore=FirebaseFirestore.getInstance();
        Bind();
        setListener();
        getImageToImageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        LlenarArrayList();
        if (getIntent().getStringExtra("id")!=null){
            getData(getIntent().getStringExtra("id"));
            id=getIntent().getStringExtra("id");
        }

    }

    void LlenarArrayList(){
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1= new JSONObject(response);

                    JSONArray jsonArray=   jsonObject1.getJSONArray("countries");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  object   =jsonArray.getJSONObject(i);
                        String name=object.getString("name_en");
                        String  code=object.getString("code");
                        Paises paises= new Paises(name,code);
                        paisesArrayList.add(paises);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    void  showBottomShetDialog(){
        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view=LinearLayout.inflate(this,R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(new AdapterPaises(paisesArrayList,AddLuchadorActivity.this));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();



    }


    void showRegiones(){
        ArrayList<String>list= new ArrayList(){{
            add("GALICIA");
            add("CEUTA");
            add("MELILLA");
            add("ANDALUCIA");
            add("CASTILLA - LA MANCHA");
            add("MADRID");
            add("LA RIOJA");
            add("MURCIA");
            add("COM VALENCIANA");
            add("ARAGON");
            add("CATALUÑA");
            add("LA RIOJA");
            add("NAVARRA");
            add("P . VASCO");
            add("CANTABRIA");
            add("P . DE ASTURIAS");
            add("CASTILLA Y LEON");
            add("EXTREMADURA");
        }};

        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view=LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterRegiones(list,this::onItemClick));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    void rachas(String ganada,String pedida,String empates,String id){
        Map<String,Object> map= new HashMap<>();
        map.put("Ganadas",ganada);
        map.put("perdidas",pedida);
        map.put("empates",empates);
        map.put("idLuchador",id);
        FirebaseFirestore.getInstance().collection("ranchas").document(id).set(map);

    }

    void getImageToImageView(){
        circleOImageViewGymProfile.setImageResource(R.drawable.profile);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) circleOImageViewGymProfile.getDrawable();
        Bitmap map=bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        img=byteArrayOutputStream.toByteArray();
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

    void updateOnlyData(String id){
        String alias=textInputAlias.getText().toString();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();

        if(!alias.isEmpty() && !code.isEmpty() && !region.isEmpty() && !pais.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty()
                && !peso.isEmpty() ){
            Map<String,Object> map= new HashMap<>();
            map.put("afiliacion",afiliacion);
            map.put("alias",alias.trim());
            map.put("code",code);
            map.put("edad",edad);
            map.put("peso",peso);
            map.put("name",name);
            firestore.collection("Luchadores").document(id).update(map);
            ShowToas("datos actualizados");
            finish();
        }else{
            ShowToas("completa los campos requeridos");
        }
    }

    void updateWithImage(String id){
        String alias=textInputAlias.getText().toString();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();

        if(!alias.isEmpty() && !code.isEmpty() && !region.isEmpty() && !pais.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty()
                && !peso.isEmpty() ){
            alertDialog.show();
            saveImagerToFirebase.delete(imgUr);
            saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    alertDialog.dismiss();
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urlImage=uri.toString();
                                Map<String,Object> map= new HashMap<>();
                                map.put("afiliacion",afiliacion);
                                map.put("alias",alias.trim());
                                map.put("code",code);
                                map.put("edad",edad);
                                map.put("peso",peso);
                                map.put("name",name);
                                map.put("urlImage",urlImage);
                                firestore.collection("Luchadores").document(id).update(map);
                                ShowToas("datos actualizados");
                                finish();
                            }
                        });
                    }

                 }
            });



        }else{
            ShowToas("completa todos los campos");
        }
    }
    void getData(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String afiliacion=documentSnapshot.getString("afiliacion");
                        String altura=documentSnapshot.getString("altura");
                        String alias=documentSnapshot.getString("alias");
                        pais=documentSnapshot.getString("pais");
                        String name=documentSnapshot.getString("name");
                        String peso=documentSnapshot.getString("peso");
                        String region=documentSnapshot.getString("region");
                         imgUr=documentSnapshot.getString("urlImage");
                        code=documentSnapshot.getString("code");
                        String edad=documentSnapshot.getString("edad");
                          AddLuchadorActivity.this.region=region;
                        String debutEnelOctagono=documentSnapshot.getString("debutEnElOctagono");
                        textInputAfiliacion.setText(afiliacion);
                        textImputAltura.setText(altura);
                        textInputAlias.setText(alias);
                        textInputPeso.setText(peso);
                        textInputUserNameLuchador.setText(name);
                        textInputEdad.setText(edad);
                        textViewRegion.setText(region);
                        textViewPais.setText(pais);
                        Picasso.with(AddLuchadorActivity.this).load(imgUr).fit().placeholder(R.drawable.spiner).into(circleOImageViewGymProfile);




                    }
            }
        });
    }

    void setListener(){
        linearLayoutRgionLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegiones();
            }
        });


        linearLayoutPaisLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomShetDialog();
            }
        });



        circleOImageViewGymProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(AddLuchadorActivity.this,circleOImageViewGymProfile);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                intent.setType("image/jpg/png");
                                startActivityForResult(intent,REQUEST_CODE_PORTADA);
                                break;
                            case R.id.eliminar:
                                circleOImageViewGymProfile.setImageResource(R.drawable.profile);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        buttonRegisterLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!id.isEmpty() && isChangeImage){
                    updateWithImage(id);
                }else if (!id.isEmpty() && !isChangeImage){
                    updateOnlyData(id);
                }else{
                    createUser();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PORTADA:
                if (resultCode== Activity.RESULT_OK){
                    try {
                        Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        img=outputStream.toByteArray();
                        circleOImageViewGymProfile.setImageBitmap(bitmap);
                        isChangeImage=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    void createUser(){
        String alias=textInputAlias.getText().toString();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();

        if(!code.isEmpty() && !region.isEmpty() && !pais.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty()

                && !peso.isEmpty() ){
            alertDialog.show();
                saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=uri.toString();
                                DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Luchadores").document();
                                Luchador model= new Luchador();
                                rachas("0","0","0",documentReference.getId());
                                model.setId(documentReference.getId());
                                model.setUrlImage(url);
                                model.setIdGym(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                model.setGanadas("0");
                                model.setPerdidas("0");
                                model.setEmpates("0");
                                model.setAlias(alias.trim());
                                model.setName(name.toLowerCase());
                                model.setEdad(edad);
                                model.setAltura(altura);
                                model.setDivicion("Amateur");

                                model.setAfiliacion(afiliacion);

                                model.setRegion(region);
                                model.setCode(code);
                                model.setPais(pais);

                                model.setPeso(peso);
                                model.setCategoria("");
                                model.setTimestamp(new Date().getTime());

                                addLuchadores add= new addLuchadores();
                                add.setIdGym(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                add.setIdLuchador(documentReference.getId());
                                add.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                add.setTimestamp(new Date().getTime());
                                FirebaseFirestore.getInstance().collection("GymAndLuchadores").document(documentReference.getId()).set(add);
                                authenticatorProvider.createUserForLuchador(model);
                                alertDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });


        }else{
            ShowToas("Debes completar los campos");
        }




    }
    void ShowToas(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    void Bind(){
        textInputAfiliacion=findViewById(R.id.textInputAfiliacion);
        linearLayoutRgionLuchador=findViewById(R.id.linearLayoutRgionLuchador);
        linearLayoutPaisLuchador=findViewById(R.id.linearLayoutPaisLuchador);
        textViewRegion=findViewById(R.id.textViewRegion);
        textViewPais=findViewById(R.id.textViewPais);
        textInputAlias=findViewById(R.id.textInputAlias);
        alertDialog=new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
        saveImagerToFirebase= new SaveImagerToFirebase();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Luchador");
        authenticatorProvider=new AuthenticatorProvider();
        textInputEmailLuchador=findViewById(R.id.textInputEmailLuchador);
        textInputPassword=findViewById(R.id.textInputPassword);
        textInputPasswordConfirmLuchador=findViewById(R.id.textInputPasswordConfirmLuchador);
        textInputUserNameLuchador=findViewById(R.id.textInputUserNameLuchador);
        textInputEdad=findViewById(R.id.textInputEdad);
        textImputAltura=findViewById(R.id.textImputAltura);
        circleOImageViewGymProfile=findViewById(R.id.circleOImageViewGymProfile);
        buttonRegisterLuchador=findViewById(R.id.buttonRegisterLuchador);
        textInputPeso=findViewById(R.id.textInputPeso);



    }

    @Override
    public void onItemClick(String txt) {
        textViewRegion.setText(txt);
       this. region=txt;
       bottomSheetDialog.dismiss();
    }

    @Override
    public void itemClick(String pais, String code) {
        textViewPais.setText(pais);
        this.pais=pais;
        this.code=code;
        bottomSheetDialog.dismiss();
    }
}