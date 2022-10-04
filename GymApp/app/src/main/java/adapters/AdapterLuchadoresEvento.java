package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.EventoAndLuchadores;

public class AdapterLuchadoresEvento extends FirestoreRecyclerAdapter<EventoAndLuchadores,AdapterLuchadoresEvento.ViewHolder> {

    Context context;
    Activity activity;

    public AdapterLuchadoresEvento(@NonNull @NotNull FirestoreRecyclerOptions<EventoAndLuchadores> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull EventoAndLuchadores model) {
            holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_list_luchadores_evento,parent,false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewLuchador;
        TextView textViewNameAtleta,textViewAlias,textViewGym,textViewRango;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewLuchador=itemView.findViewById(R.id.imageViewLuchador);
            textViewNameAtleta=itemView.findViewById(R.id.textViewNameAtleta);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewGym=itemView.findViewById(R.id.textViewGym);
            textViewRango=itemView.findViewById(R.id.textViewRango);
        }

        void Bind(EventoAndLuchadores model){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, LuchadoresActivity.class);
                    intent.putExtra("id",model.getIdLuchador());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });
            FirebaseFirestore.getInstance().collection("Luchadores").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                        textViewNameAtleta.setText(name.toUpperCase());
                        textViewAlias.setText(alias.toUpperCase());

                        Picasso.with(context).load(urlImage).placeholder(R.drawable.spiner).fit().into(imageViewLuchador);

                    }
                }
            });
            if (model.getIdGym()!=null){
                if (!model.getIdGym().isEmpty()){
                    FirebaseFirestore.getInstance().collection("Gyms").document(model.getIdGym()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String name=documentSnapshot.getString("nameGym");
                                textViewGym.setVisibility(View.VISIBLE);
                                textViewGym.setText(name.toUpperCase());
                            }else{
                                textViewGym.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }


        }
    }
}
