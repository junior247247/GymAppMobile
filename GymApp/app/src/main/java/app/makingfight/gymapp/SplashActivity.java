package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.widget.Toast;

import app.makingfight.gymapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

       //  setContentView(R.layout.activity_splash);

        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Intent intent= new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
           // Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Toast.makeText(this, "sesion iniciada", Toast.LENGTH_SHORT).show();
            //startActivity(intent);

        }else{
            Intent intent= new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //Bundle bundle=ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent);
        }


    }

}