package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import app.makingfight.gymapp.R;
import app.makingfight.gymapp.SliderAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Eventos;
import Models.Paises;
import Models.SliderItems;
import adapters.AdapterPaises;
import adapters.PeleasApdater;


public class HomeFragment extends Fragment {

        View view;
        RecyclerView recyclerView;
        SliderView sliderView;
    ArrayList<SliderItems> sliderItems;
    PeleasApdater apdater;
    String permiso="";
    FirebaseFirestore firebaseFirestore;

        FirebaseAuth auth;
        String url="https://firebasestorage.googleapis.com/v0/b/gym-app-183b9.appspot.com/o/countries.json?alt=media&token=f65334e3-a1cc-4b80-a7cc-93ba2c3e6df9";
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recicleViewEventos);
        sliderView=view.findViewById(R.id.sliderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sliderItems=new ArrayList(){{add(new SliderItems("",R.drawable.banne1));
           add(new SliderItems("",R.drawable.a));
        }};
        BindSlider();



        return view;
    }


    void showBottom(){
        ArrayList<String>list= new ArrayList(){{
            add("GALICIA");
            add("CEUTA");
            add("MELILLA");
            add("ANDALUCIA");
            add("CASTILLA - LA MANCHA");
            add("MADRID");
            add("LA RIOJA");
            add("MURCIA");
            add("COM VALENCIANA");
            add("ARAGON");
            add("CATALUÑA");
            add("LA RIOJA");
            add("NAVARRA");
            add("P . VASCO");
            add("CANTABRIA");
            add("P . DE ASTURIAS");
            add("GALICIA");
            add("CASTILLA Y LEON");
            add("EXTREMADURA");
            add("CEUTA");
            add("MELILLA");
        }};
        ArrayList<Paises> paisesArrayList= new ArrayList<>();
        View view1=LayoutInflater.from(getContext()).inflate(R.layout.layout_button_shet_dialog,null);

        RecyclerView recyclerView=view1.findViewById(R.id.recicleViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);


        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1= new JSONObject(response);
                    JSONArray jsonArray=   jsonObject1.getJSONArray("countries");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  object   =jsonArray.getJSONObject(i);
                        String name=object.getString("name_en");
                        String  code=object.getString("code");
                        Paises paises= new Paises(name,code);
                        paisesArrayList.add(paises);


                    }

                    recyclerView.setAdapter(new AdapterPaises(paisesArrayList,null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
       RequestQueue requestQueue= Volley.newRequestQueue(getContext());
       requestQueue.add(stringRequest);
    }


    @Override
    public void onStart() {
        super.onStart();

        auth=FirebaseAuth.getInstance();
        isAdmin();




    }

    @Override
    public void onStop() {
        super.onStop();
        if (apdater!=null){
            apdater.stopListening();
        }

    }


    void isAdmin(){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("aprovacion").equals("si")){
                        permiso=documentSnapshot.getString("permiso");
                        Query query=FirebaseFirestore.getInstance().collection("Eventos").whereEqualTo("emision","EN EMISION").orderBy("timestamp",Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Eventos> options=new FirestoreRecyclerOptions.Builder<Eventos>()
                                .setQuery(query,Eventos.class)
                                .build();
                        apdater= new PeleasApdater(options,getContext(),permiso,getActivity());
                        recyclerView.setAdapter(apdater);
                        apdater.startListening();
                    }else{
                        Query query=FirebaseFirestore.getInstance().collection("Eventos").whereEqualTo("emision","EN EMISION").orderBy("timestamp",Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Eventos> options=new FirestoreRecyclerOptions.Builder<Eventos>()
                                .setQuery(query,Eventos.class)
                                .build();
                        apdater= new PeleasApdater(options,getContext(),"",getActivity());
                        recyclerView.setAdapter(apdater);
                        apdater.startListening();

                    }
                }else{
                    Query query=FirebaseFirestore.getInstance().collection("Eventos").whereEqualTo("emision","EN EMISION").orderBy("timestamp",Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Eventos> options=new FirestoreRecyclerOptions.Builder<Eventos>()
                            .setQuery(query,Eventos.class)
                            .build();
                    apdater= new PeleasApdater(options,getContext(),"",getActivity());
                    recyclerView.setAdapter(apdater);
                    apdater.startListening();
                }
            }
        });

    }



    void BindSlider(){
        sliderView=view.findViewById(R.id.sliderView);
        SliderAdapter adapter= new SliderAdapter(getContext(),sliderItems);
        sliderView.setSliderAdapter(adapter);
        // sliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        //sliderView.setIndicatorSelectedColor(Color.GRAY);
        // sliderView.setIndicatorUnselectedColor(Color.WHITE);

        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
}