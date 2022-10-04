package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;

import java.util.ArrayList;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolder>{
    ArrayList<String> list;
    clickListener listener;
    public AdapterCategorias(ArrayList<String> list,clickListener listener){
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_categorias,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  AdapterCategorias.ViewHolder holder, int position) {
        holder.textViewCategorias.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView textViewCategorias;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textViewCategorias=itemView.findViewById(R.id.textViewCategorias);
        }
    }

    public interface clickListener{
        void itemClick(String dato);
    }
}
