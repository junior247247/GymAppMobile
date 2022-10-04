package service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.TokenModels;

public class TokenProvider {
    CollectionReference reference;

    public TokenProvider(){
        reference= FirebaseFirestore.getInstance().collection("Tokens");
    }

    public void CreateToken(String idUser){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull  Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                String token=task.getResult();
                Map<String ,String> map= new HashMap<>();
                TokenModels tokenModels=new TokenModels(token,new Date().getTime());
                reference.document(idUser).set(tokenModels);
            }
        });
    }

}
