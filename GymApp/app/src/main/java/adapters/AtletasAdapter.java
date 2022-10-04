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

import app.makingfight.gymapp.LuchadoresActivity;
import app.makingfight.gymapp.R;
import app.makingfight.gymapp.UpdateStadisticaActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.Luchador;
import imageprovider.SaveImagerToFirebase;

public class AtletasAdapter extends FirestoreRecyclerAdapter<Luchador,AtletasAdapter.ViewHolde> {
    Context context;
    Activity activity;
    String permiso="";
    SaveImagerToFirebase saveImagerToFirebase;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    public AtletasAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Luchador> options,Context context,Activity activity,String permiso) {
        super(options);
        this.context=context;
        this.activity=activity;
        this.permiso=permiso;
        saveImagerToFirebase= new SaveImagerToFirebase();
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolde holder, int position, @NonNull @NotNull Luchador model) {
        holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.laoyut_items_luchadores,parent,false));
    }

    public class ViewHolde extends RecyclerView.ViewHolder{
        TextView textViewName,textViewRango,textViewAlias;
        ImageView imageViewAtleta;
        public ViewHolde(@NonNull  View itemView) {
            super(itemView);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewName=itemView.findViewById(R.id.textViewNameAtleta);
            textViewRango=itemView.findViewById(R.id.textViewRango);
            imageViewAtleta=itemView.findViewById(R.id.imageViewLuchador);
        }

        public void Bind(Luchador model){
            String dato="";
            String ganadas=model.getGanadas();
            String perdidas=model.getPerdidas();
            String empates=model.getEmpates();

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
            textViewName.setText(model.getName().toUpperCase());
            if (model.getAlias()!=null){
                textViewAlias.setText(model.getAlias().toUpperCase().trim());
            }

            Picasso.with(context).load(model.getUrlImage()).fit().placeholder(R.drawable.spiner).into(imageViewAtleta);


            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    if (permiso.equals("admin")){

                        PopupMenu popupMenu= new PopupMenu(context,itemView);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_peleadores,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.verAtleta:
                                        Intent intent= new Intent(context, LuchadoresActivity.class);
                                        Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                        intent.putExtra("id",model.getId());
                                        context.startActivity(intent,bundle);
                                        break;
                                    case R.id.eliminarAtleta:
                                        new AlertDialog.Builder(context).setTitle("¿REALMENTE DECEAS ELIMINAR ESTE LUCHADOR?").setMessage("").setNegativeButton("NO",null).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                delete(model.getId(),model.getUrlImage());
                                            }
                                        }).show();
                                        break;
                                    case R.id.updateStadisticas:
                                            Intent update= new Intent(context, UpdateStadisticaActivity.class);
                                            update.putExtra("id",model.getId());
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
                        Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                        intent.putExtra("id",model.getId());
                        context.startActivity(intent,bundle);
                    }

                }
            });


        }
        void delete(String id,String url){
            firestore.collection("Luchadores").document(id).delete();
            saveImagerToFirebase.delete(url);
            firestore.collection("GymAndLuchadores").whereEqualTo("idLuchador",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size()>0){

                            String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                            firestore.collection("GymAndLuchadores").document(id).delete();
                        }
                }
            });

        }
    }


}
