package authprovider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import Models.Gym;
import Models.Luchador;
import Models.RegisterGym;
import Models.RegisterLuchadorModel;
import Models.RegisterPromotorModel;
import Models.RegisterUserStadard;
import Models.UserAdmin;

public class AuthenticatorProvider {
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    public AuthenticatorProvider(){
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public Task<AuthResult> createUserEmailAndPassord(String emal, String pass){
      return auth.createUserWithEmailAndPassword(emal,pass);
    }

    public String getUidUser(){
        if (auth.getCurrentUser()!=null){
            return auth.getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    public Task<Void> createUserForGym(RegisterGym registerGym){
        return firebaseFirestore.collection("Users").document(registerGym.getId()).set(registerGym);
    }

    public Task<Void> createUserForLuchador(Luchador model){
        return firebaseFirestore.collection("Luchadores").document(model.getId()).set(model);
    }

    public  Task<Void> createUserAdmin(UserAdmin userAdmin){
        return firebaseFirestore.collection("UserAdmin").document(userAdmin.getId()).set(userAdmin);
    }

    public Task<Void> createUserForPromotor(RegisterPromotorModel model){
        return firebaseFirestore.collection("UserPromotor").document(model.getId()).set(model);
    }
    public Task<Void> createUserStandt(RegisterUserStadard model){
        return firebaseFirestore.collection("UserStand").document(model.getId()).set(model);
    }
    public Task<Void> createGym(Gym model){
        return firebaseFirestore.collection("Gyms").document(model.getId()).set(model);
    }
}
