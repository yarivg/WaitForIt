package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 17/10/2015.
 */
public class discriptionAct extends Activity implements View.OnClickListener {
    //TODO(later) change this page and get pic of a game
    ImageButton playgame,settingpage;
    public static int timeInSec = 0;
    private TextView timer;
    private static final int toMINUTE = 1000;
    ImageView gameTitle,gamediscription,gamePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamediscription);

        timer = (TextView) findViewById(R.id.timertxt);
        playgame = (ImageButton) findViewById(R.id.playgamebutton);
        settingpage = (ImageButton) findViewById(R.id.exitdisxription);
        playgame.setOnClickListener(this);
        settingpage.setOnClickListener(this);
        StartTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.playgamebutton:
                gameAct.timeInSec = timeInSec - 1;
                //TODO(later) send the user to the right game
                startActivity(new Intent(this,gameAct.class));
                break;
            case R.id.exitdisxription:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exiting Playlist")
                        .setMessage("Are you sure you want to exit the playlist?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), waitingcompleteAct.class));
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
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
