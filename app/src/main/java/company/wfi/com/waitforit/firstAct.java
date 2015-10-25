package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

/**
 * Created by User on 17/10/2015.
 */
public class firstAct extends Activity implements View.OnClickListener {

    ImageButton wfi,google,facebook,set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.firstscreen);

        wfi = (ImageButton)findViewById(R.id.wfibutton);
        facebook = (ImageButton)findViewById(R.id.facebookbutton);
        google = (ImageButton)findViewById(R.id.googlebutton);
        set = (ImageButton)findViewById(R.id.setbtn);
        set.setOnClickListener(this);
        wfi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wfibutton:
                startActivity(new Intent(this,devPage.class));
                break;
            case R.id.googlebutton:
                startActivity(new Intent(this, categorypage.class));
                //TODO login with google and update database
                break;
            case R.id.facebookbutton:
                Toast.makeText(this,"Logging through Facebook",Toast.LENGTH_SHORT).show();
                //TODO login with facebook and update database
                break;
            case R.id.setbtn:
                startActivity(new Intent(this, settingcl.class));
                break;
        }
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
