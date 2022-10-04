package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
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

import Models.RegisterPromotorModel;
import Models.Users;
import authprovider.AuthenticatorProvider;
import dmax.dialog.SpotsDialog;

public class RegisterPromotorActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText textInputEditTextEmail,textInputEditTextPassword,textInputEditTextPasswordConfirm,textInputEditTextEdad,textInputEditTextCantidadDeEvento,texinputCiudadesEventos;
    Button buttonRegister;
    FirebaseAuth auth;
    AlertDialog alertDialog;
    AuthenticatorProvider authenticatorProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_register_promotor);
        Buind();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }
    void Buind(){
        auth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonRegister=findViewById(R.id.buttonRegisterPromotor);
        textInputEditTextEmail=findViewById(R.id.textinputCorreoPromotor);
        textInputEditTextPassword=findViewById(R.id.textInputPassword);
        textInputEditTextPasswordConfirm=findViewById(R.id.ConfirmPromotor);
        textInputEditTextEdad=findViewById(R.id.textInputEdadPromotor);
        textInputEditTextCantidadDeEvento=findViewById(R.id.texInputCantidadDeEventos);
        texinputCiudadesEventos=findViewById(R.id.texinputCiudadesEventos);
        alertDialog= new SpotsDialog.Builder().setContext(this).setMessage("ESPERE UN MOMENTO").build();
        authenticatorProvider= new AuthenticatorProvider();
    }

    void createUser(){
        String email=textInputEditTextEmail.getText().toString().trim();
        String pass=textInputEditTextPassword.getText().toString().trim();
        String passConfirm=textInputEditTextPasswordConfirm.getText().toString().trim();
        String edad=textInputEditTextEdad.getText().toString().trim();
        String cantidadDeEventos=textInputEditTextCantidadDeEvento.getText().toString().trim();
        String ciudadesEventos=texinputCiudadesEventos.getText().toString().trim();
        if (!email.isEmpty() && !pass.isEmpty() && !passConfirm.isEmpty() && !edad.isEmpty() && !cantidadDeEventos.isEmpty() && !ciudadesEventos.isEmpty()){
            if (pass.length()>=6){
                alertDialog.show();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            alertDialog.dismiss();
                            if (task.isSuccessful()){
                                RegisterPromotorModel model=new RegisterPromotorModel();
                                model.setId(auth.getCurrentUser().getUid());
                                model.setEmail(email);
                                model.setCantidadDeEventos(cantidadDeEventos);
                                model.setEdad(edad);
                                model.setCiudadesDeLosEventos(ciudadesEventos);

                                Users users= new Users();
                                users.setName("Anonymous");
                                users.setId(auth.getCurrentUser().getUid());
                                users.setUrlImg("");
                                FirebaseFirestore.getInstance().collection("Users").document(users.getId()).set(users);


                                Intent intent= new Intent(RegisterPromotorActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                showToast("No se puedo crear el usuario");
                            }
                        }
                    });
                }else{
                    showToast("EMAIL NO VALIDO");
                }

            }else{
                showToast("LA CONTRASEÑA NO PUEDE TENER MENOS DE 6 CARACTERES");
            }
        }else{
           showToast("COMPLETA TODOS LOS CAMPOS");
         }

    }


    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}