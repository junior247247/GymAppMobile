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

import app.makingfight.gymapp.R;
import app.makingfight.gymapp.UpdateStadisticaActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.solicitudLuchador;

public class solicitudAdapter extends FirestoreRecyclerAdapter<solicitudLuchador,solicitudAdapter.ViewHolder> {

    Context context;
    Activity activity;

    public solicitudAdapter(@NonNull @NotNull FirestoreRecyclerOptions<solicitudLuchador> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull solicitudLuchador model) {
            holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_solicitudes,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imaeViewSolicitud;
        TextView textViewTittle,textViewMessage;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewMessage=itemView.findViewById(R.id.textViewMessage);
            textViewTittle=itemView.findViewById(R.id.textViewTittle);
            imaeViewSolicitud=itemView.findViewById(R.id.imaeViewSolicitud);
        }

        void Bind(solicitudLuchador model){
            
            FirebaseFirestore.getInstance().collection("Luchadores").document(model.getIdLuchador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String urlImage=documentSnapshot.getString("urlImage");
                        Picasso.with(context).load(urlImage).placeholder(R.drawable.spiner).fit().into(imaeViewSolicitud);
                        textViewTittle.setText(model.getTitle());
                        textViewMessage.setText(model.getMessage());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, UpdateStadisticaActivity.class);
                    intent.putExtra("id",model.getIdLuchador());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });
        }
    }
}
