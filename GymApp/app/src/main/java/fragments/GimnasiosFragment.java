package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Models.Gym;
import adapters.AdapterGymnasios;
import app.makingfight.gymapp.R;


public class GimnasiosFragment extends Fragment {

    RecyclerView recicleViewGimasios;
    AdapterGymnasios adapterGymnasios;
    View view;
    public GimnasiosFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_gimnasios, container, false);
        recicleViewGimasios=view.findViewById(R.id.recicleViewGimasios);
        recicleViewGimasios.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       Query query= FirebaseFirestore.getInstance().collection("Gyms").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Gym> options= new FirestoreRecyclerOptions.Builder<Gym>().setQuery(query,Gym.class).build();
        adapterGymnasios= new AdapterGymnasios(options,getContext(),getActivity());
        recicleViewGimasios.setAdapter(adapterGymnasios);
        adapterGymnasios.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterGymnasios!=null){
            adapterGymnasios.stopListening();
        }
    }
}