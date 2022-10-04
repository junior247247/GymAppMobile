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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Noticias;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class NoticiasActivity extends AppCompatActivity {
    TextInputEditText textInputTittleNoticia;
    EditText ediyexyNoticia;
    ImageView imageViewNoticias;
    Button buttonCargarImage,buttonPublicar;
    byte[]img;
    AlertDialog alertDialog;
    SaveImagerToFirebase saveImagerToFirebase;
    static  final  int REQUEST_CODE_PORTADA=10;
    Toolbar toolbar;
    boolean changeImage=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_noticias);
        Bind();
        getImageResource();
        buttonCargarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(NoticiasActivity.this,buttonCargarImage);
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
                                getImageResource();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        if (getIntent().getStringExtra("idEditar")!=null){
            getDate(getIntent().getStringExtra("idEditar"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        buttonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=textInputTittleNoticia.getText().toString().trim();
                String noticia=ediyexyNoticia.getText().toString().trim();
                if (getIntent().getStringExtra("idEditar")!=null && changeImage){
                    if (!title.isEmpty() && !noticia.isEmpty()){
                        Noticias noticias= new Noticias();
                        noticias.setTitelNoticia(title);
                        noticias.setNoticia(noticia);
                        alertDialog.show();
                        updateWithImage(getIntent().getStringExtra("idEditar"),noticias);
                    }
                }else if (getIntent().getStringExtra("idEditar")!=null){
                    if (!noticia.isEmpty() && !title.isEmpty()){
                        Noticias noticias= new Noticias();
                        noticias.setTitelNoticia(title);
                        noticias.setNoticia(noticia);
                        updateNotImage(getIntent().getStringExtra("idEditar"),noticias);
                    }
                }else{
                    createNoticias();
                }

            }
        });
    }
    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    void updateWithImage(String id,Noticias noticias){
        saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()){
                    saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            Map<String,Object>map= new HashMap<>();
                            map.put("imageUrl",url);
                            map.put("titelNoticia",noticias.getTitelNoticia());
                            map.put("noticia",noticias.getNoticia());
                            map.put("idChat","");
                            map.put("idUser","");
                            FirebaseFirestore.getInstance().collection("Noticias").document(id).update(map);
                            showToast("DATOS ACTUALIZADOS");
                            finish();
                        }
                    });
                }
            }
        });
    }
    void  createNoticias(){
        String title=textInputTittleNoticia.getText().toString().trim();
        String noticia=ediyexyNoticia.getText().toString().trim();

        if (!title.isEmpty() && !noticia.isEmpty()){
            alertDialog.show();
            saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                alertDialog.dismiss();
                                String url=uri.toString();
                                DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Noticias").document();
                                Noticias noticias= new Noticias();
                                noticias.setImageUrl(url);
                                noticias.setId(documentReference.getId());
                                noticias.setTimestamp(new Date().getTime());
                                noticias.setTitelNoticia(title);
                                noticias.setNoticia(noticia);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();
                                String fecha=formatter.format(date);
                                noticias.setFechaNoticia(fecha);
                                FirebaseFirestore.getInstance().collection("Noticias").document(noticias.getId()).set(noticias);
                                showToast("PUBLICADO");
                                finish();

                            }
                        });
                    }
                }
            });
        }else{
            showToast("COMPLETA TODOS LOS CAMPOS");
        }
    }
    void updateNotImage(String id,Noticias noticias){
        Map<String,Object> map= new HashMap<>();
        map.put("noticia",noticias.getNoticia());
        map.put("titelNoticia",noticias.getTitelNoticia());
        FirebaseFirestore.getInstance().collection("Noticias").document(id).update(map);
        showToast("DATOS ACTUALIZADOS");
        finish();

    }
    void getImageResource(){
        imageViewNoticias.setImageResource(R.drawable.no_foto);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)imageViewNoticias.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        img=outputStream.toByteArray();
    }
    void getDate(String id){
        FirebaseFirestore.getInstance().collection("Noticias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String noticia=documentSnapshot.getString("noticia");
                    String title=documentSnapshot.getString("titelNoticia");
                    String imageUrl=documentSnapshot.getString("imageUrl");
                    Picasso.with(NoticiasActivity.this).load(imageUrl).placeholder(R.drawable.spiner).fit().into(imageViewNoticias);
                    ediyexyNoticia.setText(noticia);
                    textInputTittleNoticia.setText(title);
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
                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        img=byteArrayOutputStream.toByteArray();
                        Picasso.with(NoticiasActivity.this).load(data.getData()).into(imageViewNoticias);
                        changeImage=true;
                      //  imageViewNoticias.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    void Bind(){
        alertDialog= new SpotsDialog.Builder().setMessage("ESPERE UN MOMENTO").setContext(this).build();
        saveImagerToFirebase= new SaveImagerToFirebase();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CREAR NOTICIA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonCargarImage=findViewById(R.id.buttonCargarImage);
        textInputTittleNoticia=findViewById(R.id.textInputTittleNoticia);
        ediyexyNoticia=findViewById(R.id.ediyexyNoticia);
        imageViewNoticias=findViewById(R.id.imageViewNoticias);
        buttonPublicar=findViewById(R.id.buttonPublicar);
    }
}