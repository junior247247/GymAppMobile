package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Models.UserAdmin;
import Models.Users;
import authprovider.AuthenticatorProvider;
import dmax.dialog.SpotsDialog;

public class AdminRegisterActivity extends AppCompatActivity {
    AuthenticatorProvider authenticatorProvider;
    TextInputEditText textInputNombreUserStant,textInputEmailUserStand,textInputPasswordUserStand,textInputPasswordConfirmUserStand;
    Button buttonRegister;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    AlertDialog alertDialog;
    String urlImage="https://firebasestorage.googleapis.com/v0/b/makingfightfinal.appspot.com/o/imageNotPhoto.jpg?alt=media&token=974348bd-9e3b-4f83-afca-981e00ae2e25";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_admin_register);
        BinUid();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

    }

    void BinUid(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("REGISTRO");
        textInputEmailUserStand=findViewById(R.id.textInputEmailUserStand);
        textInputNombreUserStant=findViewById(R.id.textInputNombreUserStant);
        textInputPasswordUserStand=findViewById(R.id.textInputPasswordUserStand);
        textInputPasswordConfirmUserStand=findViewById(R.id.textInputPasswordConfirmUserStand);
        buttonRegister=findViewById(R.id.buttonRgisterUserStand);
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        authenticatorProvider= new AuthenticatorProvider();
        alertDialog=new SpotsDialog.Builder().setMessage("ESPERE UN MOMENTO").setContext(this).build();
    }
    void goToHomeActivity(){
        Intent intent= new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    void createUser(){
        String email=textInputEmailUserStand.getText().toString();
        String pass=textInputPasswordUserStand.getText().toString();
        String passConfirm=textInputPasswordConfirmUserStand.getText().toString();
        String nameUser=textInputNombreUserStant.getText().toString();
        if (!email.isEmpty()){
            if (Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                if (!pass.isEmpty() && !passConfirm.isEmpty()){
                    if (pass.length()>=6){
                        alertDialog.show();
                        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    UserAdmin userAdmin= new UserAdmin();
                                    userAdmin.setId(auth.getCurrentUser().getUid());
                                    userAdmin.setNombre(nameUser);
                                    userAdmin.setEmail(email);
                                    userAdmin.setPermiso("admin");
                                    userAdmin.setAprovacion("no");
                                    Users users= new Users();
                                    users.setName(nameUser);
                                    users.setId(auth.getCurrentUser().getUid());
                                    users.setUrlImg(urlImage);
                                    FirebaseFirestore.getInstance().collection("Users").document(users.getId()).set(users);



                                    authenticatorProvider.createUserAdmin(userAdmin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            alertDialog.dismiss();
                                            goToHomeActivity();
                                        }
                                    });

                                }else{
                                    showToas("No se pudo crear el usuario");
                                }
                            }
                        });
                    }else{
                        showToas("La contraseña debe tener minimo 6 caracteres");
                    }
                }else{
                    showToas("Debes ingresar la contraseña y confirmarla");
                }
            }else{
                showToas("Email no Valido");
            }
        }else{
            showToas("Debes completar el campo email");
        }
    }

    void showToas(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}