package company.wfi.com.waitforit;

/**
 * Created by User on 19/10/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private boolean isClicked = false;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);
        ((LinearLayout)findViewById(R.id.bgsplashid)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(splash.this,firstAct.class);
                isClicked = true;
                splash.this.startActivity(mainIntent);
                splash.this.finish();
            }
        });

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(!isClicked) {
                    Intent mainIntent = new Intent(splash.this, firstAct.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    @Override
    public void onBackPressed() {
    }
}