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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.Gym;
import app.makingfight.gymapp.ProfileGymActivity;
import app.makingfight.gymapp.R;

public class AdapterGymnasios extends FirestoreRecyclerAdapter<Gym,AdapterGymnasios.ViewHolder> {

    Context context;
    Activity activity;
    public AdapterGymnasios(@NonNull @NotNull FirestoreRecyclerOptions<Gym> options, Context context, Activity activity) {
        super(options);
        this.context=context;
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Gym model) {
        holder.Bind(model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, ProfileGymActivity.class);
                intent.putExtra("id",model.getId());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_gimnasios,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewGym;
        TextView textViewNameGym,textViewDesdeGym,textViewArtesMarciales;;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewGym=itemView.findViewById(R.id.imageViewGym);
            textViewNameGym=itemView.findViewById(R.id.textViewNameGym);
            textViewDesdeGym=itemView.findViewById(R.id.textViewDesdeGym);
            textViewArtesMarciales=itemView.findViewById(R.id.textViewArtesMarciales);
        }

        void  Bind(Gym model){
            Picasso.with(context).load(model.getUrlImageProfile()).fit().placeholder(R.drawable.spiner).into(imageViewGym);
           textViewDesdeGym.setText( model.getDesde()+" - "+model.getHasta());
           textViewArtesMarciales.setText(model.getArtesmarciales());
           textViewNameGym.setText(model.getNameGym().toUpperCase());
        }
    }
}
