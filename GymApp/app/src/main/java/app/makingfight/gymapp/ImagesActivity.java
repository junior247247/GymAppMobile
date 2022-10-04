package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import app.makingfight.gymapp.R;

import java.io.File;
import java.util.ArrayList;

import adapters.AdapterImage;
import imageprovider.ImageResource;

public class ImagesActivity extends AppCompatActivity {
        ImageResource imageResource;
        RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        imageResource= new ImageResource(this,this);
        ArrayList<File> list=imageResource.getImageResource();
        recyclerView=findViewById(R.id.recicleViewImages);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(new AdapterImage(this,list));

    }

}