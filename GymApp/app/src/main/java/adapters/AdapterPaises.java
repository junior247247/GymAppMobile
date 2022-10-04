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

import Models.Paises;

public class AdapterPaises extends RecyclerView.Adapter<AdapterPaises.ViewHolder>{
    ArrayList<Paises> paises;
    ArrayList<String> list= new ArrayList<>();
    onItemClick itemClick;

    public  AdapterPaises(ArrayList<Paises> paises,onItemClick itemClick){
        this.paises=paises;
        this.itemClick=itemClick;

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_regiones,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        for (int i=0;i<paises.size();i++){
            list.add(paises.get(i).getName());
        }

        holder.textViewRegion.setText(list.get(position));

        holder.textViewRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.itemClick(paises.get(position).getName(),paises.get(position).getCode());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.itemClick(paises.get(position).getName(),paises.get(position).getCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return paises.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewRegion;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewRegion=itemView.findViewById(R.id.textViewRegion);
        }
    }
    public  interface onItemClick{
        void itemClick(String pais,String code);
    }
}
