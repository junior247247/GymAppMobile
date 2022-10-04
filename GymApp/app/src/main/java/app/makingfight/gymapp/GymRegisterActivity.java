package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import Models.Gym;
import Models.RegisterGym;
import Models.Users;
import authprovider.AuthenticatorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class GymRegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText textInputEditTextCorreo,textInputEditTextPassord,textInputEditTextPassordConfirm,textInputEditTextNombre,textInputEditTextLugar
            ,textInputEditTextdesde,textInputEditTextHasta,textInputEditTextArtesMarciales,textInputEditTextFacebook,textInputEditTextInatagram
            ,textInputEditTextTwitter,textInputTiktok;
    Button buttonRegister;
    ImageView imageViewPortada;
    CircleImageView circleImageViewProfile;
    SaveImagerToFirebase saveImagerToFirebase;
    AuthenticatorProvider authenticatorProvider;
    FirebaseAuth auth;
    AlertDialog alertDialog;
    Uri uri;
    byte imgProfile[];
    byte imgPortada[];
    static  final int REQUEST_CODE_PROFILE=200;
    static  final int REQUEST_CODE_PORTADA=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_gym_register);
        setToolbar();
        BinUid();
        setListenerUid();
        showImagePortada();
        showImagePViewProfile();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }
    void setToolbar(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("REGISTRO");
    }
    void  showImagePViewProfile(){
        circleImageViewProfile.setImageResource(R.drawable.profile);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) circleImageViewProfile.getDrawable();
        Bitmap map=bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imgProfile=byteArrayOutputStream.toByteArray();



    }
    void showImagePortada(){
        imageViewPortada.setImageResource(R.drawable.making);
        BitmapDrawable bitmapDrawable1=(BitmapDrawable)imageViewPortada.getDrawable();
        Bitmap bitmapPortada=bitmapDrawable1.getBitmap();
        ByteArrayOutputStream outByte=new ByteArrayOutputStream();
        bitmapPortada.compress(Bitmap.CompressFormat.JPEG,100,outByte);
        imgPortada=outByte.toByteArray();
    }



    void BinUid(){
        textInputTiktok=findViewById(R.id.textInputTiktok);
        textInputEditTextCorreo=findViewById(R.id.textInputEmail);
        textInputEditTextPassord=findViewById(R.id.textInputPassword);
        textInputEditTextPassordConfirm=findViewById(R.id.textInputConfirmPassword);
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
        auth=FirebaseAuth.getInstance();
        saveImagerToFirebase= new SaveImagerToFirebase();
        authenticatorProvider=new AuthenticatorProvider();
        alertDialog= new SpotsDialog.Builder().setMessage("ESPERE UN MOMENTO").setContext(this).build();
    }
    void clearInput(){
        textInputEditTextPassord.setText("");
        textInputEditTextPassordConfirm.setText("");
        textInputEditTextCorreo.setText("");
        textInputEditTextNombre.setText("");
        textInputEditTextLugar.setText("");
        textInputEditTextdesde.setText("");
        textInputEditTextHasta.setText("");
        textInputEditTextFacebook.setText("");
        textInputEditTextTwitter.setText("");
        textInputEditTextInatagram.setText("");
        textInputEditTextArtesMarciales.setText("");
    }
    void goToHomeActivity(){
        Intent intent= new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    void createUser(){
        String pass=textInputEditTextPassord.getText().toString();
        String passCofirm=textInputEditTextPassordConfirm.getText().toString();
        String email=textInputEditTextCorreo.getText().toString();
        String name=textInputEditTextNombre.getText().toString();
        String lugar=textInputEditTextLugar.getText().toString();
        String desde=textInputEditTextdesde.getText().toString();
        String hasta=textInputEditTextHasta.getText().toString();
        String facebook=textInputEditTextFacebook.getText().toString();
        String twitter=textInputEditTextTwitter.getText().toString();
        String instagram=textInputEditTextInatagram.getText().toString();
        String artesMarciales=textInputEditTextArtesMarciales.getText().toString();
        String tiktok=textInputTiktok.getText().toString().trim();
        alertDialog.show();
        if (!email.isEmpty()){
            if (Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                if (!pass.isEmpty() && !passCofirm.isEmpty()){
                    if (pass.equals(passCofirm)){
                        if (pass.length()>=6){
                            saveImagerToFirebase.saveImage(imgProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageProfile=uri.toString().toString();
                                                saveImagerToFirebase.saveImage(imgPortada).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull  Task<AuthResult> task) {
                                                                            alertDialog.dismiss();
                                                                            if (task.isSuccessful()){
                                                                                String imagePortada=uri.toString();
                                                                                RegisterGym registerGym=new RegisterGym();
                                                                                registerGym.setId(auth.getCurrentUser().getUid());
                                                                                registerGym.setUrlImagePortada(imagePortada);
                                                                                registerGym.setUrlImageProfile(imageProfile);
                                                                                registerGym.setDesde(desde);
                                                                                registerGym.setEmail(email);
                                                                                registerGym.setNameGym(name.toLowerCase());
                                                                                registerGym.setLugar(lugar);
                                                                                registerGym.setHasta(hasta);
                                                                                registerGym.setFacebook(facebook);
                                                                                registerGym.setTwitter(twitter);
                                                                                registerGym.setInstagram(instagram);
                                                                                registerGym.setArtesmarciales(artesMarciales);

                                                                                Gym gym=new Gym();
                                                                                gym.setId(auth.getCurrentUser().getUid());
                                                                                gym.setUrlImagePortada(imagePortada);
                                                                                gym.setUrlImageProfile(imageProfile);
                                                                                gym.setDesde(desde);
                                                                                gym.setNameGym(name);
                                                                                gym.setLugar(lugar);
                                                                                gym.setHasta(hasta);
                                                                                gym.setFacebook(facebook);
                                                                                gym.setTwitter(twitter);
                                                                                gym.setInstagram(instagram);
                                                                                gym.setArtesmarciales(artesMarciales);
                                                                                gym.setTimestamp(new Date().getTime());
                                                                                gym.setTiktok(tiktok);
                                                                                Users users= new Users();
                                                                                users.setName(name);
                                                                                users.setId(auth.getCurrentUser().getUid());
                                                                                users.setUrlImg(imageProfile);
                                                                                FirebaseFirestore.getInstance().collection("Users").document(users.getId()).set(users);



                                                                                authenticatorProvider.createGym(gym);

                                                                                clearInput();
                                                                                goToHomeActivity();
                                                                            }else{
                                                                                Toast.makeText(GymRegisterActivity.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GymRegisterActivity.this, "La contraseña debe tener minimo 6 caracteres", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }else{
                        Toast.makeText(GymRegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }else{
                    Toast.makeText(GymRegisterActivity.this, "Debes ingresar y confirmar la contraseña", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }else{
                Toast.makeText(GymRegisterActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }else{
            Toast.makeText(GymRegisterActivity.this, "Ingrese el email", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }




    }

    @Override
    protected void onStart() {
        super.onStart();
      auth=FirebaseAuth.getInstance();
      if (auth.getCurrentUser()!=null){
         auth.signOut();
      }
      auth.signInAnonymously();

    }


    void  setListenerUid(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        imageViewPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu= new PopupMenu(GymRegisterActivity.this,imageViewPortada);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                intent.setType("image/jpg/png");
                                startActivityForResult(intent,REQUEST_CODE_PORTADA);
                                return true;
                            case R.id.eliminar:
                                showImagePortada();
                                return true;
                        }
                        return true;
                    }


                });
                popupMenu.show();
            }
        });

        circleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(GymRegisterActivity.this,circleImageViewProfile);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregar:
                                Intent intent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                intent.setType("image/jpg/png");
                                startActivityForResult(intent,REQUEST_CODE_PROFILE);
                                return true;
                            case R.id.eliminar:
                                showImagePViewProfile();
                                return true;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
                switch (requestCode){
                    case REQUEST_CODE_PROFILE:
                        if (resultCode== Activity.RESULT_OK){
                            try {
                                Bitmap bitmap= MediaStore.Images.Media.getBitmap(GymRegisterActivity.this.getContentResolver(),data.getData());
                                ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                                this.imgProfile=byteArrayOutputStream.toByteArray();
                                Picasso.with(GymRegisterActivity.this).load(data.getData()).fit().into(circleImageViewProfile);
                                uri=data.getData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case REQUEST_CODE_PORTADA:
                        if (resultCode==Activity.RESULT_OK){
                            try {
                                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                                ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                                this.imgPortada=byteArrayOutputStream.toByteArray();
                                Picasso.with(GymRegisterActivity.this).load(data.getData()).fit().into(imageViewPortada);
                                uri=data.getData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                }
        super.onActivityResult(requestCode, resultCode, data);
    }
}