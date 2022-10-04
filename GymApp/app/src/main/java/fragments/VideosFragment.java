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

import Models.Video;
import adapters.VideoApater;


public class VideosFragment extends Fragment {
        RecyclerView recyclerViewVideo;
        View view;
        VideoApater videoApater;
        FirebaseAuth auth;
        String permiso="";
    public VideosFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_videos, container, false);
        auth=FirebaseAuth.getInstance();
        recyclerViewVideo=view.findViewById(R.id.recicleViewVideo);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getContext()));

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
        if (videoApater!=null){
            videoApater.stopListening();
        }

    }


    void isAdmin(){
        FirebaseFirestore.getInstance().collection("UserAdmin").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("aprovacion").equals("si")){
                        permiso=documentSnapshot.getString("permiso");
                        Query query=FirebaseFirestore.getInstance().collection("Videos").orderBy("timestamp",Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Video> options=new FirestoreRecyclerOptions.Builder<Video>()
                                .setQuery(query,Video.class)
                                .build();
                        videoApater= new VideoApater(options,getContext(),permiso);
                        recyclerViewVideo.setAdapter(videoApater);
                        videoApater.startListening();
                    }else{
                        Query query=FirebaseFirestore.getInstance().collection("Videos").orderBy("timestamp",Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<Video> options=new FirestoreRecyclerOptions.Builder<Video>()
                                .setQuery(query,Video.class)
                                .build();
                        videoApater= new VideoApater(options,getContext(),"");
                        recyclerViewVideo.setAdapter(videoApater);
                        videoApater.startListening();

                    }
                }else{
                    Query query=FirebaseFirestore.getInstance().collection("Videos").orderBy("timestamp",Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Video> options=new FirestoreRecyclerOptions.Builder<Video>()
                            .setQuery(query,Video.class)
                            .build();
                    videoApater= new VideoApater(options,getContext(),"");
                    recyclerViewVideo.setAdapter(videoApater);
                    videoApater.startListening();
                }
            }
        });

    }

}