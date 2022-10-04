package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Models.Ranking;
import adapters.AdapterCategorias;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import adapters.AdapterMejores;


public class RankingFragment extends Fragment {

    RecyclerView recicleViewRanking;
    View view;
    AdapterMejores adapterMejores;
    AdapterMejores adapterMejoresBuscar;
    LinearLayout linearLayoutCategorias;
    RecyclerView recyclerViewCategorias;
    TextView textViewCategoria;
    ImageView imageViewShowBottom,buttonbuscar;
    TextInputEditText textInputBuscar;
    BottomSheetDialog bottomSheetDialog;
    public RankingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_ranking, container, false);
        recicleViewRanking=view.findViewById(R.id.recicleViewRanking);
        imageViewShowBottom=view.findViewById(R.id.imageViewShowBottom);
        buttonbuscar=view.findViewById(R.id.buttonbuscars);
        textInputBuscar=view.findViewById(R.id.textInputBuscars);
        textViewCategoria=view.findViewById(R.id.textViewCategoria);
        imageViewShowBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBotto();
            }
        });
        linearLayoutCategorias=view.findViewById(R.id.linearLayoutCategorias);
        linearLayoutCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBotto();
            }
        });
        buttonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textInputBuscar.getText().toString().isEmpty()){
                    if (!textViewCategoria.getText().equals("TODOS")){
                        SearchByRegionWithName(textViewCategoria.getText().toString(),textInputBuscar.getText().toString().toUpperCase());
                    }



                }
            }
        });
        /*

        textInputBuscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    switch (i){
                        case KeyEvent.KEYCODE_ENTER:
                            if (!textInputBuscar.getText().toString().isEmpty()){
                                if (!textViewCategoria.getText().equals("TODOS")){
                                    SearchByRegionWithName(textViewCategoria.getText().toString(),textInputBuscar.getText().toString().toUpperCase());
                                }



                            }
                            return true;
                    }
                }
                return true;
            }
        });

         */


        return view;
    }













    void SearchByRegionWithName(String tipo,String name){
        Query query=FirebaseFirestore.getInstance().collection("Ranking").whereEqualTo("region",tipo).orderBy("name").startAt(name).endAt(name+'\uf8ff');
        FirestoreRecyclerOptions<Ranking> options= new FirestoreRecyclerOptions.Builder<Ranking>().setQuery(query,Ranking.class).build();
        adapterMejoresBuscar= new AdapterMejores(options,getContext(),getActivity());
        recicleViewRanking.setLayoutManager(new LinearLayoutManager(getContext()));
        recicleViewRanking.setAdapter(adapterMejoresBuscar);
        adapterMejoresBuscar.startListening();
    }



    void showBotto(){
    View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_button_shet_dialog,null);
    bottomSheetDialog= new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
    bottomSheetDialog.setContentView(view);

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


    recyclerViewCategorias=view.findViewById(R.id.recicleViewCategorias);
    recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerViewCategorias.setAdapter(new AdapterCategorias(list, new AdapterCategorias.clickListener() {
        @Override
        public void itemClick(String dato) {
            textViewCategoria.setText(dato);
            bottomSheetDialog.dismiss();

                Query query=FirebaseFirestore.getInstance().collection("Ranking").whereEqualTo("region",textViewCategoria.getText()).orderBy("posiciontion", Query.Direction.ASCENDING);

                FirestoreRecyclerOptions<Ranking> options= new FirestoreRecyclerOptions.Builder<Ranking>()
                        .setQuery(query,Ranking.class).build();
                adapterMejores= new AdapterMejores(options,getContext(),getActivity());
                recicleViewRanking.setLayoutManager(new LinearLayoutManager(getContext()));
                recicleViewRanking.setAdapter(adapterMejores);

                    adapterMejores.startListening();






        }
    }));
    bottomSheetDialog.show();
    }



    @Override
    public void onStart() {
        super.onStart();
        Query query=FirebaseFirestore.getInstance().collection("Ranking").whereEqualTo("region",textViewCategoria.getText()).orderBy("posiciontion", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Ranking> options= new FirestoreRecyclerOptions.Builder<Ranking>()
                .setQuery(query,Ranking.class).build();
        adapterMejores= new AdapterMejores(options,getContext(),getActivity());
        recicleViewRanking.setLayoutManager(new LinearLayoutManager(getContext()));
        recicleViewRanking.setAdapter(adapterMejores);
        adapterMejores.startListening();



    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapterMejores!=null){
            adapterMejores.stopListening();
        }

    }


}