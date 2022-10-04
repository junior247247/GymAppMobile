package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterRegiones extends  RecyclerView.Adapter<AdapterRegiones.ViewHolder>{
    ArrayList<String> list;
    onCliCLisetener cLisetener;
    public  AdapterRegiones(ArrayList<String> list,onCliCLisetener onCliCLisetener){
        this.list=list;
        this.cLisetener=onCliCLisetener;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_regiones,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textViewRegion.setText(list.get(position).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
    TextView textViewRegion;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewRegion=itemView.findViewById(R.id.textViewRegion);
            textViewRegion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cLisetener.onItemClick(list.get(getAdapterPosition()));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cLisetener.onItemClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public  interface onCliCLisetener{
        void onItemClick(String txt);
    }
}
