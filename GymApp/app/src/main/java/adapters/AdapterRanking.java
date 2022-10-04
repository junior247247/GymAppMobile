package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import Models.Ranking;
import app.makingfight.gymapp.R;

public class AdapterRanking extends FirestoreRecyclerAdapter<Ranking,AdapterRanking.ViewHolder> {


    public AdapterRanking(@NonNull FirestoreRecyclerOptions<Ranking> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ranking model) {
        holder.posicionRanking.setText("#"+model.getPosiciontion());
        holder.textViewCiudad.setText(model.getRegion());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_ranking,parent,false) );

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView posicionRanking,textViewCiudad;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posicionRanking=itemView.findViewById(R.id.posicionRanking);
            textViewCiudad=itemView.findViewById(R.id.textViewCiudad);
        }
    }
}
