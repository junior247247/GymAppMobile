package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import Models.SliderRegisterItems;
import app.makingfight.gymapp.R;

public class SecetionModeRegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button buttonRegisterGym,buttonRegisterPromotor,buttonRegisterStand,buttonRegisterAmdin;
    LinearLayout linearLayoutRegistroLuchador,linearLayoutRegistroGimnasio,linearLayoutRegistroEspectador;
    SliderView sliderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_secetion_mode_register);
        BindUid();
        BindSlider();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        linearLayoutRegistroGimnasio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SecetionModeRegisterActivity.this,GymRegisterActivity.class);
                startActivity(intent);
            }
        });



        linearLayoutRegistroLuchador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SecetionModeRegisterActivity.this,RegisterLuchadorActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutRegistroEspectador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SecetionModeRegisterActivity.this, RegisterUserStandActivity.class);
                startActivity(intent);
            }
        });

        buttonRegisterAmdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SecetionModeRegisterActivity.this,AdminRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    void BindUid(){
        this.buttonRegisterAmdin=findViewById(R.id.buttonRegisterAmdin);


    this.buttonRegisterPromotor=findViewById(R.id.buttonRegisterPromotor);
       this.linearLayoutRegistroLuchador=findViewById(R.id.linearLayoutRegistroLuchador);
      this.linearLayoutRegistroGimnasio=findViewById(R.id.linearLayoutRegistroGimnasio);
      this.linearLayoutRegistroEspectador=findViewById(R.id.linearLayoutRegistroEspectador);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Opcciones de Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    void BindSlider(){



        ArrayList<SliderRegisterItems> list =new ArrayList(){{
            add(new SliderRegisterItems("Luchador","Si ya conoces el área y formas parte de esa disciplina registra tu perfil como luchador"));
            add(new SliderRegisterItems("Espectador","si disfruta de los eventos y quieres seguir a tus luchadores favoritos desde cerca"));
            add(new SliderRegisterItems("Gimnasio","Si eres dueño o líder de un gimnasio y deseas registrar a tus luchadores de Making Fight"));
        }};


        sliderView=findViewById(R.id.sliderView);

        SliderRegister adapter= new SliderRegister(list);
        sliderView.setSliderAdapter(adapter);
        // sliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        //sliderView.setIndicatorSelectedColor(Color.GRAY);
        // sliderView.setIndicatorUnselectedColor(Color.WHITE);

        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
}