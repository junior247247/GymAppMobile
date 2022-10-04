package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import Models.Ranking;
import adapters.AdapterListaRegiones;
import app.makingfight.gymapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Losmejores;
import adapters.AdapterCategorias;

public class RankingActivity extends AppCompatActivity implements AdapterListaRegiones.ClickListener {
    LinearLayout linearLayoutRanking;
    TextView textViewRanking,textViewName;
    BottomSheetDialog bottomSheetDialog;
    Switch checkEscampeon;
    Button buttonGuardar,buttonEliminar;
    public static  String mejor="";
    String tipo="";
   public static ArrayList<String> mejorList;
    ArrayList<String>ranking;
    Toolbar toolbar;
    public  static  String regionSelect="";
    int position=0;
    FirebaseFirestore firestore;
    RecyclerView recycleViewRegiones;
    AdapterListaRegiones adapterListaRegiones;
    int currentSize;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_ranking);
        Bind();
        ranking=new ArrayList<>();
        mejorList=new ArrayList<>();

        linearLayoutRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showRanking();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    getName(getIntent().getStringExtra("id"));
    getData(getIntent().getStringExtra("id"));
        isChampion(getIntent().getStringExtra("id"));
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mejor.isEmpty()){
                   Update(getIntent().getStringExtra("id"));
                   if (ranking.size()>0){
                       ranking.clear();
                   }

                }


                    setCheckEscampeon(getIntent().getStringExtra("id"));



                    finish();

            }
        });



        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RankingActivity.this)
                        .setMessage("")
                        .setTitle("¿REALMENTE DECEAS ELIMINAR DE LA LISTA DE RANKING?")
                        .setNegativeButton("NO",null)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).delete();
                                firestore.collection("Ranking").whereEqualTo("idLuchador",getIntent().getStringExtra("id")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.size()>0){
                                            for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                                                String id=snapshot.getId();
                                                firestore.collection("Ranking").document(id).delete();
                                            }
                                        }

                                    }
                                });
                                showToast("ELIMINADO");
                                finish();
                            }
                        })
                        .show();

            }
        });
    }
    void getName(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    textViewName.setText(name.toUpperCase().trim());

                }
            }
        });
    }

    void getData(String id){
        firestore.collection("Losmejores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    ArrayList<String> name =(ArrayList<String>) documentSnapshot.get("esmejor");
                    String data="";
                    for (int i=0; i<name.size();i++){
                        data=data+name.get(i)+"-";
                        mejorList.add(name.get(i));

                    }
                textViewRanking.setText(data);
                    recycleViewRegiones=findViewById(R.id.recycleViewRegiones);
                    recycleViewRegiones.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                    adapterListaRegiones= new AdapterListaRegiones(name,RankingActivity.this);
                    recycleViewRegiones.setAdapter(adapterListaRegiones);
                    currentSize=name.size();


                }
            }
        });
    }

        void createRanking(String id,long position,ArrayList<String> list){

                        for (int i=0;i<list.size();i++){
                            Ranking ranking= new Ranking();
                            ranking.setIdLuchador(id);
                            ranking.setRegion(list.get(i));

                            ranking.setName(textViewName.getText().toString());
                            ranking.setTimestamp(new Date().getTime());
                            firestore.collection("Ranking").whereEqualTo("region",list.get(i)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.size()>0){
                                        ranking.setPosiciontion(queryDocumentSnapshots.size()+1);
                                        firestore.collection("Ranking").document().set(ranking);
                                    }else{
                                        ranking.setPosiciontion(1);
                                        firestore.collection("Ranking").document().set(ranking);
                                    }
                                }
                            });



                        }

        }

    void Update(String id){

        firestore.collection("Losmejores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){


                    boolean isCreate=false;
                    ArrayList<String> datos=(ArrayList<String>) documentSnapshot.get("esmejor");

                    if (currentSize<datos.size()){
                        isCreate=true;

                    }else{
                        if (mejorList.size()>datos.size()){
                            for (int i=0;i<mejorList.size();i++){
                                for (int j=0; j<datos.size();j++){
                                    if (!mejorList.get(i).equals(datos.get(j))){
                                        isCreate=true;
                                    }
                                }
                            }
                        }

                    }


                        if(isCreate){
                            firestore.collection("Losmejores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                long position=documentSnapshot.getLong("posicion");
                                String dato=textViewRanking.getText().toString();
                                Losmejores losmejores= new Losmejores();
                                DocumentReference reference=firestore.collection("Losmejores").document();
                                losmejores.setId(reference.getId());
                                losmejores.setTimestamp(new Date().getTime());
                                losmejores.setEsmejor(mejorList);
                                losmejores.setTipo(tipo);
                                losmejores.setNombre(textViewName.getText().toString());
                                losmejores.setIdLuchador(getIntent().getStringExtra("id"));
                                Map<String,Object>map= new HashMap<>();
                                map.put("esmejor",mejorList);



                                map.put("tipo",tipo);
                                map.put("nombre",losmejores.getNombre());
                                firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).update(map);

                                createRanking(getIntent().getStringExtra("id"),position,mejorList);
                                showToast("DATOS ACTUALIZADO");




                            }else{

                                createRankingWithPosition();
                            }
                        }
                    });


                        }
                }else{

                    firestore.collection("Losmejores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                long position=documentSnapshot.getLong("posicion");
                                String dato=textViewRanking.getText().toString();
                                Losmejores losmejores= new Losmejores();
                                DocumentReference reference=firestore.collection("Losmejores").document();
                                losmejores.setId(reference.getId());
                                losmejores.setTimestamp(new Date().getTime());
                                losmejores.setEsmejor(mejorList);
                                losmejores.setTipo(tipo);
                                losmejores.setNombre(textViewName.getText().toString());
                                losmejores.setIdLuchador(getIntent().getStringExtra("id"));
                                Map<String,Object>map= new HashMap<>();
                                map.put("esmejor",mejorList);



                                map.put("tipo",tipo);
                                map.put("nombre",losmejores.getNombre());
                                firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).update(map);

                                createRanking(getIntent().getStringExtra("id"),position,mejorList);
                                showToast("DATOS ACTUALIZADO");




                            }else{

                                createRankingWithPosition();
                            }
                        }
                    });

                }
            }
        });



    }
    void DeleteRegion(String region){
        firestore.collection("Ranking").whereEqualTo("idLuchador",getIntent().getStringExtra("id")).whereEqualTo("region",region).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    String  id=queryDocumentSnapshots.getDocuments().get(0).getId();
                    firestore.collection("Ranking").document(id).delete();
                    Losmejores losmejores= new Losmejores();
                    DocumentReference reference=firestore.collection("Losmejores").document();
                    losmejores.setId(reference.getId());
                    losmejores.setTimestamp(new Date().getTime());
                    losmejores.setEsmejor(mejorList);
                    losmejores.setPosicion(position);
                    losmejores.setTipo(tipo);
                    losmejores.setNombre(textViewName.getText().toString());
                    losmejores.setIdLuchador(getIntent().getStringExtra("id"));
                    firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).set(losmejores);
                    createRanking(getIntent().getStringExtra("id"),position,mejorList);

                }
            }
        });
    }

    void createRankingWithPosition(){

            firestore.collection("Losmejores").whereArrayContainsAny("esmejor",mejorList).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0) {
                        position=queryDocumentSnapshots.size();
                        String dato=textViewRanking.getText().toString();
                        Losmejores losmejores= new Losmejores();
                        DocumentReference reference=firestore.collection("Losmejores").document();
                        losmejores.setId(reference.getId());
                        losmejores.setTimestamp(new Date().getTime());
                        losmejores.setEsmejor(mejorList);
                        losmejores.setPosicion(position);
                        losmejores.setTipo(tipo);
                        losmejores.setNombre(textViewName.getText().toString());
                        losmejores.setIdLuchador(getIntent().getStringExtra("id"));
                        firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).set(losmejores);
                        createRanking(getIntent().getStringExtra("id"),position,mejorList);
                        showToast("DATOS ACTUALIZADO");
                    }else{

                        position=queryDocumentSnapshots.size();
                        String dato=textViewRanking.getText().toString();
                        Losmejores losmejores= new Losmejores();
                        DocumentReference reference=firestore.collection("Losmejores").document();
                        losmejores.setId(reference.getId());
                        losmejores.setTimestamp(new Date().getTime());
                        losmejores.setEsmejor(mejorList);
                        losmejores.setPosicion(1);
                        losmejores.setTipo(tipo);
                        losmejores.setNombre(textViewName.getText().toString());
                        losmejores.setIdLuchador(getIntent().getStringExtra("id"));
                        firestore.collection("Losmejores").document(getIntent().getStringExtra("id")).set(losmejores);
                        createRanking(getIntent().getStringExtra("id"),1,mejorList);
                        showToast("DATOS ACTUALIZADO");

                    }
                }
            });


    }
    void showToast(String msj){
        Toast toast=Toast.makeText(this,msj,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    void isChampion(String id){
        firestore.collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            if (documentSnapshot.exists()){
                String divicion=documentSnapshot.getString("divicion");
                if (divicion.equals("CAMPEON")){
                    checkEscampeon.setChecked(true);
                }
            }
             }
        });

    }


    void setCheckEscampeon(String id){
        Map<String,Object> map= new HashMap<>();
        if (checkEscampeon.isChecked()){
            map.put("divicion","CAMPEON");
            firestore.collection("Luchadores").document(id).update(map);
        }else{
            map.put("divicion","AMATEUR");
            firestore.collection("Luchadores").document(id).update(map);
        }

    }
    void showRanking(){

                    View view= LayoutInflater.from(RankingActivity.this).inflate(R.layout.layout_button_shet_dialog,null);
                    RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
                    ArrayList<String> list= new ArrayList();
                    list.add("GALICIA");
                    list.add("ANDALUCIA");
                    list.add("CASTILLA - LA MANCHA");
                    list.add("MADRID");
                    list.add("LA RIOJA");
                    list.add("MURCIA");
                    list.add("COM VALENCIANA");
                    list.add("ARAGON");
                    list.add("CATALUÑA");
                    list.add("LA RIOJA");
                    list.add("NAVARRA");
                    list.add("P . VASCO");
                    list.add("CANTABRIA");
                    list.add("P . DE ASTURIAS");
                    list.add("CASTILLA Y LEON");
                    list.add("EXTREMADURA");
                    list.add("CEUTA");
                    list.add("MELILLA");
                    list.add("EL MUNDO");
                    recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                    recyclerView.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
                        @Override
                        public void itemClick(String dato) {
                            mejor="";
                            mejorList.add(dato);

                            for (int i=0;i<mejorList.size();i++){

                                    mejor=mejor+mejorList.get(i)+" -";
                                tipo="REGIONAL";

                            }

                            if (dato.equals("EL MUNDO")){
                                mejorList.clear();
                                mejorList.add(0,dato);
                                mejor=dato;
                                tipo="MUNDIAL";
                            }
                            ranking.add(0,dato);
                            textViewRanking.setText(mejor);
                            bottomSheetDialog.dismiss();

                            recycleViewRegiones=findViewById(R.id.recycleViewRegiones);
                            recycleViewRegiones.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                            adapterListaRegiones= new AdapterListaRegiones(mejorList,RankingActivity.this);
                            recycleViewRegiones.setAdapter(adapterListaRegiones);
                            adapterListaRegiones.notifyDataSetChanged();
                        }
                    }));
                    bottomSheetDialog=new BottomSheetDialog(RankingActivity.this,R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void Bind(){
        textViewName=findViewById(R.id.textViewName);
        firestore=FirebaseFirestore.getInstance();
        buttonEliminar=findViewById(R.id.buttonEliminar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ACTUALIZAR RANKING");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkEscampeon=findViewById(R.id.checkEscampeon);
        linearLayoutRanking=findViewById(R.id.linearLayoutRanking);
        textViewRanking=findViewById(R.id.textViewRanking);
        buttonGuardar=findViewById(R.id.buttonGuardar);
    }


    @Override
    public void onItemClick(int position) {
        mejor=mejorList.get(position);
        DeleteRegion(mejor);
        mejorList.remove(position);
        adapterListaRegiones.notifyItemChanged(position);
        if (mejorList!=null){
            if (mejorList.size()>0){
                mejor="";
                for (int i=0;i<mejorList.size();i++){

                    mejor=mejor+mejorList.get(i)+" -";
                    tipo="REGIONAL";

                }
                textViewRanking.setText(mejor);
            }else{
                textViewRanking.setText("");
            }

        }else{
            textViewRanking.setText("");
        }

    }
}