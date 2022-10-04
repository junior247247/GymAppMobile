package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.CrearVersus;
import Models.FinalizarEvento;
import Models.ResultadosPeleas;
import Models.UltimaPeleas;
import app.makingfight.gymapp.R;

public class AdapterCombates extends FirestoreRecyclerAdapter<CrearVersus,AdapterCombates.ViewHolder> {
    Context context;
    private  boolean verifica=false;
    String idGanador="",idPerdedor="";
    FirebaseFirestore firestore;
    boolean isEmpate=false;
    BottomSheetDialog sheetDialog;
    Activity activity;

    public AdapterCombates(@NonNull FirestoreRecyclerOptions<CrearVersus> options,Context context,Activity activity) {
        super(options);
        this.context=context;
        this.firestore=FirebaseFirestore.getInstance();
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CrearVersus model) {
        holder.BindOne(model);
        holder.BindTwo(model);
        holder.textViewCategoria.setText(model.getCategoria());
        holder.editextCatagoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showBottonChetDialog();
            }
        });
        holder.textViewVersus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.view1.setVisibility(View.GONE);
                holder.view2.setVisibility(View.GONE);
           //     holder.viewEmpates.setVisibility(View.VISIBLE);
                if (holder.viewEmpates.getVisibility()==View.GONE){
                    isEmpate=true;
                    verifica=true;
                    idGanador= model.getIdLuchador1();
                    idPerdedor=model.getIdLuchador2();
                    holder.viewEmpates.setVisibility(View.VISIBLE);
                }else{
                    verifica=false;
                    isEmpate=false;
                    holder.viewEmpates.setVisibility(View.GONE);
                }

            }
        });
        holder.buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifica){
                    if ( !holder.editextDecision.getText().toString().trim().isEmpty()  &&  !holder.editextCatagoria.getText().equals("CATEGORIA") && !holder.editextDuration.getText().toString().trim().isEmpty() && !holder.editextTipo.getText().toString().trim().isEmpty() && !holder.editextReferencia.getText().toString().trim().isEmpty()){

                        firestore.collection("Combates").whereEqualTo("is",false).whereEqualTo("idLuchador1",model.getIdLuchador1()).whereEqualTo("idLuchador2",model.getIdLuchador2()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size()>0){
                                    String id=queryDocumentSnapshots.getDocuments().get(0).getId();
                                    Map<String,Object> map= new HashMap<>();
                                    map.put("is",true);
                                    holder.UltimaPeleas(model);
                                    firestore.collection("Combates").document(id).update(map);

                                    DocumentReference documentReference =firestore.collection("ResultadosPeleas").document();
                                    ResultadosPeleas resultadosPeleas= new ResultadosPeleas();
                                    resultadosPeleas.setId(documentReference.getId());
                                    resultadosPeleas.setIdLuchador1(model.getIdLuchador1());
                                    resultadosPeleas.setIdLuchador2(model.getIdLuchador2());
                                    resultadosPeleas.setIdEvento(model.getIdEvento());
                                    resultadosPeleas.setCategoria(model.getCategoria());
                                    resultadosPeleas.setIdGanador(idGanador);
                                    resultadosPeleas.setIsView(false);
                                    resultadosPeleas.setDecicion(holder.editextDecision.getText().toString());
                                    resultadosPeleas.setEsEmpate(isEmpate);
                                    resultadosPeleas.setIdPerdedor(idPerdedor);
                                    resultadosPeleas.setTimestamp(new Date().getTime());
                                    resultadosPeleas.setEntro(false);
                                    Map<String,Object>map1= new HashMap<>();

                                    firestore.collection("ResultadosPeleas").document(resultadosPeleas.getId()).set(resultadosPeleas);
                                    if (getItemCount()==0){
                                        holder.finishEvent(model.getIdEvento());
                                    }
                                   /* if (isEmpate){
                                        ArrayList<String> list= new ArrayList();
                                        list.add(model.getIdLuchador1());
                                        list.add(model.getIdLuchador2());
                                        for ( int i=0; i<list.size();i++){
                                            holder.updateEmpates(list.get(i));
                                        }
                                    }else{
                                        holder.updateGanadas(idGanador);
                                        holder.updatePerdida(idPerdedor);
                                    }

                                    */


                                }

                            }
                        });
                    }else{
                        Toast.makeText(context, "Ingresa la description", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Debes selecionar el luchador ganador", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_combates,parent,false));
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCategoria,textViewNombrevs1,textViewAliarVs1,textViewGanadaPerdidasVs1;
        TextView textViewNombrevs2,textViewAliasVs2,textViewGanadasVs2,editextCatagoria,textViewVersus;
        ImageView imageViewVs1,imavieLuchadorvs2;
        LinearLayout linearlayout1,linearlayout2;
        EditText editextReferencia,editextDuration,editextTipo,editextDecision;
        LinearLayout libearkyout;
        Button buttonGuardar;
        View view1,view2,viewEmpates;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoria=itemView.findViewById(R.id.textViewCategoria);
            textViewNombrevs1=itemView.findViewById(R.id.textViewNombrevs1);
            textViewAliarVs1=itemView.findViewById(R.id.textViewAliarVs1);
            textViewGanadaPerdidasVs1=itemView.findViewById(R.id.textViewGanadaPerdidasVs1);
            libearkyout=itemView.findViewById(R.id.libearkyout);
            textViewNombrevs2=itemView.findViewById(R.id.textViewNombrevs2);
            textViewAliasVs2=itemView.findViewById(R.id.textViewAliasVs2);
            textViewGanadasVs2=itemView.findViewById(R.id.textViewGanadasVs2);
            linearlayout2=itemView.findViewById(R.id.linearlayout2);
            linearlayout1=itemView.findViewById(R.id.linearlayout1);
            imavieLuchadorvs2=itemView.findViewById(R.id.imavieLuchadorvs2);
            imageViewVs1=itemView.findViewById(R.id.imageViewVs1);
            editextReferencia=itemView.findViewById(R.id.editextReferencia);
            editextDuration=itemView.findViewById(R.id.editextDuration);
            editextTipo=itemView.findViewById(R.id.editextTipo);
            editextCatagoria=itemView.findViewById(R.id.editextCatagoria);
            editextDecision=itemView.findViewById(R.id.editextDecision);
            view1=itemView.findViewById(R.id.view1);
            view2=itemView.findViewById(R.id.view2);
            viewEmpates=itemView.findViewById(R.id.viewEmpates);
            textViewVersus=itemView.findViewById(R.id.textViewVersus);
            buttonGuardar=itemView.findViewById(R.id.buttonGuardar);


        }

        void  showBottonChetDialog(){
            ArrayList<String> list= new ArrayList(){{add("AMATEUR");add("PRO");}};
            sheetDialog= new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
            View view=LayoutInflater.from(context).inflate(R.layout.layout_button_shet_dialog,null);
            RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
            sheetDialog.setContentView(view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
                @Override
                public void itemClick(String dato) {
                    editextCatagoria.setText(dato);
                    sheetDialog.dismiss();
                }
            }));
            sheetDialog.show();
        }

        void UltimaPeleas(CrearVersus modelVersus){

            firestore.collection("Luchadores").document(modelVersus.getIdLuchador1()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String [] datosGanador= new String[3];
                        datosGanador[0]=documentSnapshot.getString("ganadas");
                        datosGanador[1]=documentSnapshot.getString("perdidas");
                        datosGanador[2]=documentSnapshot.getString("empates");
                        String name1=documentSnapshot.getString("name");
                        firestore.collection("Luchadores").document(modelVersus.getIdLuchador2()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){

                                    String [] datosOponentes= new String[3];


                                    datosOponentes[0]=documentSnapshot.getString("ganadas");
                                    datosOponentes[1]=documentSnapshot.getString("perdidas");
                                    datosOponentes[2]=documentSnapshot.getString("empates");

                                    String name2=documentSnapshot.getString("name");


                                    firestore.collection("Eventos").document(modelVersus.getIdEvento()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                String fecha=documentSnapshot.getString("fecha");
                                                UltimaPeleas model= new UltimaPeleas();
                                                DocumentReference reference=firestore.collection("UltimaPeleas").document();
                                                model.setId(reference.getId());
                                                model.setGanadasLuchador(datosGanador[0]);
                                                model.setPerdidasLuchador(datosGanador[1]);
                                                model.setEmpatesLuchador(datosGanador[2]);
                                                ArrayList<String> ids= new ArrayList<>();
                                                ids.add(modelVersus.getIdLuchador1());
                                                ids.add(modelVersus.getIdLuchador2());
                                                model.setIds(ids);
                                                model.setGanadasOponente(datosOponentes[0]);
                                                model.setPerdidasOponente(datosOponentes[1]);
                                                model.setEmpatesOponentes(datosOponentes[2]);
                                                model.setIdPerdedor(idPerdedor);
                                                model.setEsempate(isEmpate);
                                                model.setNameLuchador(name2.toUpperCase());
                                                model.setNameOponente(name1.toUpperCase());
                                                model.setDuration(editextDuration.getText().toString());
                                                model.setTipo(editextTipo.getText().toString());
                                                model.setPeso(textViewCategoria.getText().toString());
                                                model.setGanoCombate("");
                                                model.setFecha(fecha);
                                                model.setIdGanador(idGanador);
                                                model.setReferencia(editextReferencia.getText().toString());
                                                model.setTimestamp(new Date().getTime());
                                                model.setCategoria(editextCatagoria.getText().toString());

                                                firestore.collection("UltimaPeleas").document().set(model);
                                                if (isEmpate){
                                                    updateEmpates(modelVersus.getIdLuchador2());
                                                    updateEmpates(modelVersus.getIdLuchador1());
                                                }else{
                                                    updateGanadas(idGanador);
                                                    updatePerdida(idPerdedor);
                                                }

                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            });


        }

        void finishEvent(String id){
            firestore.collection("Eventos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        DocumentReference reference=firestore.collection("EventoFinalizado").document();
                        FinalizarEvento model= new FinalizarEvento();
                        model.setId(reference.getId());
                        model.setIdEvento(id);
                        model.setLugar(documentSnapshot.getString("lugar"));
                        model.setFecha(documentSnapshot.getString("fecha"));
                        model.setTimestamp(new Date().getTime());
                        firestore.collection("EventoFinalizado").document(model.getId()).set(model);
                        Map<String,Object> map= new HashMap<>();
                        map.put("emision","FINALIZADO");
                        firestore.collection("Eventos").document(model.getIdEvento()).update(map);
                        activity.finish();
                    }

                }
            });

        }
        void updatePerdida(String id){

            firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String perdida=documentSnapshot.getString("perdidas");
                        int perdidas=Integer.parseInt(perdida);
                        Map<String,Object> map= new HashMap<>();
                        perdidas+=1;
                        map.put("perdidas",String.valueOf(perdidas));
                        firestore.collection("Luchadores").document(id).update(map);

                    }
                }
            });
        }

        void updateEmpates(String id){
            firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String empates=documentSnapshot.getString("empates");
                        int emp=Integer.parseInt(empates);
                        emp+=1;
                        Map<String,Object> map= new HashMap<>();
                        map.put("empates",String.valueOf(emp));
                        firestore.collection("Luchadores").document(id).update(map);
                    }
                }
            });
        }
        void updateGanadas(String id){
            firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String ganadas=documentSnapshot.getString("ganadas");
                        int ganada=Integer.parseInt(ganadas);
                        ganada+=1;
                        Map<String,Object> map= new HashMap<>();
                        map.put("ganadas",String.valueOf(ganada));
                        firestore.collection("Luchadores").document(id).update(map);

                    }
                }
            });
        }

        void BindOne(CrearVersus model){
            firestore.collection("Luchadores").document(model.getIdLuchador1()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        String dato="";
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");

                        if (ganadas!=null){
                            dato=ganadas;
                        }else{
                            dato="0";
                        }
                        if (perdidas!=null){
                            dato=dato+" - "+perdidas;
                        }else{
                            dato=dato+" - "+"0";
                        }
                        if (empates!=null){
                            dato=dato+" - "+empates;
                        }else{
                            dato=dato+" - "+"0";
                        }
                        Picasso.with(context).load(documentSnapshot.getString("urlImage")).fit().placeholder(R.drawable.spiner).into(imageViewVs1);
                        textViewGanadaPerdidasVs1.setText(dato);
                        String name=documentSnapshot.getString("name").toUpperCase();
                        if (name.length()>14){
                            textViewNombrevs1.setText(name.toUpperCase().substring(0,12)+"...");
                        }else{
                            textViewNombrevs1.setText(name.toUpperCase());
                        }
                        textViewAliarVs1.setText(documentSnapshot.getString("alias").toUpperCase());
                    }
                }
            });

                linearlayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifica=true;
                        idGanador= model.getIdLuchador1();
                        idPerdedor=model.getIdLuchador2();
                        isEmpate=false;
                        viewEmpates.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);
                        view1.setBackgroundColor(Color.GREEN);
                        view2.setVisibility(View.VISIBLE);
                        view2.setBackgroundColor(Color.RED);
                    }
                });



        }



        void BindTwo(CrearVersus model){
            firestore.collection("Luchadores").document(model.getIdLuchador2()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( documentSnapshot.exists()){
                        String dato="";
                        String ganadas=documentSnapshot.getString("ganadas");
                        String perdidas=documentSnapshot.getString("perdidas");
                        String empates=documentSnapshot.getString("empates");
                    //    textViewCategoria.setText(documentSnapshot.getString("categoria").toUpperCase());
                        if (ganadas!=null){
                            dato=ganadas;
                        }else{
                            dato="0";
                        }
                        if (perdidas!=null){
                            dato=dato+" - "+perdidas;
                        }else{
                            dato=dato+" - "+"0";
                        }
                        if (empates!=null){
                            dato=dato+" - "+empates;
                        }else{
                            dato=dato+" - "+"0";
                        }
                        Picasso.with(context).load(documentSnapshot.getString("urlImage")).fit().placeholder(R.drawable.spiner).into(imavieLuchadorvs2);

                        textViewGanadasVs2.setText(dato);
                        String name=documentSnapshot.getString("name").toUpperCase();
                        if (name.length()>14){
                            textViewNombrevs2.setText(name.toUpperCase().substring(0,12)+"...");
                        }else{
                            textViewNombrevs2.setText(name.toUpperCase());
                        }
                        textViewNombrevs2.setText(documentSnapshot.getString("name").toUpperCase());
                        textViewAliasVs2.setText(documentSnapshot.getString("alias").toUpperCase());
                    }
                }
            });

            if (!isEmpate){
                linearlayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifica=true;
                        isEmpate=false;
                        idGanador= model.getIdLuchador2();
                        idPerdedor=model.getIdLuchador1();
                        view2.setVisibility(View.VISIBLE);
                        viewEmpates.setVisibility(View.GONE);
                        view2.setBackgroundColor(Color.GREEN);
                        view1.setVisibility(View.VISIBLE);
                        view1.setBackgroundColor(Color.RED);
                    }
                });
            }

        }


    }
}
