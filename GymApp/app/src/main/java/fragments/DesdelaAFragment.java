package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import Models.Luchador;
import adapters.AtletasAdapter;


public class DesdelaAFragment extends Fragment {
        ArrayList<String> list;
        View view;
        RecyclerView recicleViewAtletas;
        AtletasAdapter atletasAdapter;
        TextInputEditText textInputBuscar;
            ImageView buttonSearch;
            String permiso="";


    public DesdelaAFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_desdela_a, container, false);
        recicleViewAtletas=view.findViewById(R.id.recicleViewAtletas);
        textInputBuscar=view.findViewById(R.id.textInputBuscar);

        textInputBuscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    switch (i){
                        case KeyEvent.KEYCODE_ENTER:
                            if (!textInputBuscar.getText().toString().isEmpty()){
                                Search(textInputBuscar.getText().toString().toLowerCase().trim());


                            }
                            return true;
                    }
                }
                return false;
            }
        });
        recicleViewAtletas.setLayoutManager(new LinearLayoutManager(getContext()));
        buttonSearch=view.findViewById(R.id.buttonbuscar);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textInputBuscar.getText().toString().isEmpty()){
                    Search(textInputBuscar.getText().toString().toLowerCase().trim());
                }
            }
        });



        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
       isAdmin();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (atletasAdapter!=null){
            atletasAdapter.stopListening();
        }
    }

    void Search(String name){
        Query query=FirebaseFirestore.getInstance().collection("Luchadores").orderBy("name").startAt(name).endAt(name+'\uf8ff');
        FirestoreRecyclerOptions<Luchador> options= new FirestoreRecyclerOptions.Builder<Luchador>().setQuery(query,Luchador.class).build();
        atletasAdapter= new AtletasAdapter(options,getContext(),getActivity(),permiso);
        recicleViewAtletas.setLayoutManager(new LinearLayoutManager(getContext()));
        recicleViewAtletas.setAdapter(atletasAdapter);
        atletasAdapter.startListening();
    }


    void isAdmin(){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("aprovacion").equals("si")){
                        permiso=documentSnapshot.getString("permiso");
                        Query query= FirebaseFirestore.getInstance().collection("Luchadores").orderBy("timestamp", Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Luchador> options=new FirestoreRecyclerOptions.Builder<Luchador>()
                                .setQuery(query,Luchador.class)
                                .build();
                        atletasAdapter= new AtletasAdapter(options,getContext(),getActivity(),permiso);
                        recicleViewAtletas.setAdapter(atletasAdapter);
                        atletasAdapter.startListening();
                    }else{
                        Query query= FirebaseFirestore.getInstance().collection("Luchadores").orderBy("timestamp", Query.Direction.DESCENDING);;
                        FirestoreRecyclerOptions<Luchador> options=new FirestoreRecyclerOptions.Builder<Luchador>()
                                .setQuery(query,Luchador.class)
                                .build();
                        atletasAdapter= new AtletasAdapter(options,getContext(),getActivity(),"");
                        recicleViewAtletas.setAdapter(atletasAdapter);
                        atletasAdapter.startListening();

                    }
                }else{
                    Query query= FirebaseFirestore.getInstance().collection("Luchadores").orderBy("timestamp", Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Luchador> options=new FirestoreRecyclerOptions.Builder<Luchador>()
                            .setQuery(query,Luchador.class)
                            .build();
                    atletasAdapter= new AtletasAdapter(options,getContext(),getActivity(),"");
                    recicleViewAtletas.setAdapter(atletasAdapter);
                    atletasAdapter.startListening();
                }
            }
        });

    }


}