package adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.EventoAndLuchadores;
import app.makingfight.gymapp.R;

public class adapterListaLuchadores extends FirestoreRecyclerAdapter<EventoAndLuchadores, adapterListaLuchadores.ViewHolder> {
    Context context;
    SparseBooleanArray sparseBooleanArray= new SparseBooleanArray();
    public static ArrayList<String> ids= new ArrayList<>();
    int i=0;
    public adapterListaLuchadores(@NonNull FirestoreRecyclerOptions<EventoAndLuchadores> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull EventoAndLuchadores model) {
        holder.BindOne(model.getIdLuchador(),position);



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_selecionar_versus,parent,false));
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView imageView;
        LinearLayout linearlayout1Clcik;

        TextView textViewCategoria,textViewNombre,textViewAlias,textViewGanadaPerdidas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.view);
            imageView=itemView.findViewById(R.id.imageView);
            textViewCategoria=itemView.findViewById(R.id.textViewCategoria);
            textViewNombre=itemView.findViewById(R.id.textViewNombre);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewGanadaPerdidas=itemView.findViewById(R.id.textViewGanadaPerdidas);
            linearlayout1Clcik=itemView.findViewById(R.id.linearlayout1Clcik);

        }

        void BindOne(String id, int position){
           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (sparseBooleanArray!=null){

                        if (sparseBooleanArray.get(position,true)){
                            sparseBooleanArray.put(position,false);
                            ids.add(id);
                            view.setVisibility(View.VISIBLE);
                            i=i+1;
                           // Toast.makeText(context, "exite "+position, Toast.LENGTH_SHORT).show();
                        }else{
                           view.setVisibility(View.GONE);
                            sparseBooleanArray.put(position,true);
                           // Toast.makeText(context, "no exite "+position, Toast.LENGTH_SHORT).show();
                            i=i-1;
                            for (int i=0;i<ids.size();i++){
                                if (ids.get(i).equals(id)){
                                    ids.remove(i);
                                }
                            }
                        }

                    }


                }
            });


            FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        String dato="";
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");
                        String categoria=documentSnapshot.getString("categoria");
                        if (!categoria.isEmpty()){
                            textViewCategoria.setText(categoria);
                        }else{
                            textViewAlias.setText("");
                        }
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
                        Picasso.with(context).load(documentSnapshot.getString("urlImage")).fit().placeholder(R.drawable.spiner).into(imageView);
                        textViewGanadaPerdidas.setText(dato);
                        String name=documentSnapshot.getString("name").toUpperCase();
                        if (name.length()>14){
                            textViewNombre.setText(name.toUpperCase().substring(0,12)+"...");
                        }else{
                            textViewNombre.setText(name.toUpperCase());
                        }
                        textViewAlias.setText(documentSnapshot.getString("alias").toUpperCase().trim());
                    }
                }
            });

        }
    }
}
