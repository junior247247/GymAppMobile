package imageprovider;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class SaveImagerToFirebase {
    StorageReference mstorage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://making-eb3e6.appspot.com");

    public SaveImagerToFirebase(){

    }

    public UploadTask saveImage(byte[] img){
        StorageReference reference=storageRef.child(new Date().getTime() +".jpg");
        mstorage=reference;
        UploadTask task=reference.putBytes(img);
        return task;
    }

    public Task<Void> delete(String url){
        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(url);
        StorageReference reference=storageRef.child(storageReference.getName());
        mstorage=reference;
        return mstorage.delete();
    }


    public UploadTask save(Uri uri){
        StorageMetadata metadata= new StorageMetadata.Builder().build();

        return  storageRef.putFile(uri);
    }
    public StorageReference getStorage(){
        return  mstorage;
    }


    public UploadTask saveVideo(Uri uri){
        StorageReference reference=storageRef.child("video"+new Date().getTime());
        mstorage=reference;
        UploadTask task=reference.putFile(uri);
        return task;
    }
}
