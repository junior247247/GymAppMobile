package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import Models.Messages;
import imageprovider.modelMessages;
import io.realm.RealmResults;

public class MessageAdapter extends FirestoreRecyclerAdapter<Messages,MessageAdapter.ViewHolder> {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    Context context;
    RealmResults<modelMessages> resultMessage;

    public MessageAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Messages> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Messages model) {

            holder.textViewMessage.setText(model.getMessage());


    }

    @Override
    public int getItemViewType(int position) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(position);

        if (auth.getCurrentUser().getUid().equals(documentSnapshot.getString("idSender"))){
            return 1;
        }else{
            return 0;
        }

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType==1){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_messages_derecha,parent,false));
        }else{
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_messages,parent,false));
        }

    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage;
        LinearLayout linearMessages;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewMessage=itemView.findViewById(R.id.textViewMessage);
            linearMessages=itemView.findViewById(R.id.linearMessages);
        }
    }
}
