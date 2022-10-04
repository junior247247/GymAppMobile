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
import android.media.MediaPlayer;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Models.Video;
import dmax.dialog.SpotsDialog;
import imageprovider.SaveImagerToFirebase;

public class SubirVideoActivity extends AppCompatActivity {
    Button buttonCargarVideo,buttonCargarMiniatura,ButtonsubirVideo;
    TextView textViewDuration;
    VideoView videoView;
    TextInputEditText textInputTitulo,textInputDescription;
    Uri uriVideo=null;
    byte[] miniatura=null;
    String urlImagen="";
    SaveImagerToFirebase saveImagerToFirebase;
    SimpleDateFormat format;
    AlertDialog alertDialog;
    ImageView imageViewMiniatura;
    Toolbar toolbar;
    int position=0;
    final  int REQUEST_CODE_IMAGE=20;
    FirebaseFirestore firebaseFirestore;
    MediaController mediaController=null;
    boolean withMiniatura=false;
    private static final int REQUEST_CODE_VIDEO=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set an exit transition
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_subir_video);
        BindUid();
        setListener();
      /*  Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("video/*, image/*"); startActivityForResult(mediaChooser, RESULT_LOAD_IMAGE);

       */
        getMiniatura();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }
    void getMiniatura(){
        imageViewMiniatura=findViewById(R.id.imageViewMiniatura);
        imageViewMiniatura.setImageResource(R.drawable.miniatura);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)imageViewMiniatura.getDrawable();
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        miniatura=byteArrayOutputStream.toByteArray();
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

    void subirVideo() {
        String title=textInputTitulo.getText().toString().trim();
        String description=textInputDescription.getText().toString().trim();
        if (uriVideo!=null && !title.isEmpty() && !description.isEmpty()){
            alertDialog.show();
            saveImagerToFirebase.saveImage(miniatura).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlImagen=uri.toString();
                                saveImagerToFirebase.saveVideo(uriVideo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                        alertDialog.dismiss();
                                        if (task.isSuccessful()){
                                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String urlVideo=uri.toString();
                                                    DocumentReference documentReference=firebaseFirestore.collection("Videos").document();
                                                    Video video=new Video();
                                                    video.setId(documentReference.getId());
                                                    video.setMiniatura(urlImagen);
                                                    video.setTimestamp(new Date().getTime());
                                                    video.setVideoTittle(title);
                                                    video.setVideoDescription(description);
                                                    video.setVideoURL(urlVideo);
                                                    firebaseFirestore.collection("Videos").document(documentReference.getId()).set(video);
                                                    showToast("Video Subido");
                                                    finish();

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
            showToast("Completa Los Campos");
        }

    }

    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    void saveImage(byte[] img){
        String title=textInputTitulo.getText().toString().trim();
        String description=textInputDescription.getText().toString().trim();
        if (uriVideo!=null && !title.isEmpty() && !description.isEmpty()){
            alertDialog.show();
        saveImagerToFirebase.saveImage(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlImagen=uri.toString();
                                saveImagerToFirebase.saveVideo(uriVideo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                        alertDialog.dismiss();
                                        if (task.isSuccessful()){
                                            saveImagerToFirebase.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String urlVideo=uri.toString();
                                                    DocumentReference documentReference=firebaseFirestore.collection("Videos").document();
                                                    Video video=new Video();
                                                    video.setId(documentReference.getId());
                                                    video.setMiniatura(urlImagen);
                                                    video.setTimestamp(new Date().getTime());
                                                    video.setVideoTittle(title);
                                                    video.setVideoDescription(description);
                                                    video.setVideoURL(urlVideo);
                                                    firebaseFirestore.collection("Videos").document(documentReference.getId()).set(video);
                                                    showToast("Video Subido");
                                                    withMiniatura=false;
                                                    finish();

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
        }
    }

    void reproducirVideo(Uri uri){
        videoView.setVideoURI(uri);
        videoView.setMediaController(new MediaController(this));
        format = new SimpleDateFormat("m:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        MediaPlayer mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(uri.toString());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String timeRecording=format.format(mediaPlayer.getDuration());
        textViewDuration.setText(timeRecording);
        videoView.seekTo(5000);
        if (mediaController==null){
            mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
        }

        videoView.setVisibility(View.VISIBLE);
        videoView.setFocusable(true);
        videoView.setFocusableInTouchMode(true);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                //videoView.seekTo(position);
              /*  if (position==0){
                    videoView.start();
                }

               */

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });
    }


    void BindUid(){
        buttonCargarVideo=findViewById(R.id.buttonCargarVideo);
        buttonCargarMiniatura=findViewById(R.id.buttonCargarMiniatura);
        ButtonsubirVideo=findViewById(R.id.ButtonsubirVideo);
        textViewDuration=findViewById(R.id.textViewDuration);
        videoView=findViewById(R.id.videoView);
        saveImagerToFirebase=new SaveImagerToFirebase();
        textInputTitulo=findViewById(R.id.textInputTitulo);
        alertDialog=new SpotsDialog.Builder().setContext(this).setMessage("SUBIENDO...").build();
        textInputDescription=findViewById(R.id.textInputDescription);
        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subir Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_VIDEO:
                if (resultCode== Activity.RESULT_OK){
                    uriVideo=data.getData();
                    reproducirVideo(data.getData());
                }
                break;
            case REQUEST_CODE_IMAGE:
                if (resultCode==Activity.RESULT_OK){
                    try {
                        Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        miniatura=outputStream.toByteArray();
                        withMiniatura=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void  setListener(){
            buttonCargarVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                    mediaChooser.setType("video/*"); startActivityForResult(mediaChooser, REQUEST_CODE_VIDEO);

                }
            });

        ButtonsubirVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (withMiniatura){
                       saveImage(miniatura);
                   }else{
                       subirVideo();
                   }
                }
            });

        buttonCargarMiniatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                mediaChooser.setType("image/*");
                startActivityForResult(mediaChooser, REQUEST_CODE_IMAGE);
            }
        });
    }


}