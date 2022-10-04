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

import app.makingfight.gymapp.CreateEventosActivity;
import app.makingfight.gymapp.FinalizarEventoActivity;
import app.makingfight.gymapp.R;
import app.makingfight.gymapp.ShowInfoPeleaActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Models.Eventos;

public class PeleasApdater extends FirestoreRecyclerAdapter<Eventos,PeleasApdater.ViewHolder> {
        Context context;
        String permiso="";
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        Activity activity;
        String meses[]={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        FirebaseAuth auth=FirebaseAuth.getInstance();

    public PeleasApdater(@NonNull @NotNull FirestoreRecyclerOptions<Eventos> options,Context context,String permiso,Activity activity) {
        super(options);
        this.context=context;
        this.permiso=permiso;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_peleas,parent,false));
    }
    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Eventos model) {

        if (model.getNombreEvento().length()>=30){
            holder.textViewTitle.setText(model.getNombreEvento().toUpperCase().substring(0,30)+" ...");
        }else{
            holder.textViewTitle.setText(model.getNombreEvento().toUpperCase());
        }

        holder.textViewLugarEvento.setText(model.getLugar());
        Picasso.with(context).load(model.getUrlImage()).fit().placeholder(R.drawable.spiner).into(holder.imageViewLuchador);
        String myDate1=model.getFecha()+" "+ model.getHoraEvento().substring(0,5)+":00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;

        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
        try {
           date=format.parse(myDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //String stringRender=  new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY").format(date).toString();
        String numFecha=model.getFecha().substring(5,6);
        int indice=Integer.parseInt(numFecha);
        String data=new SimpleDateFormat("EEEE dd").format(date);
        String allDate=data+" de " +meses[indice-1] +" de " + model.getFecha().substring(0,4);
        holder.textViewDate.setText(allDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permiso.equals("admin")){
                    PopupMenu popupMenu=new PopupMenu(context,holder.itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_admin_peleas,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.mostrar:
                                        Intent intent2= new Intent(context, ShowInfoPeleaActivity.class);
                                        Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                        intent2.putExtra("id",model.getId());
                                        context.startActivity(intent2,bundle);
                                        break;
                                    case R.id.editar:
                                        Intent intent= new Intent(context, CreateEventosActivity.class);
                                        Bundle bundle1=ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                        intent.putExtra("id",model.getId());
                                        context.startActivity(intent,bundle1);
                                        break;
                                    case R.id.eliminar:
                                        new AlertDialog.Builder(context).setTitle("Eliminar evento").setMessage("¿Realmende deceas eliminar este evento?")
                                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        delete(model.getId());
                                                    }
                                                }).setNegativeButton("NO",null).show();

                                        break;
                                    case R.id.finalizarEvento:
                                        Intent intent1=new Intent(context, FinalizarEventoActivity.class);
                                        intent1.putExtra("id",model.getId());
                                        intent1.putExtra("lugar",model.getLugar());
                                        intent1.putExtra("fecha",model.getFecha());
                                        Bundle bundle2=ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                                        context.startActivity(intent1,bundle2);
                                        break;

                                }
                            return true;
                        }
                    });
                    popupMenu.show();
                }else{
                    Intent intent= new Intent(context,ShowInfoPeleaActivity.class);
                    intent.putExtra("id",model.getId());
                    Bundle bundle=ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                    context.startActivity(intent,bundle);
                }
            }
        });



    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate,textViewTitle,textViewLugarEvento,textViewCategoria;
        ImageView imageViewLuchador;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageViewLuchador=itemView.findViewById(R.id.imageViewLuchador);
            textViewDate=itemView.findViewById(R.id.textviewDate);
            textViewTitle=itemView.findViewById(R.id.textViewTitleEvento);
            textViewCategoria=itemView.findViewById(R.id.textViewCategoria);
            textViewLugarEvento=itemView.findViewById(R.id.textViewLugarEvento);
        }
    }
    void delete(String id){
        firestore.collection("Eventos").document(id).delete();
    }
}
