package app.makingfight.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import app.makingfight.gymapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Models.chats;
import adapters.chatsAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ListChatsActivity extends AppCompatActivity  {
    Toolbar toolbar;
    RecyclerView recicleViewChats;
    chatsAdapter chatsAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    int page=1,limit=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_list_chats);
        auth=FirebaseAuth.getInstance();
        recicleViewChats=findViewById(R.id.recicleViewChats);
        recicleViewChats.setLayoutManager(new LinearLayoutManager(this));
        firestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CHATS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView=findViewById(R.id.scroll_view);
        progressBar=findViewById(R.id.progress_bar);


            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                        progressBar.setVisibility(View.VISIBLE);

                    }
                }
            });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }


    }



    private void  getData(String url){
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query= firestore.collection("chats").whereArrayContains("ids", auth.getCurrentUser().getUid()).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<chats> options= new FirestoreRecyclerOptions.Builder<chats>().setQuery(query,chats.class).build();
        chatsAdapter= new chatsAdapter(options,this,this);
        chatsAdapter.startListening();
        recicleViewChats.setLayoutManager(new LinearLayoutManager(this));
        recicleViewChats.setAdapter(chatsAdapter);




    }


    @Override
    protected void onStop() {
        super.onStop();
        if (chatsAdapter!=null){
            chatsAdapter.stopListening();
            if (chatsAdapter.getLisener()!=null){
                chatsAdapter.getLisener().remove();
            }

            if (chatsAdapter.getListenerRegistration2()!=null){
                chatsAdapter.getListenerRegistration2().remove();
            }


        }


    }




}