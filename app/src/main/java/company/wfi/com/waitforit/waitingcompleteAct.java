package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class waitingcompleteAct extends Activity implements View.OnClickListener {

    ImageButton continuewfi,sharefacebook,ratebtn,set;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(),"Thank You!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                if(!Internet.isNetworkAvailable(getApplicationContext()))
                    Internet.ShowDialog(getApplicationContext());
            }
        });
        setContentView(R.layout.waitingcompletepage);

        continuewfi = (ImageButton)findViewById(R.id.conwaiting);
        sharefacebook = (ImageButton)findViewById(R.id.sharebutton);
        ratebtn = (ImageButton)findViewById(R.id.ratebutton);
        set = (ImageButton)findViewById(R.id.settingbutton);
        set.setOnClickListener(this);
        continuewfi.setOnClickListener(this);
        sharefacebook.setOnClickListener(this);
        ratebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        switch (view.getId()){
            case R.id.settingbutton:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivity", getClass().getName());
                editor.commit();
                startActivity(new Intent(this,settingcl.class));
                break;
            case R.id.ratebutton:
                Toast.makeText(this,"Thank you! But this is only a beta version",Toast.LENGTH_SHORT).show();
                //goRating(this);
                break;
            case R.id.sharebutton:
                Toast.makeText(this,"Sharing through Facebook..",Toast.LENGTH_SHORT).show();
                ShareOnFacebook();
                break;
            case R.id.conwaiting:
                startActivity(new Intent(this, waitingtime.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled here.",Toast.LENGTH_SHORT).show();
    }
    private void ShareOnFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Wait For It")
                    .setContentDescription(
                            "Be exposed to the most trending content with - Wait For It!")
                    .setContentUrl(Uri.parse("https://www.facebook.com/waitforitapp/?fref=ts"))
                    .setImageUrl(Uri.parse("http://s4.postimg.org/tu95fe18p/wfilogo.png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }
    public void goRating(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
        //TODO(later) check if this function sends you the user to the right page.
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //waitingDetails.ResetCategoryArray();
        videopage.timeInSec = 0;
        gameAct.timeInSec = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        playlistInfo.StopPlaylist();
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
