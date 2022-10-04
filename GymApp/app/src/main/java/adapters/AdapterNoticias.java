package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.DetailNoticiasActivity;
import app.makingfight.gymapp.NoticiasActivity;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.Noticias;
import imageprovider.SaveImagerToFirebase;

public class AdapterNoticias extends FirestoreRecyclerAdapter<Noticias, AdapterNoticias.ViewHolder> {
    Context context;
    Activity activity;
    String permiso="";
    SaveImagerToFirebase saveImagerToFirebase;
    public AdapterNoticias(@NonNull @NotNull FirestoreRecyclerOptions<Noticias> options,Context context,Activity activity,String permiso) {
        super(options);
        this.context=context;
        this.activity=activity;
        this.permiso=permiso;
        saveImagerToFirebase= new SaveImagerToFirebase();
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Noticias model) {
            holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_noticias,parent,false));
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewFecha;
        ImageView imageViewNoticias;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textViewTittleNoticias);
            textViewFecha=itemView.findViewById(R.id.textViewFecha);
            imageViewNoticias=itemView.findViewById(R.id.imageViewNoticias);
        }
        public void Bind(Noticias noticias){
            if (noticias.getTitelNoticia().length()>=15){
                textViewTitle.setText(noticias.getTitelNoticia().substring(0,15));
            }else{
                textViewTitle.setText(noticias.getTitelNoticia());
            }
            textViewFecha.setText(noticias.getFechaNoticia());
            Picasso.with(context).load(noticias.getImageUrl()).fit().placeholder(R.drawable.spiner).into(imageViewNoticias);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permiso.equals("admin")){
                        PopupMenu popupMenu= new PopupMenu(context,itemView);
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_noticias,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.mostrar:
                                        Intent intent= new Intent(context, DetailNoticiasActivity.class);
                                        intent.putExtra("id",noticias.getId());
                                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                                            break;
                                    case R.id.eliminar:
                                        new AlertDialog.Builder(context).setTitle("¿REALMENTE DECEAS ELIMINAR ESTE POST?").setMessage("").setNegativeButton("NO",null).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                eliminar(noticias.getId(),noticias.getImageUrl());
                                            }
                                        }).show();


                                        break;
                                    case R.id.editar:
                                        Intent intent2= new Intent(context, NoticiasActivity.class);
                                        intent2.putExtra("idEditar",noticias.getId());
                                        context.startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }else{
                        Intent intent= new Intent(context, DetailNoticiasActivity.class);
                        intent.putExtra("id",noticias.getId());
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                    }

                }
            });
        }

    void  eliminar(String id,String url){
        FirebaseFirestore.getInstance().collection("Noticias").document(id).delete();
        saveImagerToFirebase.delete(url);
    }
    }
}
