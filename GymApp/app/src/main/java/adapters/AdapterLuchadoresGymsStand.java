package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.addLuchadores;
import app.makingfight.gymapp.AddLuchadorActivity;
import app.makingfight.gymapp.LuchadoresActivity;
import app.makingfight.gymapp.R;
import app.makingfight.gymapp.UpdateStadisticaActivity;
import imageprovider.SaveImagerToFirebase;

public class AdapterLuchadoresGymsStand extends FirestoreRecyclerAdapter<addLuchadores,AdapterLuchadoresGymsStand.ViewHolder> {
    Context context;
    Activity activity;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    private String isAdmin="";
    SaveImagerToFirebase saveImagerToFirebase;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    public AdapterLuchadoresGymsStand(@NonNull @NotNull FirestoreRecyclerOptions<addLuchadores> options, Activity  activity,Context context) {
        super(options);
        this.context=context;
        this.activity=activity;
        saveImagerToFirebase=new SaveImagerToFirebase();
        isAdmin(auth.getCurrentUser().getUid());
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull addLuchadores model) {
        holder.Bind(model.getIdLuchador());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser().getUid().equals(model.getIdGym())){
                    PopupMenu popupMenu= new PopupMenu(context,holder.itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu_gym,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.editarLuchador:
                                    Intent intent= new Intent(context, AddLuchadorActivity.class);
                                    intent.putExtra("id",model.getIdLuchador());
                                    context.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

                }else if (isAdmin.equals("si")){

                    PopupMenu popupMenu= new PopupMenu(context,holder.itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_peleadores,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.verAtleta:
                                    Intent intent= new Intent(context, LuchadoresActivity.class);
                                    Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                    intent.putExtra("id",model.getIdLuchador());
                                    context.startActivity(intent,bundle);
                                    break;
                                case R.id.eliminarAtleta:
                                    new AlertDialog.Builder(context).setTitle("¿REALMENTE DECEAS ELIMINAR ESTE LUCHADOR?").setMessage("").setNegativeButton("NO",null).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            holder.delete(model.getIdLuchador());
                                        }
                                    }).show();
                                    break;
                                case R.id.updateStadisticas:
                                    Intent update= new Intent(context, UpdateStadisticaActivity.class);
                                    update.putExtra("id",model.getIdLuchador());
                                    Bundle bundle1=ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                    context.startActivity(update,bundle1);
                                    break;


                            }
                            return true;
                        }
                    });

                    popupMenu.show();

                }else{
                    Intent intent= new Intent(context, LuchadoresActivity.class);
                    intent.putExtra("id",model.getIdLuchador());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }


            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laoyut_items_luchadores,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewRango,textViewAlias;
        ImageView imageViewAtleta;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewName=itemView.findViewById(R.id.textViewNameAtleta);
            textViewRango=itemView.findViewById(R.id.textViewRango);
            imageViewAtleta=itemView.findViewById(R.id.imageViewLuchador);
        }




        public void Bind(String id){

            firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");
                        String name=documentSnapshot.getString("name");
                        String alias=documentSnapshot.getString("alias");
                        String urlImage=documentSnapshot.getString("urlImage");
                        String id=documentSnapshot.getString("id");
                        String dato="";
                        if (ganadas!=null){
                            dato=ganadas;
                        }else{
                            dato="0";
                        }
                        if (perdidas!=null){
                            dato=dato+" - "+perdidas;
                        }else{
                            dato=dato+" - "+"0";
                        }
                        if (empates!=null){
                            dato=dato+" - "+empates;
                        }else{
                            dato=dato+" - "+"0";
                        }


                        textViewRango.setText(dato);
                        textViewName.setText(name.toUpperCase());
                        textViewAlias.setText(alias.toUpperCase().trim());


                        Picasso.with(context).load(urlImage).placeholder(R.drawable.spiner).fit().into(imageViewAtleta);

                    }
                }
            });



        }

        void delete(String id){
         firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
             @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
               if (documentSnapshot.exists()){
                   String url=documentSnapshot.getString("urlImage");
                   saveImagerToFirebase.delete(url);
                   firestore.collection("Luchadores").document(id).delete();
                   firestore.collection("GymAndLuchadores").whereEqualTo("idLuchador",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                       @Override
                       public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                           if (queryDocumentSnapshots.size()>0){

                               String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                               firestore.collection("GymAndLuchadores").document(id).delete();
                           }
                       }
                   });
                   firestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                firestore.collection("Users").document(id).delete();
                            }
                       }
                   });
               }
             }
         })   ;

        }
    }
    public void isAdmin(String id){
        firestore.collection("UserAdmin").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    isAdmin=documentSnapshot.getString("aprovacion");
                }else{
                    isAdmin="";
                }
            }
        });
    }

}
