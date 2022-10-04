package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.LuchadoresActivity;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import Models.EventoAndLuchadores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.FrmBody;
import service.FrmResponse;
import service.RelativeTime;
import service.RetroFitConfiguration;

public class NotificationAdapters extends FirestoreRecyclerAdapter<EventoAndLuchadores,NotificationAdapters.ViewHolder> {
    Context context;
    RetroFitConfiguration retroFitConfiguration;
    Activity activity;

    public NotificationAdapters(@NonNull @NotNull FirestoreRecyclerOptions<EventoAndLuchadores> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull EventoAndLuchadores model) {
        holder.Bind(model,position);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.linar_layout_items_solicitudes,parent,false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitulo,textViewNombreLuchador,textViewHace,textViewIndividual;
        Button buttonAprovar,buttonRechazar;
        ImageView imageViewFoto;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewTitulo=itemView.findViewById(R.id.textViewTitulo);
            textViewNombreLuchador=itemView.findViewById(R.id.textViewNombreLuchador);
            textViewHace=itemView.findViewById(R.id.textViewHace);
            buttonAprovar=itemView.findViewById(R.id.buttonAprovar);
            imageViewFoto=itemView.findViewById(R.id.imageViewFoto);
            buttonRechazar=itemView.findViewById(R.id.buttonRechazar);
            textViewIndividual=itemView.findViewById(R.id.textViewIndividual);
        }

        public void Bind(EventoAndLuchadores model, int position){
            textViewHace.setText(RelativeTime.getTimeAgo(model.getTimestamp(),context));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, LuchadoresActivity.class);
                    intent.putExtra("id",model.getIdLuchador());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });
            FirebaseFirestore.getInstance().collection("Eventos").document(model.getIdEvento()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        long timestamp=documentSnapshot.getLong("timestamp");
                        String time= RelativeTime.getTimeAgo(timestamp,context);
                        String title=documentSnapshot.getString("nombreEvento");
                        textViewTitulo.setText(title);
                        FirebaseFirestore.getInstance().collection("Luchadores").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    String name=documentSnapshot.getString("name");
                                    textViewNombreLuchador.setText(name);
                                    String img=documentSnapshot.getString("urlImage");
                                    Picasso.with(context).load(img).placeholder(R.drawable.spiner).fit().into(imageViewFoto);
                                }
                            }
                        });
                    }


                }
            });

            if (model.getIdGym()!=null){
                if (!model.getIdGym().isEmpty()){


                String id=model.getIdGym();
                FirebaseFirestore.getInstance().collection("Gyms").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String nombre=documentSnapshot.getString("nameGym");
                            textViewIndividual.setText("GIMNASIO:"+nombre.toUpperCase());
                        }
                    }
                });
                }
            }





            buttonAprovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context).setTitle("¿DECEAS ACEPTAR ESTA SOLICITUD?").setMessage("").setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("idEvento",model.getIdEvento()).whereEqualTo("idLuchador",model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots!=null){
                                        if (queryDocumentSnapshots.size()>0){
                                            String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                            Map<String,Object> map= new HashMap<>();
                                            map.put("isAcceted","SI");
                                            sendNotification(model,"FUE ACEPTADA TU SOLICITUD PARA EL EVENTO","MAKING FIGHT SOLICITUD DE EVENTO");
                                            FirebaseFirestore.getInstance().collection("EventoYluchadores").document(id).update(map);
                                            notifyDataSetChanged();
                                        }
                                    }
                                }
                            });

                        }
                    }).setNegativeButton("NO",null).show();
                }
            });

            buttonRechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context).setTitle("¿REALMENTE DECEAS RECHAZAR ESTA SOLICITUD?").setMessage("").setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("idEvento",model.getIdEvento()).whereEqualTo("idLuchador",model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots!=null){
                                            if (queryDocumentSnapshots.size()>0){
                                                String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                                Map<String,Object> map= new HashMap<>();
                                                map.put("isAcceted","SI");
                                                FirebaseFirestore.getInstance().collection("EventoYluchadores").document(id).delete();
                                                sendNotification(model,"TU SOLICITUD FUE RECHAZADA PARA UNISE AL EVENTO","MAKING FIGHT SOLICITUD DE EVENTO");
                                                notifyDataSetChanged();
                                            }
                                        }
                                }
                            });

                        }
                    }).setNegativeButton("NO",null).show();
                }
            });
        }

        void sendNotification(EventoAndLuchadores model,String msj,String title){
            FirebaseFirestore.getInstance().collection("EventoYluchadores").whereEqualTo("idEvento",model.getIdEvento()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        String idEvento=queryDocumentSnapshots.getDocuments().get(0).getString("idEvento");
                        FirebaseFirestore.getInstance().collection("Eventos").document(idEvento).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    String name=documentSnapshot.getString("nombreEvento");
                                    String urlImage=documentSnapshot.getString("urlImage");
                                    FirebaseFirestore.getInstance().collection("Tokens").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                String  token=documentSnapshot.getString("token");
                                                Map<String,String>map= new HashMap<>();
                                                map.put("title",title.toUpperCase());
                                                map.put("message",msj+" "+name);
                                                map.put("urlimagen",urlImage);


                                                map.put("idChat","");
                                                map.put("idUser","");
                                                map.put("titles","");
                                                map.put("idEvento","");
                                                map.put("messages","");
                                                map.put("name",name);
                                                map.put("idNotification","");

                                                configNotification(token,map);
                                            }
                                        }
                                    });

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
                        if (response.body().getSuccess()==1){

                        }else{

                        }
                    }
                }

                @Override
                public void onFailure(Call<FrmResponse> call, Throwable t) {

                }
            });
        }

    }
}
