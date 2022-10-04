package adapters;

import android.app.Activity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import Models.UltimaPeleas;

public class AdapterCircuitos extends FirestoreRecyclerAdapter<UltimaPeleas,AdapterCircuitos.ViewHolder> {
    Activity context;
    String id;



    public AdapterCircuitos(@NonNull @NotNull FirestoreRecyclerOptions<UltimaPeleas> options,Activity context,String id) {
        super(options);
        this.context=context;
        this.id=id;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull UltimaPeleas model) {

        holder.Bind(model);

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_peleas_luchador,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewLuchador,textViewFecha,textViewGanadasOponente,textViewGanadasLuchador,textvieTipoEvento,textViewPeso
                ,textViewDuracion,textViewCategoria,textViewRefetencia,textViewGano;
        ImageButton ButtonDoum;
        CardView carviewPerdida,cardViewCollapse;
        LinearLayout hiddenCollapse;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewLuchador=itemView.findViewById(R.id.textViewLuchador);
            textViewFecha=itemView.findViewById(R.id.textViewFecha);
            textViewGanadasOponente=itemView.findViewById(R.id.textViewGanadasOponente);
            textViewGanadasLuchador=itemView.findViewById(R.id.textViewGanadasLuchador);
            textvieTipoEvento=itemView.findViewById(R.id.textvieTipoEvento);
            textViewPeso=itemView.findViewById(R.id.textViewPeso);
            textViewDuracion=itemView.findViewById(R.id.textViewDuracion);
            textViewCategoria=itemView.findViewById(R.id.textViewCategoria);
            textViewRefetencia=itemView.findViewById(R.id.textViewRefetencia);
            textViewGano=itemView.findViewById(R.id.textViewGano);
            ButtonDoum=itemView.findViewById(R.id.imageButtonDoum);
            carviewPerdida=itemView.findViewById(R.id.carviewPerdida);
            cardViewCollapse=itemView.findViewById(R.id.cardViewCollapse);
            hiddenCollapse=itemView.findViewById(R.id.hiddenCollapse);

        }

        void getName(String id){
            FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String name=documentSnapshot.getString("name");
                        textViewLuchador.setText(name.toUpperCase());
                    }
                }
            });
        }
        void Bind(UltimaPeleas model){
            if (model.getEsempate()){
                textViewGano.setText("EMPATE");
                carviewPerdida.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }else{
                if(model.getIdGanador().equals(id)){
                    // carviewPerdida.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    carviewPerdida.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    //getName(model.getIdPerdedor());
                    textViewGano.setText("GANADA");
                }else{
                    //getName(model.getIdGanador());
                    carviewPerdida.setBackgroundColor(ContextCompat.getColor(context,R.color.red));

                    textViewGano.setText("PERDIDA");
                }
            }
            if(model.getIdGanador().equals(id)){
               // carviewPerdida.setBackgroundColor(ContextCompat.getColor(context,R.color.green));

                getName(model.getIdPerdedor());


            }else{
                getName(model.getIdGanador());



            }
            textViewFecha.setText(model.getFecha().replace("/","-"));
            textViewGanadasLuchador.setText(model.getGanadasLuchador()+" - "+model.getPerdidasLuchador()+" - "+model.getEmpatesLuchador());
            textViewGanadasOponente.setText(model.getGanadasOponente()+" - "+model.getPerdidasOponente()+" - "+model.getEmpatesOponentes());
            textViewPeso.setText(model.getPeso().trim().toUpperCase());
            textViewDuracion.setText(model.getDuration().toUpperCase().trim());
            textViewCategoria.setText(model.getCategoria().toUpperCase().trim());
            textViewRefetencia.setText("Referee: "+model.getReferencia().toUpperCase().trim());
            textvieTipoEvento.setText(model.getTipo());



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hiddenCollapse.getVisibility() == View.VISIBLE) {

                        TransitionManager.beginDelayedTransition(cardViewCollapse,
                                new AutoTransition());
                        hiddenCollapse.setVisibility(View.GONE);
                        ButtonDoum.setImageResource(R.drawable.down);
                    }

                    else {

                        TransitionManager.beginDelayedTransition(cardViewCollapse,
                                new AutoTransition());
                        hiddenCollapse.setVisibility(View.VISIBLE);
                        ButtonDoum.setImageResource(R.drawable.up);
                    }
                }
            });

            ButtonDoum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hiddenCollapse.getVisibility() == View.VISIBLE) {

                        TransitionManager.beginDelayedTransition(cardViewCollapse,
                                new AutoTransition());
                        hiddenCollapse.setVisibility(View.GONE);
                        ButtonDoum.setImageResource(R.drawable.down);
                    }

                    else {

                        TransitionManager.beginDelayedTransition(cardViewCollapse,
                                new AutoTransition());
                        hiddenCollapse.setVisibility(View.VISIBLE);
                        ButtonDoum.setImageResource(R.drawable.up);
                    }
                }
            });

        }
    }
}
