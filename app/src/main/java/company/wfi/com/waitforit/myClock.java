package company.wfi.com.waitforit;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

public class myClock {
    private static boolean firstTime = true;
    public static int timeLeftSec;
    public static Thread myThread;
    public static void StartClock(int TimeInMinutes) {
        timeLeftSec = TimeInMinutes * 60;
        Thread thread = new Thread( new Thread(new Runnable() {
            @Override
            public void run() {
                while(timeLeftSec > 0) {
                    try {
                        Thread.sleep(1000);
                        timeLeftSec -= 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(timeLeftSec == 0) {
                    firstTime = true;
                    Log.d("myClock", "Clock ended");
                }
            }
        }));

        if(myClock.firstTime){
            myClock.firstTime = false;
            thread.start();
        }
    }
    public static void stopThread(Context context){
    }
    public int getTimeLeft(){
        return timeLeftSec;
    }
    public void setTimeLeft(int TimeInMinutes){
        timeLeftSec = TimeInMinutes*60;
    }
    public static String getTimeText(){
        if (timeLeftSec / 60 >= 10) {
            if (timeLeftSec % 60 >= 10)
                return String.valueOf(timeLeftSec / 60 + ":" + timeLeftSec % 60);
            else
                return String.valueOf(timeLeftSec / 60 + ":0" + timeLeftSec % 60);
        } else {
            if (timeLeftSec % 60 >= 10)
                return "0" + String.valueOf(timeLeftSec / 60 + ":" + timeLeftSec % 60);
            else
                return "0" + String.valueOf(timeLeftSec / 60 + ":0" + timeLeftSec % 60);
        }
    }
}
