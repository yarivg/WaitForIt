package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ratingAct extends Activity implements View.OnClickListener {

    ImageButton sababa,notSababa,goSetting,otherView;
    SharedPreferences prefs;
    String rateValue;
    final String urlReqest = "http://www.wait4itapp.com/webservice/mark_object.php?";
    final String objectId = "object_id";
    final String userId= "user_id";
    final String likeStatus = "like_status";
    final String mTotalTime = "total_time";
    final String mTotalScore = "total_score";

    public static String total_time;
    public static String total_score;
    public static int timeInSec = 0;
    private TextView timer;
    private static final int toMINUTE = 1000;
    private long mLastClickTime = 0;
    //Other View variable contains the reference to the button that needs to be gone slowly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingpage);
        timer = (TextView)findViewById(R.id.timertxt);
        sababa = (ImageButton)findViewById(R.id.sabababutton);
        notSababa = (ImageButton)findViewById(R.id.notsabababutton);
        goSetting = (ImageButton)findViewById(R.id.exitplaylistbtn);
        sababa.setOnClickListener(this);
        notSababa.setOnClickListener(this);
        goSetting.setOnClickListener(this);
        Typeface fontTimer = Typeface.createFromAsset(getAssets(),"fonts/Ailerons.ttf");
        timer.setTypeface(fontTimer);
        StartThisClock();
    }
    @Override
    public void onClick(View view) {
        //Preventing multiple clicks,Using hold of 1 second
        if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return;
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (view.getId()){
            case R.id.sabababutton:
                notSababa.setClickable(false);
                rateValue = "1";
                sababa.setImageResource(R.drawable.mysababa);
                otherView = notSababa;
                otherView.setImageAlpha(50);
                break;
            case R.id.notsabababutton:
                sababa.setClickable(false);
                rateValue = "-1";
                notSababa.setImageResource(R.drawable.mynotsababa);
                otherView = sababa;
                otherView.setImageAlpha(50);
                break;
            case R.id.backbuttoninrating:
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
        if(view.getId() != R.id.backbuttoninrating) {
            SendRateToServer();
            if(myClock.timeLeftSec > 15) {
                discriptionAct.timeInSec = timeInSec;
                gameAct.timeInSec = timeInSec;
                videopage.timeInSec = timeInSec ;
                playlistInfo.NextInPlaylist(this);
            }
            else{
                Intent i = new Intent(getApplicationContext(),waitingcompleteAct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            }
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

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void SendRateToServer(){
        prefs = getSharedPreferences("X",MODE_PRIVATE);
        String mUserId = prefs.getString("UserId", "0");
        String mObjectId = playlistInfo.mPlaylist.get(playlistInfo.indexInList).getId();
        String fullRequest = urlReqest + objectId + "=" + mObjectId + "&" + userId + "=" + mUserId + "&" + likeStatus + "=" + rateValue + "&" + mTotalTime + "=" + total_time + "&" + mTotalScore + "=" + total_score;
        mAsyncTaskforString mAsyncTastforString = (company.wfi.com.waitforit.mAsyncTaskforString) new mAsyncTaskforString(new mAsyncTaskforString.AsyncResponse() {
            @Override
            public void processFinish(String str) {
                prefs = getSharedPreferences("X",MODE_PRIVATE);
                String mUserId = prefs.getString("UserId", "-1");
                if(mUserId.equals(str)){
                    //Log.d("ratingAct","Send Rating Succeed.");
                }
            }
        }).execute(fullRequest);
    }
    @Override
    public void onBackPressed() {
        //Toast.makeText(this,"Back button is currently disabled here.",Toast.LENGTH_SHORT).show();
    }
    public void StartTimer(){
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
            }
            public void onFinish() {
                timer.setText("00:00");
            }
        }.start();
    }
    public class myAsyncTask extends AsyncTask<Void,Integer,Integer> {
        public myAsyncTask() {
            super();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            publishProgress(50);
            return 50;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //Change USer Interface
            super.onPostExecute(integer);
        }
        @Override
        protected void onProgressUpdate(Integer... alpha) {
            super.onProgressUpdate(alpha);
            try {
                Thread.sleep(1000);
                Intent i = new Intent(getApplicationContext(),discriptionAct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
