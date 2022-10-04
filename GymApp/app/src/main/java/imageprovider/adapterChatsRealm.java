package imageprovider;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import app.makingfight.gymapp.ChatActivity;
import app.makingfight.gymapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

public class adapterChatsRealm extends RecyclerView.Adapter<adapterChatsRealm.ViewHolder>{
    RealmResults<RealmChats> realmResults;
    Context context;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    Activity activity;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public adapterChatsRealm(Context context,RealmResults<RealmChats> realmChats,Activity activity){
        this.context=context;
        this.realmResults=realmChats;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_items_chats,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.Bind(realmResults.get(position));
        String id="";
        if (auth.getCurrentUser().getUid().equals(realmResults.get(position).getIdUser1())){
            id=realmResults.get(position).getIdUser2();
        }else{
            id=realmResults.get(position).getIdUser1();
        }

        holder.getUser(id);
        holder.getCount(realmResults.get(position).getIdChat());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id="";
                if (auth.getCurrentUser().getUid().equals(realmResults.get(position).getIdUser1())){
                        id=realmResults.get(position).getIdUser2();
                }else{
                    id=realmResults.get(position).getIdUser1();
                }
                Intent intent= new Intent(context, ChatActivity.class);
                intent.putExtra("idUser",id);
                intent.putExtra("idChat",realmResults.get(position).getIdChat());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());


            }
        });
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewProfileChat;
        TextView textViewUserNameChat,textViewUltimoMensaje,textViewCountLastMessage;
        CardView carviewLastMessage;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textViewUserNameChat=itemView.findViewById(R.id.textViewUserNameChat);
            textViewUltimoMensaje=itemView.findViewById(R.id.textViewUltimoMensaje);
            textViewCountLastMessage=itemView.findViewById(R.id.textViewCountLastMessage);
            circleImageViewProfileChat=itemView.findViewById(R.id.circleImageViewProfileChat);
            carviewLastMessage=itemView.findViewById(R.id.carviewLastMessage);
        }
        void Bind(RealmChats model){
            textViewUserNameChat.setText(model.getNameUser());
        }


        void getCount(String idChat){
                firestore.collection("Messages").whereEqualTo("idReceiber",auth.getCurrentUser().getUid()).whereEqualTo("idChat",idChat).whereEqualTo("visto",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if (value!=null){
                            if (value.size()>0){
                                textViewCountLastMessage.setText(value.size()+"");
                                carviewLastMessage.setVisibility(View.VISIBLE);
                            }else{
                                textViewCountLastMessage.setText("");
                                carviewLastMessage.setVisibility(View.GONE);
                            }

                        }
                    }
                });
        }
        void getUser(String id){
            firestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String name=documentSnapshot.getString("name");
                        String urlimg=documentSnapshot.getString("urlImg");
                        Picasso.with(context).load(urlimg).placeholder(R.drawable.person).fit().into(circleImageViewProfileChat);
                        textViewUserNameChat.setText(name);
                    }
                }
            });
        }
    }




}
