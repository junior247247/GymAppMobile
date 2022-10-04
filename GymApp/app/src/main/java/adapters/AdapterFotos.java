package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.FinalizarEvento;
import app.makingfight.gymapp.R;

public class AdapterFotos  extends FirestoreRecyclerAdapter<FinalizarEvento,AdapterFotos.ViewHolde> {
        AdapterResultadosNuevos adapterResultadosNuevos;
        Context context;
        LinearLayoutManager linearLayoutManager;
        ArrayList<String> list= new ArrayList<>();
    public AdapterFotos(@NonNull FirestoreRecyclerOptions<FinalizarEvento> options,Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull FinalizarEvento model) {
        holder.Bind(model.getIdEvento());


    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_resultados_con_fotos,parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(position);

        return super.getItemViewType(position);
    }

    int getViewType(String id){
        return 0;
    }

    public  class ViewHolde extends RecyclerView.ViewHolder{
        RecyclerView recyclerViewItem;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            //recyclerViewItem=itemView.findViewById(R.id.recycleViewItemsFotos);
        }

        void Bind(String id){

            ImageView imageView =itemView.findViewById(R.id.imageViewFotoEvento);
            FirebaseFirestore.getInstance().collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String img=documentSnapshot.getString("urlImage");
                        Picasso.with(context).load(img).fit().fit().into(imageView);
                    }
                }
            });

        }
    }


}
