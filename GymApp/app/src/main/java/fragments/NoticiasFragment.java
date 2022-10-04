package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Models.Noticias;
import adapters.AdapterNoticias;


public class NoticiasFragment extends Fragment {
    RecyclerView recicleViewNoticias;
    View view;
    AdapterNoticias adapterNoticias;

    public NoticiasFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_noticias, container, false);
        recicleViewNoticias=view.findViewById(R.id.recicleViewNoticias);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        isAdmin();

    }
    void isAdmin(){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("aprovacion").equals("si")){
                       String permiso=documentSnapshot.getString("permiso");
                        Query query= FirebaseFirestore.getInstance().collection("Noticias").orderBy("timestamp", Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Noticias> options= new FirestoreRecyclerOptions.Builder<Noticias>().setQuery(query,Noticias.class).build();
                        adapterNoticias= new AdapterNoticias(options,getContext(),getActivity(),permiso);
                        recicleViewNoticias.setLayoutManager(new LinearLayoutManager(getContext()));
                        recicleViewNoticias.setAdapter(adapterNoticias);
                        adapterNoticias.startListening();
                    }else{
                        String permiso=documentSnapshot.getString("permiso");
                        Query query= FirebaseFirestore.getInstance().collection("Noticias").orderBy("timestamp", Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Noticias> options= new FirestoreRecyclerOptions.Builder<Noticias>().setQuery(query,Noticias.class).build();
                        adapterNoticias= new AdapterNoticias(options,getContext(),getActivity(),"");
                        recicleViewNoticias.setLayoutManager(new LinearLayoutManager(getContext()));
                        recicleViewNoticias.setAdapter(adapterNoticias);
                        adapterNoticias.startListening();
                    }
                }else{
                    String permiso=documentSnapshot.getString("permiso");
                    Query query= FirebaseFirestore.getInstance().collection("Noticias").orderBy("timestamp", Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Noticias> options= new FirestoreRecyclerOptions.Builder<Noticias>().setQuery(query,Noticias.class).build();
                    adapterNoticias= new AdapterNoticias(options,getContext(),getActivity(),"");
                    recicleViewNoticias.setLayoutManager(new LinearLayoutManager(getContext()));
                    recicleViewNoticias.setAdapter(adapterNoticias);
                    adapterNoticias.startListening();
                }




            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        if ( adapterNoticias!=null){
            adapterNoticias.stopListening();
        }
    }
}