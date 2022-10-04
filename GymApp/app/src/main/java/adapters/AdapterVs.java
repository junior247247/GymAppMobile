package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.VS;

public class AdapterVs extends FirestoreRecyclerAdapter<VS,AdapterVs.ViewHolder> {
    Context context;
    Activity activity;


    public AdapterVs(@NonNull @NotNull FirestoreRecyclerOptions<VS> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull VS model) {
        holder.BindOne(model.getIdUser1());
        holder.BindTwo(model.getIdUser2());
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_versus,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCategoria,textViewNombrevs1,textViewAliarVs1,textViewGanadaPerdidasVs1;
        TextView textViewNombrevs2,textViewAliasVs2,textViewGanadasVs2;
        ImageView imageViewVs1,imavieLuchadorvs2;
        LinearLayout linearlayout1,linearlayout2;
        LinearLayout libearkyout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewCategoria=itemView.findViewById(R.id.textViewCategoria);
            textViewNombrevs1=itemView.findViewById(R.id.textViewNombrevs1);
            textViewAliarVs1=itemView.findViewById(R.id.textViewAliarVs1);
            textViewGanadaPerdidasVs1=itemView.findViewById(R.id.textViewGanadaPerdidasVs1);
            libearkyout=itemView.findViewById(R.id.libearkyout);
            textViewNombrevs2=itemView.findViewById(R.id.textViewNombrevs2);
            textViewAliasVs2=itemView.findViewById(R.id.textViewAliasVs2);
            textViewGanadasVs2=itemView.findViewById(R.id.textViewGanadasVs2);
            linearlayout2=itemView.findViewById(R.id.linearlayout2);
            linearlayout1=itemView.findViewById(R.id.linearlayout1);
            imavieLuchadorvs2=itemView.findViewById(R.id.imavieLuchadorvs2);
            imageViewVs1=itemView.findViewById(R.id.imageViewVs1);

        }

        void BindOne(String id){
            FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        String dato="";
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");

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
                        Picasso.with(context).load(documentSnapshot.getString("urlImage")).fit().placeholder(R.drawable.spiner).into(imageViewVs1);
                        textViewGanadaPerdidasVs1.setText(dato);
                        String name=documentSnapshot.getString("name").toUpperCase();
                        if (name.length()>14){
                            textViewNombrevs1.setText(name.toUpperCase().substring(0,12)+"...");
                        }else{
                            textViewNombrevs1.setText(name.toUpperCase());
                        }
                        textViewAliarVs1.setText(documentSnapshot.getString("alias").toUpperCase());
                    }
                }
            });
            linearlayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, LuchadoresActivity.class);
                    intent.putExtra("id",id);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });

        }



        void BindTwo(String id){
            FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        String dato="";
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");
                        textViewCategoria.setText(documentSnapshot.getString("categoria").toUpperCase());
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
                        Picasso.with(context).load(documentSnapshot.getString("urlImage")).fit().placeholder(R.drawable.spiner).into(imavieLuchadorvs2);

                        textViewGanadasVs2.setText(dato);
                        String name=documentSnapshot.getString("name").toUpperCase();
                        if (name.length()>14){
                            textViewNombrevs2.setText(name.toUpperCase().substring(0,12)+"...");
                        }else{
                            textViewNombrevs2.setText(name.toUpperCase());
                        }
                        textViewNombrevs2.setText(documentSnapshot.getString("name").toUpperCase());
                        textViewAliasVs2.setText(documentSnapshot.getString("alias").toUpperCase());
                    }
                }
            });
            linearlayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context,LuchadoresActivity.class);
                    intent.putExtra("id",id);
                    context.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });
        }
    }
}
