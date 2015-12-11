package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

public class gameAct extends Activity implements View.OnClickListener {

    boolean pause = true;
    private int topMarginTimer,heightTimer = 80;
    public static String[] games = new String[9];
    public static WebView mainView;
    public static boolean firstJS = true;
    public static String lastGameUrl = "";
    private Object activityResultKeepRunning;
    private Object keepRunning;
    public static int timeInSec = 0;
    private TextView timer;

    private ImageButton skipBtn,stopBtn,exitBtn;

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

        stopBtn = (ImageButton)findViewById(R.id.pausegame);
        skipBtn = (ImageButton)findViewById(R.id.skipgame);
        exitBtn = (ImageButton)findViewById(R.id.exitplalistbtn);
        stopBtn.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        StartThisClock();

            gameAct.this.

            runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    mainView = (WebView) findViewById(R.id.mainView);
                    mainView.getSettings().setJavaScriptEnabled(true);
                    mainView.setBackgroundColor(Color.WHITE);
                    mainView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            //Log.d("url", url);
                            return false;
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            //Log.d("url", "Page started");
                        }

                        @Override
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            super.onReceivedError(view, errorCode, description, failingUrl);
                            //Log.d("url", description);
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
            }

            );

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
    private void gameSkipped(){
        Log.d("gameAct","gameSkipped");
        firstJS = false;
        lastGameUrl = playlistInfo.mPlaylist.get(playlistInfo.indexInList).getUrl();
        ratingAct.total_score = "0";
        ratingAct.total_time = "0";
        ratingAct.timeInSec = timeInSec;
        startActivity(new Intent(getApplicationContext(), ratingAct.class));
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
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting Playlist")
                .setMessage("Are you sure you want to exit the playlist?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lastGameUrl = "";
                        startActivity(new Intent(getApplicationContext(), waitingcompleteAct.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();
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
            case R.id.exitplalistbtn:
                onBackPressed();
                break;
            case R.id.skipgame:
                gameSkipped();
                break;
        }
    }
    private void PauseGame() {
        mainView.onPause();
        stopBtn.setImageResource(R.drawable.resumegame);
        pause = false;
    }
    private void ResumeGame() {
        mainView.onResume();
        stopBtn.setImageResource(R.drawable.finalstop);
        pause = true;

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
            stopBtn.setImageResource(R.drawable.finalstop);
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

        //finish();
        Log.d("url", "onDestroy");
    }


    public Object onMessage(String id, Object data) {
        //Log.d("gameAct", "onMessage(" + id + "," + data + ")");
        mainView.reload();
        if ("exit".equals(id)) {
            super.finish();
        }
        return null;
    }
}
