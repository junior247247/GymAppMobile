package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapters.AdapterResultadosNuevos;
import app.makingfight.gymapp.EventoYfotos;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ResultadosFragment extends Fragment {
    View rootView;
    RecyclerView recyclerViewNoticias;
    AdapterResultadosNuevos adapterFotos;
    public ResultadosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView=inflater.inflate(R.layout.fragment_resultados, container, false);
        recyclerViewNoticias=rootView.findViewById(R.id.recicleViewNoticias);
        recyclerViewNoticias.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        Query query=FirebaseFirestore.getInstance().collection("EventoYfotos").orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<EventoYfotos> options= new FirestoreRecyclerOptions.Builder<EventoYfotos>()
                .setQuery(query,EventoYfotos.class)
                .build();
        adapterFotos= new AdapterResultadosNuevos(options,getContext());
        recyclerViewNoticias.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNoticias.setAdapter(adapterFotos);
        adapterFotos.startListening();




    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterFotos!=null){
            adapterFotos.stopListening();
        }

    }
}