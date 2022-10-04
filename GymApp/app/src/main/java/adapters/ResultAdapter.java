package adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import app.makingfight.gymapp.ResultActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import Models.FinalizarEvento;

public class ResultAdapter extends FirestoreRecyclerAdapter<FinalizarEvento, ResultAdapter.ViewHolder> {
    Context context;
    Activity activity;
    public ResultAdapter(@NonNull @NotNull FirestoreRecyclerOptions<FinalizarEvento> options,Context context ,Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull FinalizarEvento model) {
        holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_result,parent,false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewLugar;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textViewNombreDelEvento);
            textViewLugar=itemView.findViewById(R.id.textViewLugar);
        }

        public void Bind(FinalizarEvento models){

            FirebaseFirestore.getInstance().collection("Eventos").document(models.getIdEvento()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String title=documentSnapshot.getString("nombreEvento");
                        String lugar=documentSnapshot.getString("lugar");
                        if (lugar.length()>=30){
                            textViewLugar.setText(lugar.toUpperCase().substring(0,30)+" ...");
                        }else{
                            textViewLugar.setText(lugar.toUpperCase());
                        }
                        if (title.length()>=15){
                            textViewTitle.setText(title.toUpperCase().substring(0,15)+" ...");
                        }else{
                            textViewTitle.setText(title.toUpperCase());
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, ResultActivity.class);
                    intent.putExtra("id",models.getIdEvento());
                    Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                    context.startActivity(intent,bundle);
                }
            });

        }


    }
}
