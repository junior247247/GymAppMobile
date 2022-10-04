package app.makingfight.gymapp;

import androidx.annotation.NonNull;
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
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
import Models.Users;
import adapters.AdapterPaises;
import adapters.AdapterRegiones;
import authprovider.AuthenticatorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class RegisterLuchadorActivity extends AppCompatActivity  implements AdapterPaises.onItemClick, AdapterRegiones.onCliCLisetener {
    Toolbar toolbar;
    TextInputEditText textInputEmailLuchador,textInputPassword,textInputPasswordConfirmLuchador,textInputUserNameLuchador,textInputEdad
            ,textImputAltura
            ,
            textInputPeso,textInputAlias,textInputAfiliacion
            ;
    static final int REQUEST_CODE_PORTADA=50;
    FirebaseAuth auth;
    String pais="",code="",region="";
    BottomSheetDialog bottomSheetDialog;

    AlertDialog alertDialog;
    TextView textViewPais,textViewRegion;
    SaveImagerToFirebase saveImagerToFirebase;
    LinearLayout linearLayoutPaisLuchador,linearLayoutRegionLuchador;
    byte img[];
    ArrayList<Paises>paisesArrayList= new ArrayList<>();
    Button buttonRegisterLuchador;

    String url="https://firebasestorage.googleapis.com/v0/b/makingfightfinal.appspot.com/o/countries.json?alt=media&token=4ebaee3d-e4d3-4830-819c-9ad09c383292";
    CircleImageView circleOImageViewGymProfile;
    AuthenticatorProvider authenticatorProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_register_luchador);
        Bind();
        setListener();
        getImageToImageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        llenarArrayList();
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
            add("GALICIA");
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


    void rachas(String ganada,String pedida,String empates){
        Map<String,Object> map= new HashMap<>();
        map.put("Ganadas",ganada);
        map.put("perdidas",pedida);
        map.put("empates",empates);
        map.put("idLuchador",FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore.getInstance().collection("ranchas").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map);

    }
    void llenarArrayList(){

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
                    Toast.makeText(RegisterLuchadorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        recyclerView.setAdapter(new AdapterPaises(paisesArrayList,RegisterLuchadorActivity.this));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();


    }

    void getImageToImageView(){
        circleOImageViewGymProfile.setImageResource(R.drawable.profile);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) circleOImageViewGymProfile.getDrawable();
        Bitmap map=bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        img=byteArrayOutputStream.toByteArray();
    }

    void createUser(){
        String alias=textInputAlias.getText().toString().trim();
        String email=textInputEmailLuchador.getText().toString().trim();
        String pass=textInputPassword.getText().toString().trim();
        String passConfirm=textInputPasswordConfirmLuchador.getText().toString().trim();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();

        if(!region.isEmpty() && !code.isEmpty() && !pais.isEmpty() &&!email.isEmpty() && !pass.isEmpty() && !passConfirm.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty()
         && !peso.isEmpty() ){
        if (pass.length()>=6){
            if (pass.equals(passConfirm)){
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    alertDialog.show();
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()){
                                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String urlImge=uri.toString();
                                                    Luchador model= new Luchador();
                                                    model.setId(auth.getCurrentUser().getUid());
                                                    model.setUrlImage(urlImge);
                                                    model.setIdGym("");
                                                    model.setRegion(region);
                                                    model.setName(name.toLowerCase());
                                                    model.setEmpates("0");
                                                    model.setGanadas("0");
                                                    model.setPerdidas("0");
                                                    model.setCode(code);
                                                    model.setPais(pais);
                                                    model.setAlias(alias);
                                                    model.setEdad(edad);
                                                    model.setAltura(altura);
                                                    model.setAfiliacion(afiliacion);
                                                    rachas("0","0","0");

                                                    model.setDivicion("Amateur");

                                                    model.setPeso(peso);
                                                    model.setCategoria("");
                                                    model.setTimestamp(new Date().getTime());

                                                    Users users= new Users();
                                                    users.setName(name);
                                                    users.setId(auth.getCurrentUser().getUid());
                                                    users.setUrlImg(urlImge);
                                                    FirebaseFirestore.getInstance().collection("Users").document(users.getId()).set(users);

                                                    authenticatorProvider.createUserForLuchador(model);
                                                    alertDialog.dismiss();
                                                    Intent intent= new Intent(RegisterLuchadorActivity.this,HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                });

                            }else{
                                alertDialog.dismiss();
                                Toast.makeText(RegisterLuchadorActivity.this, "El usuario no se pudo crear", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(this, "Email no valido", Toast.LENGTH_SHORT).show();
                }



            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "La contraseña no puede tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
        }

        }else{
           Toast toast= Toast.makeText(this,"Debes completar los campos",Toast.LENGTH_LONG);
           toast.setGravity(Gravity.CENTER,0,0);
           toast.show();
        }




    }

    void Bind(){
        textInputAfiliacion=findViewById(R.id.textInputAfiliacion);
        textViewRegion=findViewById(R.id.textViewRegion);
        textViewPais=findViewById(R.id.textViewPais);
        linearLayoutRegionLuchador=findViewById(R.id.linearLayoutRegionLuchador);
        linearLayoutPaisLuchador=findViewById(R.id.linearLayoutPaisLuchador);
        alertDialog=new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
        auth=FirebaseAuth.getInstance();
        authenticatorProvider=new AuthenticatorProvider();
        saveImagerToFirebase= new SaveImagerToFirebase();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registro Luchador");
        textInputEmailLuchador=findViewById(R.id.textInputEmailLuchador);
        textInputAlias=findViewById(R.id.textInputAlias);
        textInputPassword=findViewById(R.id.textInputPassword);
        textInputPasswordConfirmLuchador=findViewById(R.id.textInputPasswordConfirmLuchador);
        textInputUserNameLuchador=findViewById(R.id.textInputUserNameLuchador);
        textInputEdad=findViewById(R.id.textInputEdad);
        textImputAltura=findViewById(R.id.textImputAltura);
        circleOImageViewGymProfile=findViewById(R.id.circleOImageViewGymProfile);
        buttonRegisterLuchador=findViewById(R.id.buttonRegisterLuchador);
        textInputPeso=findViewById(R.id.textInputPeso);

    }



    void setListener(){
        linearLayoutPaisLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomShetDialog();
            }
        });

        linearLayoutRegionLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegiones();
            }
        });



        circleOImageViewGymProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(RegisterLuchadorActivity.this,circleOImageViewGymProfile);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(intent,REQUEST_CODE_PORTADA);
                                return true;
                            case R.id.eliminar:
                                  getImageToImageView();
                                return true;
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
            createUser();
        }
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PORTADA:
                if (resultCode== Activity.RESULT_OK){
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(RegisterLuchadorActivity.this.getContentResolver(),data.getData());
                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        img=byteArrayOutputStream.toByteArray();
                        Picasso.with(RegisterLuchadorActivity.this).load(data.getData()).into(circleOImageViewGymProfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void itemClick(String pais, String code) {
        this.pais=pais;
        this.code=code;
        this.textViewPais.setText(pais);
        this.bottomSheetDialog.dismiss();
    }

    @Override
    public void onItemClick(String txt) {
        this.textViewRegion.setText(txt);
        this.region=txt;
        this.bottomSheetDialog.dismiss();
    }
}