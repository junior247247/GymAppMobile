package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import Models.Resenas;

public class ResenaAdapter extends FirestoreRecyclerAdapter<Resenas,ResenaAdapter.viewHolder> {


    public ResenaAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Resenas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position, @NonNull @NotNull Resenas model) {
            holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_resenas,parent,false));
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        TextView textViewUserName,textViewResena;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewUserName=itemView.findViewById(R.id.textViewUserName);
            textViewResena=itemView.findViewById(R.id.textViewResena);
        }
        void Bind(Resenas model){
            textViewResena.setText(model.getMessge());
            FirebaseFirestore.getInstance().collection("Luchadores").document(model.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                     if (documentSnapshot.exists()){
                         String name=documentSnapshot.getString("name");
                         textViewUserName.setText(name);
                     }else{
                         FirebaseFirestore.getInstance().collection("Gyms").document(model.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String name=documentSnapshot.getString("nameGym");
                                        textViewUserName.setText(name);
                                    }else{
                                        FirebaseFirestore.getInstance().collection("UserStand").document(model.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()){
                                                    String name=documentSnapshot.getString("name");
                                                    textViewUserName.setText(name);
                                                }else{
                                                    FirebaseFirestore.getInstance().collection("UserAdmin").document(model.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if (documentSnapshot.exists()){
                                                                String name=documentSnapshot.getString("nombre");
                                                                textViewUserName.setText(name);
                                                            }else{
                                                                textViewUserName.setText("anonymous");
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                             }
                         });
                     }
                }
            });
        }
    }
}
