package service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewrMessageHelper {
    static FirebaseAuth auth=FirebaseAuth.getInstance();
    static FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public  static void updateOnline(boolean status,final Context context){

        if (auth.getCurrentUser().getUid()!=null){
            isAApplicationSendToBackground(context);
            updateOnline(auth.getUid(),status);
        }else if(status){
            updateOnline(auth.getCurrentUser().getUid(),status);
        }

    }

    private static void updateOnline(String id, boolean status){
        Map<String,Object> map= new HashMap<>();
        map.put("is online",status);
        firestore.collection("online").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        firestore.collection("online").document(id).update(map);
                    }   else{
                        firestore.collection("online").document(id).set(map);
                    }
            }
        });

    }


    public static  boolean isAApplicationSendToBackground(final Context context){
        ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos=activityManager.getRunningTasks(1);
        if (!taskInfos.isEmpty()){
            ComponentName topActivity=taskInfos.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())){
                return true;
            }
        }
        return false;
    }
}
