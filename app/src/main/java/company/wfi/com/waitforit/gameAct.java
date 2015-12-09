package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.LoginFilter;
import android.util.Config;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.IPlugin;
import org.apache.cordova.api.LOG;

public class gameAct extends Activity implements View.OnClickListener,CordovaInterface {

    final CountDownTimer countDownTimer = new CountDownTimer(timeInSec * toMINUTE, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
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
    };
    boolean pause = true;
    private int topMarginTimer,heightTimer = 80;
    ImageButton pauseGame;
    public static String[] games = new String[9];
    public static WebView mainView;
    public static boolean firstJS = true;
    public static String lastGameUrl = "";
    private IPlugin activityResultCallback;
    private Object activityResultKeepRunning;
    private Object keepRunning;
    public static int timeInSec = 0;
    private TextView timer;

    public static boolean loadNewGame = false;
    private static final int toMINUTE = 1000;
    public class MyInterface {
        public String $points;
        public String $totalTime;
        public Context context;

        @JavascriptInterface
        public void getGameInfo(String $points,String $totalTime){
            if(firstJS || !lastGameUrl.equals(playlistInfo.mPlaylist.get(playlistInfo.indexInList).getUrl())) {
                Log.d("url", "JS ecexuted.");
                firstJS = false;
                lastGameUrl = playlistInfo.mPlaylist.get(playlistInfo.indexInList).getUrl();
                this.$points = $points;
                this.$totalTime = $totalTime;
                ratingAct.total_score = $points;
                ratingAct.total_time = $totalTime;
                countDownTimer.cancel();
                ratingAct.timeInSec = timeInSec;
                startActivity(new Intent(getApplicationContext(), ratingAct.class));
                //context.startActivity(new Intent(context.getApplicationContext(),ratingAct.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("url", "onCreate");
        getIntent().putExtra("loadUrlTimeoutValue", 170000);
        setContentView(R.layout.gamepage);
        timer = (TextView)findViewById(R.id.timertxt);
        Typeface fontTimer = Typeface.createFromAsset(getAssets(),"fonts/Ailerons.ttf");
        timer.setTypeface(fontTimer);
        pauseGame = (ImageButton)findViewById(R.id.pausegame);
        pauseGame.setOnClickListener(this);

        //countDownTimer.start();
        StartThisClock();
        gameAct.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainView = (WebView) findViewById(R.id.mainView);
                mainView.getSettings().setJavaScriptEnabled(true);
                mainView.setBackgroundColor(Color.WHITE);
                mainView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Log.d("url", url);
                        return false;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.d("url", "Page started");
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        Log.d("url", description);
                    }
                });
                mainView.setWebChromeClient(new WebChromeClient());
                mainView.getSettings().setDomStorageEnabled(true);
                mainView.getSettings().setSupportMultipleWindows(true);
                mainView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
                MyInterface myInterface = new MyInterface();
                myInterface.context = getApplicationContext();
                mainView.addJavascriptInterface(myInterface, "myInterface");
            }
        });
        MakePlayerFullScreenPortrait();
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
    private void MakePlayerFullScreenPortrait(){
        FrameLayout.LayoutParams myLayout = (FrameLayout.LayoutParams)mainView.getLayoutParams();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) timer.getLayoutParams();
        topMarginTimer = lp.topMargin;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float density = getResources().getDisplayMetrics().density;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                heightTimer = (int) (93 * density / 1.5);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                heightTimer = 130;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                heightTimer = (int) (98 * density / 1.5);
                break;
            case DisplayMetrics.DENSITY_560:
            case DisplayMetrics.DENSITY_XXXHIGH:
                heightTimer = (int) (100 * density / 1.5);
                break;
            default:
                break;
        }
        myLayout.height = height - ((heightTimer + topMarginTimer));
        myLayout.gravity = Gravity.BOTTOM;
        mainView.setLayoutParams(myLayout);
    }

    @Override
    public void onBackPressed() {
        PauseGame();
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
                    PauseGame();
                }
                else {
                   ResumeGame();
                }
                break;
        }
    }
    private void PauseGame() {
        if (Build.VERSION.SDK_INT < 19) {
            Toast.makeText(this,"Your android version dosen't support pausing games.",Toast.LENGTH_SHORT).show();
        } else {
            mainView.onPause();
            pauseGame.setImageResource(R.drawable.resumegame);
            pause = false;
        }
    }
    private void ResumeGame() {
        if (Build.VERSION.SDK_INT < 19) {
            Toast.makeText(this,"Your android version dosen't support resuming games.",Toast.LENGTH_SHORT).show();
        } else {
            mainView.onResume();
            pauseGame.setImageResource(R.drawable.pausegame);
            pause = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(loadNewGame){
            String currentUrl = playlistInfo.mPlaylist.get(playlistInfo.indexInList).getUrl();
            mainView.loadUrl(currentUrl);
            loadNewGame = false;
            mainView.onResume();
            pauseGame.setImageResource(R.drawable.pausegame);
            pause = true;
        }
        Log.d("testURl", "onStart()");
        //games[0] = "https://googledrive.com/host/0ByndIaCDcs86ekRjNVRLT1lySUE/index.html";//pupe down!
//        games[1] = "https://googledrive.com/host/0ByndIaCDcs86TkJzcVBoM2h4dHc/index.html";//ninja!
//        games[2] = "https://googledrive.com/host/0ByndIaCDcs86U29Wam1NY091azg/index.html";//pong catchup!
//        games[3] = "https://googledrive.com/host/0ByndIaCDcs86RXUxaEZNYjF2enc/index.html";//monster touch!
//        games[4] = "https://googledrive.com/host/0ByndIaCDcs86V3lhZXNUM0dXLUU/index.html";//stability!
//        games[5] = "https://googledrive.com/host/0ByndIaCDcs86R0p6LURMdHY1VjQ/index.html";//colors!
//        games[6] = "https://googledrive.com/host/0ByndIaCDcs86azZuejUwM0dqTGM/index.html";//cars!
//        games[7] = "https://googledrive.com/host/0ByndIaCDcs86eXNUcTFMSkFPb0U/index.html";//trangles!
//        games[8] = "https://d8b923084546aa2ae0cecaf4ba13d7273dc0556f.googledrive.com/host/0ByndIaCDcs86SHFkRDRzSkFOOUk/index.html";//apple black and red!

        //mainView.loadUrl("https://googledrive.com/host/0ByndIaCDcs86RXUxaEZNYjF2enc/index.html");
        MakePlayerFullScreenPortrait();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //countDownTimer.cancel();

        //finish();
        Log.d("url", "onDestroy");
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
        mainView.reload();
        if ("exit".equals(id)) {
            super.finish();
        }
        return null;
    }
}
