package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;

import java.util.ArrayList;

public class AdapterBocales extends RecyclerView.Adapter<AdapterBocales.ViewHolder>{
    Context context;
    ArrayList<String> list;
    clickAdapterLisener lisener;

    public AdapterBocales(Context context,ArrayList<String> list,clickAdapterLisener lisener){
        this.context=context;
        this.list=list;
        this.lisener=lisener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_items_bocal,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBocales.ViewHolder holder, int position) {
        holder.textViewBocal.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView textViewBocal;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textViewBocal=itemView.findViewById(R.id.textViewBocales);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lisener.onClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface clickAdapterLisener{
        void onClick(String letra);
    }
}
