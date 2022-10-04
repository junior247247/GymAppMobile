package imageprovider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;

public class ImageResource {
        Context context=null;
        Activity activity=null;
        
    public ImageResource(Context context,Activity activity){
        this.context=context;
        this.activity=activity;
    }

    public ArrayList<File> getImageResource(){
        ArrayList<String> img=new ArrayList<>();
        ArrayList<File> imgint= new ArrayList<File>();
        if(isPermission()){

            final String[] colums={MediaStore.Images.Thumbnails.DATA};
            Cursor cursor=context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,colums,null,null,null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{
                       @SuppressLint("Range") String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));

                        File file= new File(path);
                        imgint.add(file);
                       // img.add(imgResource);
                    }while (cursor.moveToNext());
                }
            }
            cursor.close();
        }

        return imgint;
    }

    private boolean isPermission(){
        int check= context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(check== PackageManager.PERMISSION_GRANTED){
            return  true;
        }else{
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            return false;
        }
    }
}
