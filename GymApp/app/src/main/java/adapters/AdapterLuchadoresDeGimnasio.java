package adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import Models.addLuchadores;

public class AdapterLuchadoresDeGimnasio extends FirestoreRecyclerAdapter<addLuchadores,AdapterLuchadoresDeGimnasio.ViewHolde> {

    Context context;
    Activity activity;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    int i=0;
    setCountListener setCountListener;
    SparseBooleanArray sparseBooleanArray= new SparseBooleanArray();
    public static ArrayList<String> ids= new ArrayList<>();
    String id;

    public AdapterLuchadoresDeGimnasio(@NonNull @NotNull FirestoreRecyclerOptions<addLuchadores> options,Activity activity,Context context,String id,setCountListener countListener) {
        super(options);
        this.context=context;
        this.activity=activity;
        this.setCountListener=countListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolde holder, int position, @NonNull @NotNull addLuchadores model) {
        //if (model.getIdGym().equals(auth.getCurrentUser().getUid())){
            holder.Bind(model.getIdLuchador());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                            if (sparseBooleanArray!=null){
                                if (sparseBooleanArray.get(position,true)){
                                    sparseBooleanArray.put(position,false);
                                    ids.add(model.getIdLuchador());
                                    holder.viewSelect.setVisibility(View.VISIBLE);
                                    i=i+1;
                                }else{
                                    holder.viewSelect.setVisibility(View.GONE);
                                    sparseBooleanArray.put(position,true);
                                    String id= model.getIdLuchador();
                                    i=i-1;
                                    for (int i=0;i<ids.size();i++){
                                        if (ids.get(i).equals(id)){
                                            ids.remove(i);
                                        }
                                    }
                                }

                            }
                            setCountListener.CountItmen(i, model.getIdLuchador());


                }
            });
        //}
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.laoyut_items_luchadores,parent,false));
    }



    public class ViewHolde extends RecyclerView.ViewHolder{
        TextView textViewName,textViewRango,textViewAlias;
        ImageView imageViewAtleta;
        View viewSelect;
        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            textViewAlias=itemView.findViewById(R.id.textViewAlias);
            textViewName=itemView.findViewById(R.id.textViewNameAtleta);
            textViewRango=itemView.findViewById(R.id.textViewRango);
            imageViewAtleta=itemView.findViewById(R.id.imageViewLuchador);
            viewSelect=itemView.findViewById(R.id.viewSelect);
        }

        public void Bind(String id){

            FirebaseFirestore.getInstance().collection("Luchadores").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                         if (documentSnapshot.exists()){
                             String ganadas=documentSnapshot.getString("ganadas");
                             String perdidas=documentSnapshot.getString("perdidas");
                             String empates=documentSnapshot.getString("empates");
                             String name=documentSnapshot.getString("name");
                             String alias=documentSnapshot.getString("alias");
                             String urlImage=documentSnapshot.getString("urlImage");
                             String id=documentSnapshot.getString("id");
                             String dato="";
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


                             textViewRango.setText(dato);
                             textViewName.setText(name.toUpperCase());
                                 textViewAlias.setText(alias.toUpperCase());


                             Picasso.with(context).load(urlImage).placeholder(R.drawable.spiner).fit().into(imageViewAtleta);

                         }
                }
            });



        }
    }

    public interface  setCountListener{
        void CountItmen(int count,String id);
    }
}
