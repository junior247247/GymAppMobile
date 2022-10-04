package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import app.makingfight.gymapp.EventoYfotos;
import app.makingfight.gymapp.R;

public class AdapterResultadosNuevos extends FirestoreRecyclerAdapter<EventoYfotos,AdapterResultadosNuevos.ViewHolder> {
    Context context;
    String nameAll;
    String apellido1,apellido2;
    String name,name1;


    public AdapterResultadosNuevos(@NonNull FirestoreRecyclerOptions<EventoYfotos> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull EventoYfotos model) {
            if (model.getTipo()==1){
                holder.BindPhoto(model.getIdEvento());
            }else{
                holder.Bind(model.getIdCaganador(), model.getIdPerdedor(),model.isEsEmpate());

            }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            switch (viewType){
                case 1:
                    return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_resultados_con_fotos,parent,false));
                case 2:
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_resultados_nuevo,parent,false));

                default: return  null;
            }

    }

    @Override
    public int getItemViewType(int position) {

        DocumentSnapshot snapshot=getSnapshots().getSnapshot(position);

        if (snapshot.getLong("tipo")==1){
            return 1;
        }else{
            return  2;
        }

    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNombres,textViewNombreGanador,textiViewGanador;
        ImageView imageViewFotoEvento,imageViewFoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFotoEvento=itemView.findViewById(R.id.imageViewFotoEvento);
            textViewNombres=itemView.findViewById(R.id.textViewNombres);
            textViewNombreGanador=itemView.findViewById(R.id.textViewNombreGanador);
            imageViewFoto=itemView.findViewById(R.id.imageViewFotoEvento);
            textiViewGanador=itemView.findViewById(R.id.textiViewGanador);
        }

        void BindPhoto(String idEvento){
            FirebaseFirestore.getInstance().collection("Eventos").document(idEvento).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String urlImage=documentSnapshot.getString("urlImage");
                        Picasso.with(context).load(urlImage).fit().into(imageViewFoto);
                    }
                }
            });
        }

        void Bind(String idGanador,String idPerdedor,boolean isEmapte){

            FirebaseFirestore.getInstance().collection("Luchadores").document(idGanador).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                         name=documentSnapshot.getString("name");

                        String img=documentSnapshot.getString("urlImage");
                        Picasso.with(context).load(img).fit().into(imageViewFotoEvento);
                        for(int i=0;i<name.length();i++){
                            if(name.substring(0,i).contains(" ")){
                                apellido1=name.substring(i,name.length());
                                break;
                            }
                        }

                        FirebaseFirestore.getInstance().collection("Luchadores").document(idPerdedor).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    name1=documentSnapshot.getString("name");
                                    for(int i=0;i<name1.length();i++){
                                        if(name1.substring(0,i).contains(" ")){
                                            apellido2=name1.substring(i,name1.length());
                                            break;
                                        }
                                    }
                                    nameAll=apellido1+" VS "+apellido2;
                                    if (isEmapte){
                                        textiViewGanador.setText("EMPATE");
                                        textViewNombreGanador.setText("");
                                    }else{
                                        textiViewGanador.setText("GANADOR:");
                                        textViewNombreGanador.setText(name.toUpperCase());
                                        textViewNombres.setText(nameAll.toUpperCase());
                                    }
                                    textViewNombres.setText(nameAll.toUpperCase());
                                }
                            }
                        });
                    }
                }
            });
        }
    }



}
