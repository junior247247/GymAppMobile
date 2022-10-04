package adapters;

import android.app.Activity;
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

import app.makingfight.gymapp.ChatActivity;
import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.chats;
import de.hdodenhof.circleimageview.CircleImageView;

public class chatsAdapter extends FirestoreRecyclerAdapter<chats, chatsAdapter.ViewHolder> {
    Context context;
    Activity activity;
    String idUser="";
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    ListenerRegistration listenerRegistration;
    ListenerRegistration listenerRegistration2;

    public chatsAdapter(@NonNull @NotNull FirestoreRecyclerOptions<chats> options,Context context,Activity activity) {
        super(options);
        this.activity=activity;
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull chats model) {

        if (model.getIdUser1().equals(auth.getCurrentUser().getUid())){
            //holder.Bind(model.getIdUser2());
            //idUser=model.getIdUser2();
            holder.Bind(model.getIdUser2());
        }else{
            holder.Bind(model.getIdUser1());
        }
        holder.mensajeNoleidos(model.getId());

        holder.getLastMessage(model.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getIdUser1().equals(auth.getCurrentUser().getUid())){
                    //holder.Bind(model.getIdUser2());
                    idUser=model.getIdUser2();
                }else{
                    // holder.Bind(model.getIdUser1());
                    idUser=  model.getIdUser1();
                }
                Intent intent= new Intent(context, ChatActivity.class);
                intent.putExtra("idChat",model.getId());
                intent.putExtra("idUser",idUser);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_items_chats,parent,false));
    }

    public  ListenerRegistration getListenerRegistration2(){
        return listenerRegistration2;
    }

    public ListenerRegistration getLisener(){
        return  listenerRegistration;
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


        void getLastMessage(String idChat){
            listenerRegistration=  firestore.collection("Messages").whereEqualTo("idChat",idChat).limit(1).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (value!=null){
                        if (value.size()>0){
                            textViewUltimoMensaje.setText(value.getDocuments().get(0).getString("message"));
                        }
                    }
                }
            });
        }

        void mensajeNoleidos(String idChat){
            listenerRegistration2=  firestore.collection("Messages").whereEqualTo("idChat",idChat).whereEqualTo("visto",false).whereEqualTo("idReceiber",auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    if (value.size()>0){
                        carviewLastMessage.setVisibility(View.VISIBLE);
                        textViewCountLastMessage.setText(value.size()+"");
                    }else{
                        textViewCountLastMessage.setText("");
                        carviewLastMessage.setVisibility(View.GONE);
                    }
                }
            }
        });
        }
        void Bind(String id){
            firestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String name=documentSnapshot.getString("name");
                        String urlImage=documentSnapshot.getString("urlImg");
                        if (!urlImage.isEmpty()){
                            Picasso.with(context).load(urlImage).placeholder(R.drawable.profile).into(circleImageViewProfileChat);
                        }else{
                            Picasso.with(context).load(R.drawable.profile).into(circleImageViewProfileChat);
                        }
                        textViewUserNameChat.setText(name);
                    }else{
                        textViewUserNameChat.setText("anonymous");
                        Picasso.with(context).load(R.drawable.profile).into(circleImageViewProfileChat);
                    }
                }
            });
        }
    }
}
