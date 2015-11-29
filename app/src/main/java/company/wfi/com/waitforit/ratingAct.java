package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 05/11/2015.
 */
public class ratingAct extends Activity implements View.OnClickListener {

    ImageButton sababa,notSababa,goSetting,otherView;
    Thread thread;


    public static int timeInSec = 0;
    private TextView timer;
    private static final int toMINUTE = 1000;
    //Other View variable contains the reference to the button that needs to be gone slowly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingpage);
        timer = (TextView)findViewById(R.id.timertxt);
        sababa = (ImageButton)findViewById(R.id.sabababutton);
        notSababa = (ImageButton)findViewById(R.id.notsabababutton);
        goSetting = (ImageButton)findViewById(R.id.backbuttoninrating);
        sababa.setOnClickListener(this);
        notSababa.setOnClickListener(this);
        goSetting.setOnClickListener(this);
        StartTimer();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sabababutton:
                sababa.setImageResource(R.drawable.mysababa);
                otherView = notSababa;
                otherView.setImageAlpha(50);
                //TODO(later) make a http request as sababa of the video/game
                break;
            case R.id.notsabababutton:
                notSababa.setImageResource(R.drawable.mynotsababa);
                otherView = sababa;
                otherView.setImageAlpha(50);
                //TODO(later) make a http request as not sababa of the video/game
                break;
            case R.id.backbuttoninrating:
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
        if(view.getId() != R.id.backbuttoninrating) {
            if(timeInSec > 15) {
                discriptionAct.timeInSec = timeInSec - 1;
                gameAct.timeInSec = timeInSec - 1;
                myAsyncTask myTask = new myAsyncTask();
                myTask.doInBackground();
            }
            else{
                startActivity(new Intent(getApplicationContext(), waitingcompleteAct.class));
            }
        }
            //for(otherView.setImageAlpha(255);otherView.getImageAlpha() >= 30;otherView.setImageAlpha(otherView.getImageAlpha() - 10)) {
            //   myTask.doInBackground(otherView.getImageAlpha());
            //}
            //thisView = (FrameLayout.LayoutParams)view.getLayoutParams();
            //thisView.height = view.getHeight() +20;
            //thisView.width = view.getWidth() +10;
            //view.setLayoutParams(thisView);
            //startActivity(new Intent(this, gameAct.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        sababa.setImageAlpha(255);
        notSababa.setImageAlpha(255);
        sababa.setImageResource(R.drawable.sababa);
        notSababa.setImageResource(R.drawable.notsababa);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled here.",Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getApplicationContext(), discriptionAct.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
