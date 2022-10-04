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
import java.util.HashMap;
import java.util.Map;

import Models.Paises;
import adapters.AdapterPaises;
import adapters.AdapterRegiones;
import authprovider.AuthenticatorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class UpdateProfileActivity extends AppCompatActivity implements  AdapterPaises.onItemClick,AdapterRegiones.onCliCLisetener {
    TextInputEditText textInputEmailLuchador,textInputPassword,textInputPasswordConfirmLuchador,textInputUserNameLuchador,textInputEdad
            ,textImputAltura,textInputDebutEneloctagono
            ,
            textInputPeso,textInputAlias,textInputAfiliacion;

    String url="https://firebasestorage.googleapis.com/v0/b/gym-app-183b9.appspot.com/o/countries.json?alt=media&token=f65334e3-a1cc-4b80-a7cc-93ba2c3e6df9";
    TextView textViewPais,textViewRegion;
    LinearLayout linearLayoutPaisLuchador,linearLayoutRgionLuchador;
    final  int REQUEST_CODE_PORTADA=10;
    byte img[];
    boolean isWithImage=false;
    CircleImageView circleOImageViewGymProfile;
    Button buttonRegisterLuchador;
    SaveImagerToFirebase saveImagerToFirebase;
    AlertDialog alertDialog;
    RecyclerView recyclerViewCategorias;
    String region="",pais="",code="",urImage="";
    Toolbar toolbar;
    BottomSheetDialog bottomSheetDialog;
    AuthenticatorProvider authenticatorProvider;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<Paises> paisesArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_update_profile);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        paisesArrayList= new ArrayList<>();
        Bind();
        getImageToImageView();
        setListener();
        getDataOfUser(auth.getCurrentUser().getUid());
        getArrayList();
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

    void updateUserOnlyData(String id,String name){
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        firestore.collection("Users").document(id).update(map);
    }

    void updateUserWithImage(String id,String urlimg,String name){
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        map.put("urlImg",urlimg);
        firestore.collection("Users").document(id).update(map);
    }

    void getDataOfUser(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        urImage=documentSnapshot.getString("urlImage");
                        String afiliacion=documentSnapshot.getString("afiliacion");
                        String alias=documentSnapshot.getString("alias");
                        String altura=documentSnapshot.getString("altura");
                        String name=documentSnapshot.getString("name");
                        String debut=documentSnapshot.getString("debutEnelOctagono");
                        String edad=documentSnapshot.getString("edad");
                        String peso=documentSnapshot.getString("peso");
                        String codes=documentSnapshot.getString("code");
                        pais=documentSnapshot.getString("pais");
                         region=documentSnapshot.getString("region");
                            code=codes;
                         textViewPais.setText(pais);
                         textInputPeso.setText(peso);
                         textViewRegion.setText(region);
                         textInputAfiliacion.setText(afiliacion);
                         textInputAlias.setText(alias);
                         textImputAltura.setText(altura);
                         textInputUserNameLuchador.setText(name);
                         textInputDebutEneloctagono.setText(debut);
                         textInputEdad.setText(edad);
                        Picasso.with(UpdateProfileActivity.this).load(urImage).placeholder(R.drawable.spiner).fit().into(circleOImageViewGymProfile);
                    }
                }
            });
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
        getSupportActionBar().setTitle("EDITAR");
        authenticatorProvider=new AuthenticatorProvider();
        textInputEmailLuchador=findViewById(R.id.textInputEmailLuchador);
        textInputPassword=findViewById(R.id.textInputPassword);
        textInputPasswordConfirmLuchador=findViewById(R.id.textInputPasswordConfirmLuchador);
        textInputUserNameLuchador=findViewById(R.id.textInputUserNameLuchador);
        textInputEdad=findViewById(R.id.textInputEdad);
        textImputAltura=findViewById(R.id.textImputAltura);
        textInputDebutEneloctagono=findViewById(R.id.textInputDebutEneloctagono);
        circleOImageViewGymProfile=findViewById(R.id.circleOImageViewGymProfile);
        buttonRegisterLuchador=findViewById(R.id.buttonRegisterLuchador);
        textInputPeso=findViewById(R.id.textInputPeso);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PORTADA:
                if (resultCode== Activity.RESULT_OK){
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        img=outputStream.toByteArray();
                        circleOImageViewGymProfile.setImageBitmap(bitmap);
                        isWithImage=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void getImageToImageView(){
        circleOImageViewGymProfile.setImageResource(R.drawable.no_foto);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) circleOImageViewGymProfile.getDrawable();
        Bitmap map=bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        img=byteArrayOutputStream.toByteArray();
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
            add("CEUTA");
            add("MELILLA");
        }};

        bottomSheetDialog= new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterRegiones(list,this::onItemClick));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }




    void UpdateWithImage(){
        String alias=textInputAlias.getText().toString();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String debutEnElOctagono=textInputDebutEneloctagono.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();
        if (!region.isEmpty() && !code.isEmpty() && !alias.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty() && !debutEnElOctagono.isEmpty() && !peso.isEmpty() && !afiliacion.isEmpty()){
        alertDialog.show();
          saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                  //saveImagerToFirebase.delete(urImage);
                  alertDialog.dismiss();
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String URL=uri.toString();
                                Map<String,Object> map= new HashMap<>();
                                map.put("alias",alias);
                                map.put("name",name);
                                map.put("altura",altura);
                                map.put("edad",edad);
                                map.put("code",code);
                                map.put("pais",pais);
                                map.put("region",region);
                                map.put("urlImage",URL);
                                updateUserWithImage(auth.getUid(),URL,name);
                                firestore.collection("Luchadores").document(auth.getCurrentUser().getUid()).update(map);
                                finish();
                            }
                        });
                    }
              }
          });



        }else{
            ShowToas("COMPLETA TODOS LOS CAMPOS");
        }

    }


    void Update(){
        String alias=textInputAlias.getText().toString();
        String name=textInputUserNameLuchador.getText().toString().trim();
        String edad=textInputEdad.getText().toString().trim();
        String altura=textImputAltura.getText().toString().trim();
        String debutEnElOctagono=textInputDebutEneloctagono.getText().toString().trim();
        String peso=textInputPeso.getText().toString().trim();
        String afiliacion=textInputAfiliacion.getText().toString().trim();
        if (!region.isEmpty() && !code.isEmpty() && !alias.isEmpty() && !name.isEmpty() && !edad.isEmpty() && !altura.isEmpty() && !debutEnElOctagono.isEmpty() && !peso.isEmpty() && !afiliacion.isEmpty()){
            Map<String,Object> map= new HashMap<>();
            map.put("alias",alias);
            map.put("name",name);
            map.put("altura",altura);
            map.put("edad",edad);
            map.put("code",code);
            map.put("pais",pais);
            map.put("region",region);
            updateUserOnlyData(auth.getCurrentUser().getUid(),name);
            firestore.collection("Luchadores").document(auth.getCurrentUser().getUid()).update(map);

        }else{
            ShowToas("Completa Todos los Campos");
        }

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
                PopupMenu popupMenu= new PopupMenu(UpdateProfileActivity.this,circleOImageViewGymProfile);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(intent,REQUEST_CODE_PORTADA);
                                break;
                            case R.id.eliminar:
                                circleOImageViewGymProfile.setImageResource(R.drawable.no_foto);
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
                    if (isWithImage){
                        UpdateWithImage();
                    }else{
                        Update();
                    }
            }
        });


    }





    void ShowToas(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    void getArrayList(){

        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1= new JSONObject(response);
                    JSONArray jsonArray=   jsonObject1.getJSONArray("countries");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  object   =jsonArray.getJSONObject(i);
                        String name=object.getString("name_es");
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
        recyclerView.setAdapter(new AdapterPaises(paisesArrayList,UpdateProfileActivity.this));
        bottomSheetDialog.setContentView(view);
         bottomSheetDialog.show();

    }

    @Override
    public void itemClick(String pais, String code) {
        this.pais=pais;
        this.code=code;
        this.textViewPais.setText(pais);
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onItemClick(String txt) {
    this.region=txt;
    this.textViewRegion.setText(txt);
    bottomSheetDialog.dismiss();
    }
}