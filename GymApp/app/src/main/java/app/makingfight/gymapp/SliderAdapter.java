package app.makingfight.gymapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.makingfight.gymapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.SliderItems;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    ArrayList<SliderItems> list;
    Context context;

    public SliderAdapter(Context context,ArrayList<SliderItems> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_slider_layout_items,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(context).load(list.get(position).getImgResource()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageViewSlider);
        }
    }
}
