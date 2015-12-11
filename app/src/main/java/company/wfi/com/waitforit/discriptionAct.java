package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class discriptionAct extends Activity implements View.OnClickListener {
    ImageButton playgame,settingpage;
    private static String gameTitleStr,gameDiscriptionStr;
    private final String requestURL = "http://wait4itapp.com/";
    public static String currentGameId = "1243";
    private final String getRequest = "http://www.wait4itapp.com/webservice/game_request.php?id=";
    public static int timeInSec = 0;
    private TextView timer;
    private static final int toMINUTE = 1000;
    TextView gameTitle,gamediscription;
    ImageView gamePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAsyncTask mAsyncTask = (company.wfi.com.waitforit.mAsyncTask) new mAsyncTask(new mAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(JSONObject object) {
                try {
                    String name = object.getString("name");
                    name = name.replaceAll("\r\n", "");
                    String instruction = object.getString("instruction");
                    instruction = instruction.replaceAll("\r\n", "");
                    String pic = object.getString("pic");
                    pic = pic.replaceAll("\r\n", "");
                    pic = pic.replace("\\", "/");
                    gameTitleStr = name;
                    gameDiscriptionStr = instruction;
                    mAsyncTaskforBitmap mAsyncTaskforBitmap = (mAsyncTaskforBitmap)new mAsyncTaskforBitmap(new mAsyncTaskforBitmap.AsyncResponse(){
                        @Override
                        public void processFinish(Bitmap bitmap) {
                            gamePic.setImageBitmap(bitmap);
                            gamePic.setVisibility(View.VISIBLE);
                            playgame.setVisibility(View.VISIBLE);
                            playgame.setClickable(true);
                            gameTitle.setText(gameTitleStr);
                            gamediscription.setText(gameDiscriptionStr);
                        }
                    }).execute(requestURL + pic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(getRequest+currentGameId);
        setContentView(R.layout.gamediscription);

        //Typeface fontTitle = Typeface.createFromAsset(getAssets(),"fonts/Dense-Regular.otf");
        Typeface fontTimer = Typeface.createFromAsset(getAssets(),"fonts/Ailerons.ttf");

        timer = (TextView) findViewById(R.id.timertxt);
        playgame = (ImageButton) findViewById(R.id.playgamebutton);
        settingpage = (ImageButton) findViewById(R.id.exitplaylistbtn);
        gameTitle = (TextView)findViewById(R.id.gametitle);
        timer.setTypeface(fontTimer);
        gamediscription = (TextView)findViewById(R.id.discription);
        gamePic = (ImageView)findViewById(R.id.gameimage);
        playgame.setOnClickListener(this);
        settingpage.setOnClickListener(this);

        StartThisClock();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.playgamebutton:
                String currentUrl = playlistInfo.mPlaylist.get(playlistInfo.indexInList).getUrl();
                gameAct.timeInSec = timeInSec;
                Intent intent = new Intent(getApplicationContext(),gameAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                gameAct.loadNewGame = true;
                gameAct.firstJS = true;
                startActivity(intent);
                finish();
                break;
            case R.id.exitplaylistbtn:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exiting Playlist")
                        .setMessage("Are you sure you want to exit the playlist?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(),waitingcompleteAct.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled.",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
