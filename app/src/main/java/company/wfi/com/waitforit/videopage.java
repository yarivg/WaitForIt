package company.wfi.com.waitforit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
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

/**
 * Created by User on 22/10/2015.
 */
public class videopage extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String DeveloperKey = "AIzaSyBJwgHQfIaXBp-mKV8Fp68M50Me0OR8aPc";
    private YouTubePlayerView playerView;
    private YouTubePlayer youTubePlayer;
    public static int timeInSec = 0;
    private boolean fullScreen;
    private int height,width;
    private TextView timer;
    private static final int toMINUTE = 1000;
    private ImageButton outofvideo;
    private int topMarginTimer,heightTimer = 80;
    private int timeInAct = 0;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.videolayout);
        outofvideo = (ImageButton) findViewById(R.id.outofvideopage);
        outofvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        timer = (TextView)findViewById(R.id.timertxt);
        playerView = (YouTubePlayerView) findViewById(R.id.videoview);
        playerView.initialize(DeveloperKey, this);

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

        StartTimer();
        MakePlayerFullScreenPortrait();
    }
    private void HideStatusBar(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }
    private void FullScreenLandSpace(){
        HideViews();
        //HideStatusBar();
        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }
    private void MakePlayerFullScreenPortrait(){
        FrameLayout.LayoutParams myLayout = (FrameLayout.LayoutParams)playerView.getLayoutParams();
        timer.setVisibility(View.VISIBLE);
        outofvideo.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) timer.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) timer.getLayoutParams();
        topMarginTimer = lp.topMargin;

        //playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float density = getResources().getDisplayMetrics().density;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                heightTimer = (int)(93*density/1.5);
                //Toast.makeText(this,"HDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                heightTimer = 130;
                //Toast.makeText(this,"xHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                heightTimer = (int)(98*density/1.5);
                //Toast.makeText(this,"xxHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_560:
            case DisplayMetrics.DENSITY_XXXHIGH:
                heightTimer = (int)(100*density/1.5);
                //Toast.makeText(this,"560DPI",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,String.valueOf(getResources().getDisplayMetrics().densityDpi),Toast.LENGTH_SHORT).show();
        }
        myLayout.height = height - (int)((heightTimer + topMarginTimer));
        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, height - heightTimer - topMarginTimer));

        myLayout.gravity = Gravity.BOTTOM;
        playerView.setLayoutParams(myLayout);
    }

    public void StartTimer(){
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        timeInSec = prefs.getInt("WaitingTime",600);
        //A function which starts the timer or continue it
        new CountDownTimer(timeInSec*toMINUTE, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 / 60 >= 10) {
                    if (millisUntilFinished / 1000 % 60 >= 10)
                        timer.setText(String.valueOf(millisUntilFinished / 1000 / 60 + ":" + millisUntilFinished / 1000 % 60));
                    else
                        timer.setText(String.valueOf(millisUntilFinished / 1000 / 60 + ":0" + millisUntilFinished / 1000 % 60));
                } else {
                    if (millisUntilFinished / 1000 % 60 >= 10)
                        timer.setText("0" + String.valueOf(millisUntilFinished / 1000 / 60 + ":" + millisUntilFinished / 1000 % 60));
                    else
                        timer.setText("0" + String.valueOf(millisUntilFinished / 1000 / 60 + ":0" + millisUntilFinished / 1000 % 60));
                }
                timeInSec = (int)millisUntilFinished/1000;
                timeInAct++;
            }
            public void onFinish() {
                timer.setText("00:00");
            }
        }.start();
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
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
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
                        //HideViews();
                        //videoWidth = playerView.getLayoutParams().width;
                        //videoHeight = playerView.getLayoutParams().height;
                        //playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
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
        //TODO(later) A method which get a url from the database to display to the user
        return "R7AXBOT8KzU";
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
                ratingAct.timeInSec = timeInSec - 1;
                finish();
                startActivity(new Intent(getApplicationContext(), ratingAct.class));
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                Toast.makeText(getApplicationContext(), errorReason.toString(), Toast.LENGTH_SHORT).show();
//                if(!Internet.isNetworkAvailable(getApplicationContext()))
//                    Internet.ShowDialog(getApplicationContext());
            }
        });
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                //Toast.makeText(getApplicationContext(), "Video is playing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaused() {
                //Toast.makeText(getApplicationContext(), "Video Paused", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopped() {
                //Toast.makeText(getApplicationContext(), "Video Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBuffering(boolean b) {
                //Toast.makeText(getApplicationContext(), "Video Buffering", Toast.LENGTH_SHORT).show();
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