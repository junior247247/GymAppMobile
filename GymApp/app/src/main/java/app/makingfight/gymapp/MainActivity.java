package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Models.SliderItems;
import Models.Users;
import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity {

    TextView textViewRegister;

    Button buttonLogin;
    LinearLayout buttonloginGoogle;
    FirebaseAuth auth;
    AlertDialog alertDialog;
    GoogleSignInClient googleSignInClient;
    TextInputEditText textInputPassword,textInputEmail;
    static final int RC_SIGN_IN=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_GymApp);
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set an exit transition
        getWindow().setExitTransition(new Explode());


        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }



        BindIud();

        setListener();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Intent intent= new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent);
            showModeLogin(auth.getCurrentUser().getUid());
        }

    }

    private void showModeLogin(String id){
        FirebaseFirestore.getInstance().collection("Gyms").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                            showModeLogin("Has iniciado sesion como gimnasio");
                    }else{
                        FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    showModeLogin("Has iniciado sesion como luchador");
                                }
                            }
                        });
                    }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        if (requestCode==RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                alertDialog.show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()) {
                            // si ninico sesion
                            Users user= new Users();
                            user.setUrlImg(auth.getCurrentUser().getPhotoUrl().toString());
                            user.setId(auth.getCurrentUser().getUid());
                            user.setName(auth.getCurrentUser().getDisplayName());
                            FirebaseFirestore.getInstance().collection("Users").document(user.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (!documentSnapshot.exists()){
                                        FirebaseFirestore.getInstance().collection("Users").document(user.getId()).set(user);
                                    }
                                }
                            });
                            showToast("Has iniciado sesion como espectador");
                            goToHomeAcitivy();


                        } else {
                            //no inicio sesion con google
                        }
                    }
                });
    }

    void setListener(){
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Intent intent= new Intent(MainActivity.this, SecetionModeRegisterActivity.class);
                Intent intent= new Intent(MainActivity.this,SecetionModeRegisterActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(intent,bundle);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        buttonloginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }


    void BindIud(){
        textViewRegister=findViewById(R.id.textViewRegister);
        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        alertDialog= new SpotsDialog.Builder().setMessage("Espere un momento").setContext(this).build();
        textInputEmail=findViewById(R.id.textInputEmail);
        textInputPassword=findViewById(R.id.textInputPassword);
        auth=FirebaseAuth.getInstance();
        buttonloginGoogle=findViewById(R.id.buttonLoginGoogle);
    }

    void login(){
        String email=textInputEmail.getText().toString();
        String pass=textInputPassword.getText().toString();
        if (!email.trim().isEmpty()){
            if (!pass.isEmpty()){
                alertDialog.show();
                auth.signInWithEmailAndPassword(email.trim(),pass.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()){
                            goToHomeAcitivy();
                            showModeLogin(auth.getCurrentUser().getUid());
                        }else{
                            Toast.makeText(MainActivity.this, "Email o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Debes ingresar la contraña", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Debes ingresar el email", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    void goToHomeAcitivy(){
        Intent intent =new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}