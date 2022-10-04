package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.makingfight.gymapp.R;
import app.makingfight.gymapp.VideoActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import Models.Video;
import imageprovider.SaveImagerToFirebase;
import service.RelativeTime;

public class VideoApater extends FirestoreRecyclerAdapter<Video,VideoApater.ViewHolder> {
    Context context;
    String permiso="";
    SaveImagerToFirebase saveImagerToFirebase;
    public VideoApater(@NonNull @NotNull FirestoreRecyclerOptions<Video> options, Context context,String permiso) {
        super(options);
        this.context=context;
        this.permiso=permiso;
        saveImagerToFirebase= new SaveImagerToFirebase();
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Video model) {
        holder.Bind(model);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_video,parent,false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewVideo;
        View view;
        TextView textViewTitle,textViewDescription,textViewTimeVideo;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            imageViewVideo=itemView.findViewById(R.id.imageViewvideoImage);
            textViewTitle=itemView.findViewById(R.id.textViewTittleVideo);
            textViewDescription=itemView.findViewById(R.id.textViewDescriptionVideo);
            textViewTimeVideo=itemView.findViewById(R.id.textViewTimeVideo);
            view=itemView;
        }
        public void Bind(Video video){
           Picasso.with(context).load(video.getMiniatura()).fit().placeholder(R.drawable.spiner).into(imageViewVideo);
            textViewTitle.setText(video.getVideoTittle().toUpperCase());
            //textViewTimeVideo.setText(video.getTimeVideo());
            String description=video.getVideoDescription();
            if (description.length()>=100){
                textViewDescription.setText(description.substring(0,100)+"...");
            }else{
                textViewDescription.setText(description);
            }
            textViewTimeVideo.setText(RelativeTime.getTimeAgo(video.getTimestamp(),context));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permiso.equals("admin")){
                        PopupMenu popupMenu= new PopupMenu(context,itemView);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_video_admin,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.verVideo:
                                        Intent intent= new Intent(context, VideoActivity.class);
                                        intent.putExtra("id",video.getId());
                                        context.startActivity(intent);
                                        break;
                                    case R.id.videoElimnar:
                                        new AlertDialog.Builder(context)
                                                .setTitle("ELIMINAR VIDEO")
                                                .setMessage("¿REALMENTE DECEAS ELIMINAR ESTE VIDEO?")
                                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        eliminar(video.getId(),video.getVideoURL());
                                                    }
                                                })
                                                .setNegativeButton("NO",null)
                                                .show();
                                        break;
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }else{
                        Intent intent= new Intent(context, VideoActivity.class);
                        intent.putExtra("id",video.getId());
                        context.startActivity(intent);
                    }

                }
            });
        }
        void eliminar(String id,String url){
            FirebaseFirestore.getInstance().collection("Videos").document(id).delete();
            saveImagerToFirebase.delete(url);
        }
    }
}
