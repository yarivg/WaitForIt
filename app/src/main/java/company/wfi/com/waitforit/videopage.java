package company.wfi.com.waitforit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Timer;
import java.util.TimerTask;


public class videopage extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
//TODO use this to solve the re-buffering problem  -https://code.google.com/p/youtube-api-samples/source/browse/android-player/src/com/examples/youtubeapidemo/FullscreenDemoActivity.java?r=c2e8be914ee785f7e14a2c5bf1f183ea2510d09c
    private String DeveloperKey = "AIzaSyBJwgHQfIaXBp-mKV8Fp68M50Me0OR8aPc";

    private static final int toMINUTE = 1000;
    public static int timeInSec = 0;
    private YouTubePlayerView playerView;

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("videopage","OnStart");
    }
    private Thread mThread;
    private YouTubePlayer youTubePlayer;
    private boolean fullScreen;
    private int height,width;
    private TextView timer;
    private ImageButton outofvideo,skipVideo;
    private int topMarginTimer,heightTimer = 80;
    private int timeInAct = 0;
    public static String videoURL = "";
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.videolayout);
        outofvideo = (ImageButton) findViewById(R.id.exitplaylistbtn);
        outofvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        skipVideo = (ImageButton)findViewById(R.id.skipvideo);
        skipVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingAct.total_time = "0";
                ratingAct.total_score = "0";
                finish();
                startActivity(new Intent(getApplicationContext(), ratingAct.class));
            }
        });
        timer = (TextView)findViewById(R.id.timertxt);
        playerView = (YouTubePlayerView) findViewById(R.id.videoview);
        playerView.initialize(DeveloperKey, this);
        Typeface fontTimer = Typeface.createFromAsset(getAssets(),"fonts/Ailerons.ttf");
        timer.setTypeface(fontTimer);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;
        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fullScreen) {
                    //HideStatusBar();
                    HideViews();
                }
                return true;
            }
        });
        MakePlayerFullScreenPortrait();
        StartThisClock();
    }


    private void StartThisClock() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(myClock.getTimeText());
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
    private void FullScreenLandSpace(){
        HideViews();
        //HideStatusBar();
        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }
    //TODO add method called onConfigurationChanged to detect any screen orientaition change and handle the transition between portrait and landspace.
    private void MakePlayerFullScreenPortrait(){
        FrameLayout.LayoutParams myLayout = (FrameLayout.LayoutParams)playerView.getLayoutParams();
        timer.setVisibility(View.VISIBLE);
        outofvideo.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) timer.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) timer.getLayoutParams();
        topMarginTimer = lp.topMargin;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float density = getResources().getDisplayMetrics().density;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                heightTimer = (int)(93*density/1.5);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                heightTimer = 130;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                heightTimer = (int)(98*density/1.5);
                break;
            case DisplayMetrics.DENSITY_560:
            case DisplayMetrics.DENSITY_XXXHIGH:
                heightTimer = (int)(100*density/1.5);
                break;
            default:
                break;
        }
        myLayout.height = height - ((heightTimer + topMarginTimer));
        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height - heightTimer - topMarginTimer));

        myLayout.gravity = Gravity.BOTTOM;
        playerView.setLayoutParams(myLayout);
    }

    @Override
    public void onBackPressed() {
        if (fullScreen) {
            youTubePlayer.setFullscreen(false);
        } else {
            //super.onBackPressed();
            if(youTubePlayer != null) {
                youTubePlayer.pause();
            }
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exiting Playlist")
                    .setMessage("Are you sure you want to exit the playlist?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), waitingcompleteAct.class));
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
        timer.setY(timer.getY() + 5);
    }

    @Override
    public void onInitializationSuccess(final YouTubePlayer.Provider provider, final YouTubePlayer player,
                                        boolean restored) {
        if (!restored) {
            this.youTubePlayer = player;
            callEvents();
            this.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean NEWfullscreen) {
                    if (NEWfullscreen) {
                        FullScreenLandSpace();
                    } else {
                        MakePlayerFullScreenPortrait();
                    }
                    fullScreen = NEWfullscreen;

                }
            });
            if (fullScreen)
                HideViews();
            player.loadVideo(GetVideoUrl());
        }
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        //player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
    }
    public void HideViews(){
        timer.setVisibility(View.INVISIBLE);
        outofvideo.setVisibility(View.INVISIBLE);
    }
    private String GetVideoUrl(){
        return videoURL;
    }
    private void callEvents(){
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                //Toast.makeText(getApplicationContext(), "Video Loading", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLoaded(String s) {
                //Toast.makeText(getApplicationContext(), "Video Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {
                //Toast.makeText(getApplicationContext(), "Video Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVideoEnded() {
                ratingAct.total_time = "0";
                ratingAct.total_score = "0";
                finish();
                startActivity(new Intent(getApplicationContext(), ratingAct.class));
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                //Log.e("VideoPageError",errorReason.toString());
                if(errorReason == YouTubePlayer.ErrorReason.NOT_PLAYABLE){
                    playlistInfo.NextInPlaylist(getApplicationContext());
                }
            }
        });
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onStopped() {
            }

            @Override
            public void onBuffering(boolean b) {
            }

            @Override
            public void onSeekTo(int i) {

            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}