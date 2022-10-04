package app.makingfight.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import app.makingfight.gymapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VideoActivity extends AppCompatActivity {
    String URL="https://firebasestorage.googleapis.com/v0/b/practicando-e7d08.appspot.com/o/Roman%20Reigns%E2%80%99%20WrestleMania%20workout%20for%20Brock%20Lesnar%20match.mp4?alt=media&token=5dddeb30-f3ca-4b7c-ac9b-4b91ff40c9d5";
    VideoView videoView;
    private View decorView;
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btnFullScreen;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);


    /*    decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(hideSystemBars());
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i==0)
                    decorView.setSystemUiVisibility(hideSystemBars());
                
            }
        });


     */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        if (getIntent().getExtras()!=null){
            showVideo(getIntent().getStringExtra("id"));
        }
       // btnFullScreen=playerView.findViewById(R.id.btnFullScren);



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    void showVideo(String id){
        FirebaseFirestore.getInstance().collection("Videos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                   if (documentSnapshot.exists()){
                       String urlImge= documentSnapshot.getString("videoURL");

                        String url="https://tioanime.com/ver/boruto-naruto-next-generations-tv-247";

                       playerView=findViewById(R.id.player_view);
                       progressBar=findViewById(R.id.progress_bar);
                       btnFullScreen=playerView.findViewById(R.id.btnFullScren);
                       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                       Uri videiUrl=Uri.parse(urlImge);
                       LoadControl loadControl= new DefaultLoadControl();

                       BandwidthMeter bandwidthMeter= new DefaultBandwidthMeter();

                       TrackSelector trackSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                       simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(VideoActivity.this,trackSelector,loadControl);

                       DefaultHttpDataSourceFactory factory= new DefaultHttpDataSourceFactory("exoplayer_video");

                       ExtractorsFactory extractorsFactory= new DefaultExtractorsFactory();

                       MediaSource mediaSource= new ExtractorMediaSource(videiUrl,factory,extractorsFactory,null,null);

                       playerView.setPlayer(simpleExoPlayer);
                       playerView.setKeepScreenOn(true);
                       simpleExoPlayer.prepare(mediaSource);
                       simpleExoPlayer.setPlayWhenReady(true);
                       simpleExoPlayer.addListener(new Player.EventListener() {
                           @Override
                           public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                           }

                           @Override
                           public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                           }

                           @Override
                           public void onLoadingChanged(boolean isLoading) {

                           }

                           @Override
                           public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                               if (playbackState==Player.STATE_BUFFERING){
                                   progressBar.setVisibility(View.VISIBLE);
                               }else if (playbackState==Player.STATE_READY){
                                   progressBar.setVisibility(View.GONE);
                               }
                           }

                           @Override
                           public void onRepeatModeChanged(int repeatMode) {

                           }

                           @Override
                           public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                           }

                           @Override
                           public void onPlayerError(ExoPlaybackException error) {

                           }

                           @Override
                           public void onPositionDiscontinuity(int reason) {

                           }

                           @Override
                           public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                           }

                           @Override
                           public void onSeekProcessed() {

                           }
                       });


                       btnFullScreen.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               if (flag){
                                   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                   btnFullScreen.setImageResource(R.drawable.full_screen);
                                   flag=false;
                               }else{
                                   flag=true;
                                   btnFullScreen.setImageResource(R.drawable.exitscrean);
                                   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                               }
                           }
                       });


                   }
            }
        });
    }



    int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }

}