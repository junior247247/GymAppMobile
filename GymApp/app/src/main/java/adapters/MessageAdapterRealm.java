package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import app.makingfight.gymapp.R;
import imageprovider.modelMessages;
import io.realm.RealmResults;

public class MessageAdapterRealm extends RecyclerView.Adapter<MessageAdapterRealm.ViewHolder>{
    Context context;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    RealmResults<modelMessages> realmResults;

    public MessageAdapterRealm(Context context,RealmResults<modelMessages> realmResults){
        this.context=context;
        this.realmResults=realmResults;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (viewType==1){
            return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_messages_derecha,parent,false));
        }else{
            return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_messages,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textViewMessage.setText(realmResults.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (auth.getCurrentUser().getUid().equals(realmResults.get(position).getIdSender())){
            return 1;
        }else {
            return 0;
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
