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

import Models.Ranking;
import app.makingfight.gymapp.LuchadoresActivity;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AdapterMejores extends FirestoreRecyclerAdapter<Ranking,AdapterMejores.ViewHolder> {
    Context context;
    Activity activity;
    FirebaseFirestore firestore;
    public AdapterMejores(@NonNull @NotNull FirestoreRecyclerOptions<Ranking> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
        this.firestore=FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Ranking model) {
        holder.Bind(model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, LuchadoresActivity.class);
                intent.putExtra("id",model.getIdLuchador());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_items_mejor_por_regiones,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewLuchador;
        TextView textViewAlias,textViewNombre,textViewMejorRegion,textViewRacha,textViewPosicion,textViewEstadisticas;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewLuchador=itemView.findViewById(R.id.imageViewLuchador);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewNombre=itemView.findViewById(R.id.textViewNombre);
            textViewMejorRegion=itemView.findViewById(R.id.textViewMejorRegion);
            textViewRacha=itemView.findViewById(R.id.textViewRacha);
            textViewPosicion=itemView.findViewById(R.id.textViewPosicion);
            textViewEstadisticas=itemView.findViewById(R.id.textViewEstadisticas);

        }

        void Bind(Ranking model){
            textViewPosicion.setText("#"+model.getPosiciontion());
            firestore.collection("Luchadores").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){

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


                        textViewEstadisticas.setText(dato);
                        String name=documentSnapshot.getString("name");
                        String img=documentSnapshot.getString("urlImage");
                        String alias=documentSnapshot.getString("alias");
                        Picasso.with(context).load(img).fit().placeholder(R.drawable.spiner).into(imageViewLuchador);
                        textViewAlias.setText(alias.toUpperCase().trim());
                        textViewNombre.setText(name.toUpperCase());


                        textViewMejorRegion.setText(model.getRegion());
                        FirebaseFirestore.getInstance().collection("ranchas").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String racha=documentSnapshot.getString("Ganadas");
                                if (Integer.parseInt(racha)>=3){
                                    textViewRacha.setText("RACHA ACTUAL:"+racha);
                                }else{
                                    textViewRacha.setText("");
                                }

                            }
                            }
                        });
                    }
                }
            });


        }
    }
}
