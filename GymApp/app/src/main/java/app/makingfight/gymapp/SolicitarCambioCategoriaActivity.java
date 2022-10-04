package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.solicitudLuchador;
import adapters.AdapterCategorias;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RetroFitConfiguration;

public class SolicitarCambioCategoriaActivity extends AppCompatActivity{

    AdapterCategorias adapterCategorias;
    BottomSheetDialog bottomSheetDialog;
    Button buttonSolicitarCambio;
    RetroFitConfiguration retroFitConfiguration;
    Toolbar toolbar;
    EditText editextMessge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_solicitar_cambio_categoria);
            toolbar =findViewById(R.id.toolbar);
        editextMessge=findViewById(R.id.editextMessge);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("CAMBIO DE CATEGORIA");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        buttonSolicitarCambio=findViewById(R.id.buttonSolicitarCambio);
        buttonSolicitarCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editextMessge.getText().toString().isEmpty()){
                    sendNotificaion(editextMessge.getText().toString());
                    Toast.makeText(SolicitarCambioCategoriaActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SolicitarCambioCategoriaActivity.this, "Ingrese los cambios que decea solicitar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void creaNotification(String message,String title){


        solicitudLuchador solicitud= new solicitudLuchador();
        DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Solicitudes").document();
        solicitud.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        solicitud.setIdLuchador(FirebaseAuth.getInstance().getCurrentUser().getUid());
        solicitud.setMessage(message);
        solicitud.setTitle(title);
        solicitud.setTimestamp(new Date().getTime());
        solicitud.setView(false);
        FirebaseFirestore.getInstance().collection("Solicitudes").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(solicitud);

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




    void sendNotificaion(String cambio){

        FirebaseFirestore.getInstance().collection("Luchadores").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    String urlImage=documentSnapshot.getString("urlImage");

                    FirebaseFirestore.getInstance().collection("UserAdmin").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                            if (value!=null){
                                creaNotification("HA SOLICITADO UN CAMBIO DE ESTADISTICAS DE "+cambio.toUpperCase(),name.toUpperCase() +" SOLICITUD DE CAMBIO DE ESTADISTICAS" );
                                List<DocumentSnapshot> list=value.getDocuments();
                                for(int i=0;i<list.size();i++){
                                    FirebaseFirestore.getInstance().collection("Tokens").document(list.get(i).getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                String token=documentSnapshot.getString("token");
                                                Map<String,String> map= new HashMap<>();
                                                map.put("title",name+" HA SOLICITADO UN CAMBIO DE ESTADISTICAS");
                                                map.put("message","SOLICITUD DE CAMBIO DE ESTADISTICAS " + cambio.toUpperCase());
                                                map.put("urlimagen",urlImage);
                                                map.put("idChat","");
                                                map.put("idUser","");




                                                map.put("titles","");
                                                map.put("idEvento","");
                                                map.put("messages","");
                                                map.put("name","");
                                                map.put("idNotification","");

                                                configNotification(token,map);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
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
                   
                }
            }

            @Override
            public void onFailure(Call<FrmResponse> call, Throwable t) {

            }
        });
    }

}