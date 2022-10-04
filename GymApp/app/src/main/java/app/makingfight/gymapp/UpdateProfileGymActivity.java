package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Explode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import authprovider.AuthenticatorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class UpdateProfileGymActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText textInputEditTextNombre,textInputEditTextLugar
            ,textInputEditTextdesde,textInputEditTextHasta,textInputEditTextArtesMarciales,textInputEditTextFacebook,textInputEditTextInatagram
            ,textInputEditTextTwitter,textInputTiktok;
    Button buttonRegister;
    ImageView imageViewPortada;
    CircleImageView circleImageViewProfile;
    SaveImagerToFirebase saveImagerToFirebase;
    AuthenticatorProvider authenticatorProvider;
    FirebaseAuth auth;
    AlertDialog alertDialog;
    String urlProfile="",urlPortada="";
    FirebaseFirestore firestore;
    byte imgProfile[];
    byte imgPortada[];
    boolean isChanePortada=false,isChangeProfile=false;
    static  final int REQUEST_CODE_PROFILE=200;
    static  final int REQUEST_CODE_PORTADA=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_update_profile_gym);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        firestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EDITAR PERFIL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BinUid();
        Listener();
        getData(auth.getCurrentUser().getUid());
        //image/jpg

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

    void updateUserChange(String id,String name,String url){
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        map.put("urlImg",url);
        firestore.collection("Users").document(id).update(map);
    }
    void updateOnlyname(String id,String name){
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        firestore.collection("Users").document(id).update(map);
    }

    void getData(String id){
        firestore.collection("Gyms").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String artesmarciales=documentSnapshot.getString("artesmarciales");
                    String desde=documentSnapshot.getString("desde");
                    String hasta=documentSnapshot.getString("hasta");
                    String facebook=documentSnapshot.getString("facebook");
                    String twitter=documentSnapshot.getString("twitter");
                    String tiktok=documentSnapshot.getString("tiktok");
                    String lugar=documentSnapshot.getString("lugar");
                    String instagram=documentSnapshot.getString("instagram");
                    String name=documentSnapshot.getString("nameGym");
                     urlPortada=documentSnapshot.getString("urlImagePortada");
                     urlProfile=documentSnapshot.getString("urlImageProfile");
                    textInputEditTextNombre.setText(name);
                    textInputEditTextArtesMarciales.setText(artesmarciales);
                    textInputEditTextdesde.setText(desde);
                    textInputEditTextHasta.setText(hasta);
                    textInputEditTextFacebook.setText(facebook);
                    textInputEditTextLugar.setText(lugar);
                    textInputEditTextTwitter.setText(twitter);
                    textInputTiktok.setText(tiktok);
                    textInputEditTextInatagram.setText(instagram);
                    Picasso.with(UpdateProfileGymActivity.this).load(urlPortada).placeholder(R.drawable.spiner).fit().into(imageViewPortada);
                    Picasso.with(UpdateProfileGymActivity.this).load(urlProfile).placeholder(R.drawable.spiner).fit().into(circleImageViewProfile);

                }
            }
        });
    }

    void changeOnlyData(){
        String name=textInputEditTextNombre.getText().toString();
        String lugar=textInputEditTextLugar.getText().toString();
        String desde=textInputEditTextdesde.getText().toString();
        String hasta=textInputEditTextHasta.getText().toString();
        String facebook=textInputEditTextFacebook.getText().toString();
        String twitter=textInputEditTextTwitter.getText().toString();
        String instagram=textInputEditTextInatagram.getText().toString();
        String artesMarciales=textInputEditTextArtesMarciales.getText().toString();
        String tiktok=textInputTiktok.getText().toString().trim();
        if (!name.isEmpty() && !lugar.isEmpty() && !desde.isEmpty() && !lugar.isEmpty()){
            Map<String,Object> map= new HashMap<>();
            map.put("artesmarciales",artesMarciales);
            map.put("desde",desde);
            map.put("hasta",hasta);
            map.put("lugar",lugar);
            map.put("instagram",instagram);
            map.put("tiktok",tiktok);
            map.put("twitter",twitter);
            map.put("facebook",facebook);
            map.put("nameGym",name.toLowerCase());
            updateOnlyname(auth.getCurrentUser().getUid(),name);
            firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        showToast("datos actualizados");
                        finish();
                    }else{
                        showToast("los datos no se pudieron actualizar");
                    }
                }
            });

        }else{
            showToast("completa los campos requeridos");
        }

    }

    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }



    void changeTwoImage(){

        String name=textInputEditTextNombre.getText().toString();
        String lugar=textInputEditTextLugar.getText().toString();
        String desde=textInputEditTextdesde.getText().toString();
        String hasta=textInputEditTextHasta.getText().toString();

        String facebook=textInputEditTextFacebook.getText().toString();
        String twitter=textInputEditTextTwitter.getText().toString();
        String instagram=textInputEditTextInatagram.getText().toString();
        String artesMarciales=textInputEditTextArtesMarciales.getText().toString();
        String tiktok=textInputTiktok.getText().toString().trim();
        if (!name.isEmpty() && !lugar.isEmpty() && !desde.isEmpty() && !hasta.isEmpty()){
            saveImagerToFirebase.delete(urlPortada);
            saveImagerToFirebase.delete(urlProfile);
            alertDialog.show();
            saveImagerToFirebase.saveImage(imgPortada).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urlportada=uri.toString();
                                saveImagerToFirebase.saveImage(imgProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                        alertDialog.dismiss();
                                        if (task.isSuccessful()){
                                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String urlImageProfile=uri.toString();
                                                    Map<String,Object> map= new HashMap<>();
                                                    map.put("artesmarciales",artesMarciales);
                                                    map.put("desde",desde);
                                                    map.put("hasta",hasta);
                                                    map.put("lugar",lugar);
                                                    map.put("instagram",instagram);
                                                    map.put("tiktok",tiktok);
                                                    map.put("twitter",twitter);
                                                    map.put("facebook",facebook);
                                                    map.put("nameGym",name.toLowerCase());
                                                    map.put("urlImagePortada",urlportada);
                                                    map.put("urlImageProfile",urlImageProfile);
                                                    updateUserChange(auth.getCurrentUser().getUid(),name,urlImageProfile);
                                                    firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                showToast("Datos actualizados");
                                                                finish();
                                                            }else{
                                                                showToast("no se pudieron actulizar los datos");
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }else{
           showToast("Debes Completar los campos requerido");
        }

    }


    void chaneImageonlinePortada(){

        String name=textInputEditTextNombre.getText().toString();
        String lugar=textInputEditTextLugar.getText().toString();
        String desde=textInputEditTextdesde.getText().toString();
        String hasta=textInputEditTextHasta.getText().toString();

        String facebook=textInputEditTextFacebook.getText().toString();
        String twitter=textInputEditTextTwitter.getText().toString();
        String instagram=textInputEditTextInatagram.getText().toString();
        String artesMarciales=textInputEditTextArtesMarciales.getText().toString();
        String tiktok=textInputTiktok.getText().toString().trim();
        if (!name.isEmpty() && !lugar.isEmpty() && !desde.isEmpty() && !hasta.isEmpty()){
            alertDialog.show();
            saveImagerToFirebase.delete(urlPortada);
            saveImagerToFirebase.saveImage(imgPortada).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    alertDialog.dismiss();
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String  url=uri.toString();
                                Map<String,Object> map= new HashMap<>();
                                map.put("artesmarciales",artesMarciales);
                                map.put("desde",desde);
                                map.put("hasta",hasta);
                                map.put("lugar",lugar);
                                map.put("instagram",instagram);
                                map.put("tiktok",tiktok);
                                map.put("twitter",twitter);
                                map.put("facebook",facebook);
                                map.put("nameGym",name.toLowerCase());
                                map.put("urlImagePortada",url);
                                firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            showToast("datos actualizados");
                                            finish();
                                        }else{
                                            showToast("no se pudieron actulizar los datos");
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }else{
            showToast("completa los campos requeridos");
        }

    }
    void changeInfoOnliProfileImage(){
        String name=textInputEditTextNombre.getText().toString().trim();
        String lugar=textInputEditTextLugar.getText().toString().trim();
        String desde=textInputEditTextdesde.getText().toString().trim();
        String hasta=textInputEditTextHasta.getText().toString().trim();

        String facebook=textInputEditTextFacebook.getText().toString();
        String twitter=textInputEditTextTwitter.getText().toString();
        String instagram=textInputEditTextInatagram.getText().toString();
        String artesMarciales=textInputEditTextArtesMarciales.getText().toString();
        String tiktok=textInputTiktok.getText().toString().trim();

        if (!name.isEmpty() && !lugar.isEmpty() && !desde.isEmpty() && !hasta.isEmpty()){
            saveImagerToFirebase.delete(urlProfile);
            alertDialog.show();
            saveImagerToFirebase.saveImage(imgProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    alertDialog.dismiss();
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profile=uri.toString();
                                Map<String,Object> map= new HashMap<>();
                                map.put("artesmarciales",artesMarciales);
                                map.put("desde",desde);
                                map.put("hasta",hasta);
                                map.put("lugar",lugar.toUpperCase());
                                map.put("instagram",instagram);
                                map.put("tiktok",tiktok);
                                map.put("twitter",twitter);
                                map.put("facebook",facebook);
                                map.put("nameGym",name.toLowerCase());
                                map.put("urlImageProfile",profile);
                                updateUserChange(auth.getCurrentUser().getUid(),name,profile);
                                firestore.collection("Gyms").document(auth.getCurrentUser().getUid()).update(map);
                                showToast("datos actualizados");
                                finish();
                            }
                        });
                    }
                }
            });

        }else {
            showToast("completa los campos requeridos");
        }


    }


    void Listener(){


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChanePortada && isChangeProfile){
                    changeTwoImage();
                }else if (isChangeProfile){
                    changeInfoOnliProfileImage();
                }else if (isChanePortada){
                    chaneImageonlinePortada();
                }else{
                    changeOnlyData();
                }
            }
        });

        circleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(UpdateProfileGymActivity.this,circleImageViewProfile);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                intent.setType("image/jpg/png");
                                startActivityForResult(intent,REQUEST_CODE_PROFILE);
                                break;
                            case R.id.eliminar:
                                circleImageViewProfile.setImageResource(R.drawable.person);
                                isChangeProfile=false;
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }

        });

        imageViewPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(UpdateProfileGymActivity.this,imageViewPortada);
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
                                imageViewPortada.setImageResource(R.drawable.making);
                                isChanePortada=false;
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
      switch (requestCode){
          case REQUEST_CODE_PORTADA:
              if (resultCode==Activity.RESULT_OK){
                  try {
                      Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                      ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                      bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                      Picasso.with(UpdateProfileGymActivity.this).load(data.getData()).fit().into(imageViewPortada);
                      imgPortada=outputStream.toByteArray();
                      isChanePortada=true;
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              break;
          case REQUEST_CODE_PROFILE:

              if (resultCode==Activity.RESULT_OK){
                  try {
                      Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    imgProfile=byteArrayOutputStream.toByteArray();
                    isChangeProfile=true;
                    Picasso.with(UpdateProfileGymActivity.this).load(data.getData()).fit().into(circleImageViewProfile);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }

              }

        break;

      }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void BinUid(){
        textInputTiktok=findViewById(R.id.textInputTiktok);


        textInputEditTextNombre=findViewById(R.id.textInputNombre);
        textInputEditTextLugar=findViewById(R.id.textInputLugar);
        textInputEditTextdesde=findViewById(R.id.textInputDesde);
        textInputEditTextHasta=findViewById(R.id.textInputHasta);
        textInputEditTextFacebook=findViewById(R.id.textInputfacebook);
        textInputEditTextInatagram=findViewById(R.id.textInputInstagram);
        textInputEditTextArtesMarciales=findViewById(R.id.textInputArtesMarciales);
        textInputEditTextTwitter=findViewById(R.id.textInputTwitter);
        circleImageViewProfile=findViewById(R.id.circleOImageViewGym);
        imageViewPortada=findViewById(R.id.imageViewPortada);
        buttonRegister=findViewById(R.id.buttonRgisterUserGym);
        auth= FirebaseAuth.getInstance();
        saveImagerToFirebase= new SaveImagerToFirebase();
        authenticatorProvider=new AuthenticatorProvider();
        alertDialog= new SpotsDialog.Builder().setMessage("ESPERE UN MOMENTO").setContext(this).build();
    }
}