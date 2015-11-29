package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Config;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.IPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.IPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by User on 17/10/2015.
 */
public class gameAct extends Activity implements View.OnClickListener,CordovaInterface {

    boolean pause = true;
    private int topMarginTimer,heightTimer = 80;
    ImageButton pauseGame;
    WebView webView;
    String gameUrl = "https://googledrive.com/host/0ByndIaCDcs86ekRjNVRLT1lySUE/index.html";
    CordovaWebView mainView;
    private IPlugin activityResultCallback;
    private Object activityResultKeepRunning;
    private Object keepRunning;
    public static int timeInSec = 0;
    private TextView timer;
    private static final int toMINUTE = 1000;
    //TODO solve the problem with resuming the game- maybe from a specific sdk it works
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().putExtra("loadUrlTimeoutValue", 7000);
        setContentView(R.layout.gamepage);

        timer = (TextView)findViewById(R.id.timertxt);
        mainView = (CordovaWebView) findViewById(R.id.mainView);
        MakePlayerFullScreenPortrait();
        // Simplest usage: note that an exception will NOT be thrown
        // if there is an error loading this page (see below).
        mainView.setWebViewClient(new MyWebViewClient());
        //mainView.setAnimationCacheEnabled(true);
        //mainView.getSettings().setJavaScriptEnabled(true);
        //mainView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        mainView.loadUrl(gameUrl);
        ViewTreeObserver vto = timer.getViewTreeObserver();
        mainView.setBackgroundColor(Color.WHITE);
        pauseGame = (ImageButton)findViewById(R.id.pausegame);
        pauseGame.setOnClickListener(this);
        StartTimer();
        //mainView.getSettings().setDomStorageEnabled(true);
        //mainView.getSettings().setAllowFileAccess(true);
        //mainView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //mainView.getSettings().setAppCacheEnabled(false);
    }
    private void onGameEnded(){
        startActivity(new Intent(this,ratingAct.class));
    }
    public void StartTimer() {
        //A function which starts the timer or continue it
        new CountDownTimer(timeInSec * toMINUTE, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
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
                timeInSec = (int) millisUntilFinished / 1000;
            }

            public void onFinish() {
                timer.setText("00:00");
            }
        }.start();
    }

    private void MakePlayerFullScreenPortrait(){
        FrameLayout.LayoutParams myLayout = (FrameLayout.LayoutParams)mainView.getLayoutParams();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) timer.getLayoutParams();
        topMarginTimer = lp.topMargin;

        //playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float density = getResources().getDisplayMetrics().density;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                heightTimer = (int)(93*density/1.5);
                Toast.makeText(this,"HDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                heightTimer = 130;
                Toast.makeText(this,"xHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                heightTimer = (int)(98*density/1.5);
                Toast.makeText(this,"xxHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_560:
            case DisplayMetrics.DENSITY_XXXHIGH:
                heightTimer = (int)(100*density/1.5);
                Toast.makeText(this,"560DPI",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,String.valueOf(getResources().getDisplayMetrics().densityDpi),Toast.LENGTH_SHORT).show();
        }
        myLayout.height = height - (int)((heightTimer + topMarginTimer));
        //myLayout.height =  height - 175;
        myLayout.gravity = Gravity.BOTTOM;
        mainView.setLayoutParams(myLayout);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"Back button will be disabled here on beta.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void cancelLoadUrl() {

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pausegame:
                if(pause) {
                    mainView.onPause();
                    //mainView.getSettings().setJavaScriptEnabled(false);
                    pauseGame.setImageResource(R.drawable.resumegame);
                    pause = false;
                }
                else {
                    mainView.onResume();
                    //mainView.getSettings().setJavaScriptEnabled(true);
                    pauseGame.setImageResource(R.drawable.pausegame);
                    pause = true;
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainView.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(IPlugin iPlugin, Intent intent, int i) {

    }

    @Override
    public void setActivityResultCallback(IPlugin plugin) {
        this.activityResultCallback = plugin;
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    public Object onMessage(String id, Object data) {
        LOG.d("gameAct", "onMessage(" + id + "," + data + ")");
        if ("exit".equals(id)) {
            super.finish();
        }
        return null;
    }
}
