package app.makingfight.gymapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

import Models.SliderRegisterItems;

public class SliderRegister extends SliderViewAdapter<SliderRegister.ViewHolder> {
    ArrayList<SliderRegisterItems> items;

    public  SliderRegister(ArrayList<SliderRegisterItems> list){
        this.items=list;
    }
    @Override
    public SliderRegister.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SliderRegister.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_slider_register,parent,false));
    }

    @Override
    public void onBindViewHolder(SliderRegister.ViewHolder viewHolder, int position) {
        viewHolder.textViewTitle.setText(items.get(position).getTitle());
        viewHolder.textViewDescription.setText(items.get(position).getDescription());
    }

    @Override
    public int getCount() {
        return items.size();
    }


    public class ViewHolder extends SliderViewAdapter.ViewHolder{
        TextView textViewTitle,textViewDescription;
        public ViewHolder(View itemView) {
            super(itemView);
           textViewTitle=itemView.findViewById(R.id.title);
           textViewDescription=itemView.findViewById(R.id.description);
        }
    }
}
