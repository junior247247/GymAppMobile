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

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import Models.Luchador;
import adapters.AdapterCategorias;
import adapters.AtletasAdapter;


public class ClasificacionesFragment extends Fragment {
        ImageView imageViewShowBottom;
        AdapterCategorias adapterCategorias;
        BottomSheetDialog bottomSheetDialog;
        TextView textViewCategoria;
    AtletasAdapter atletasAdapter;
        View view;
        RecyclerView recicleViewAtletas;
        String permiso="";
        LinearLayout linearLayoutCategorias;


    public ClasificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            view=inflater.inflate(R.layout.fragment_clasificaciones, container, false);
        linearLayoutCategorias=view.findViewById(R.id.linearLayoutCategorias);
            recicleViewAtletas=view.findViewById(R.id.recicleViewAtletas);
            textViewCategoria=view.findViewById(R.id.textViewCategoria);
            imageViewShowBottom=view.findViewById(R.id.imageViewShowBottom);
            recicleViewAtletas.setLayoutManager(new LinearLayoutManager(getContext()));

        linearLayoutCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowButtonSheetDialod();
            }
        });
            imageViewShowBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowButtonSheetDialod();
                }
            });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        isAdmin();
        //permiso=documentSnapshot.getString("permiso");

    }

    void Search(String categoria){
        if (categoria.equals("TODOS LOS PESO")){
            isAdmin();
        }else{
            Query query=FirebaseFirestore.getInstance().collection("Luchadores").orderBy("categoria").startAt(categoria).endAt(categoria+'\uf8ff');
            FirestoreRecyclerOptions<Luchador> options= new FirestoreRecyclerOptions.Builder<Luchador>().setQuery(query,Luchador.class).build();
            atletasAdapter= new AtletasAdapter(options,getContext(),getActivity(),permiso);
            recicleViewAtletas.setLayoutManager(new LinearLayoutManager(getContext()));
            recicleViewAtletas.setAdapter(atletasAdapter);
            atletasAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (atletasAdapter!=null){
            atletasAdapter.stopListening();
        }
    }

    void ShowButtonSheetDialod(){
        bottomSheetDialog= new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
        View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_button_shet_dialog,null);
        RecyclerView recyclerView=view.findViewById(R.id.recicleViewCategorias);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        ArrayList<String> list= new ArrayList(){{
            add("PESO PAJA");
            add("PESO MOSCA");
            add("PESO GALLO");
            add("PESO PLUMA");
            add("PESO WELTER");
            add("PESO MEDIO");
            add("PESO SEMIPESADO");
            add("PESO PESADO");
            add("TODOS LOS PESO");
        }};
        adapterCategorias=new AdapterCategorias(list, new AdapterCategorias.clickListener() {
            @Override
            public void itemClick(String dato) {

                textViewCategoria.setText(dato);
                Search(dato);
                bottomSheetDialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterCategorias);


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