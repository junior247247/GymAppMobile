package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ViewHolder>{
    ArrayList<File> list;
    ArrayList<Boolean> isSelected= new ArrayList<>();
    ArrayList<String> saveImageSelected=new ArrayList<>();
    Context context;

    public AdapterImage(Context context,ArrayList<File> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_images_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  AdapterImage.ViewHolder holder, int position) {
        Bitmap bitmap= BitmapFactory.decodeFile(list.get(position).getAbsolutePath());
        holder.imageView.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    View view;
    public ViewHolder(@NonNull  View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageViewImageLoader);
        view=itemView.findViewById(R.id.viewImage);
    }
}
}
