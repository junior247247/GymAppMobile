package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.makingfight.gymapp.R;
import app.makingfight.gymapp.RankingActivity;

public class AdapterListaRegiones extends RecyclerView.Adapter<AdapterListaRegiones.ViewHolder> {
    ArrayList<String> list;
    ClickListener clickListener;
    public  AdapterListaRegiones (ArrayList<String> list,ClickListener clickListener) {
        this.list=list;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_regiones_seleccionado,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewRegion.setText(list.get(position));
        holder.bottonEliminarRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RankingActivity.mejorList.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewRegion;
        Button bottonEliminarRegion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bottonEliminarRegion=itemView.findViewById(R.id.bottonEliminarRegion);
            textViewRegion=itemView.findViewById(R.id.textViewRegion);
        }
    }

    public  interface  ClickListener{
        void onItemClick(int position);
    }

}
