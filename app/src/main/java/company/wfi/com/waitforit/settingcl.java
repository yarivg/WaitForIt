package company.wfi.com.waitforit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class settingcl extends Activity implements View.OnClickListener {

    int session;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Boolean sessionUser;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    ImageButton about,rateUs,facebookShare,setDefaultTime,contactUs,backClick,logoutbtn;
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
                Toast.makeText(getApplicationContext(),"Thank You for sharing out great work!",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.settinglayout);

        about = (ImageButton)findViewById(R.id.aboutbtn);
        rateUs = (ImageButton)findViewById(R.id.rateusbtn);
        facebookShare = (ImageButton)findViewById(R.id.sharefacebookbtn);
        setDefaultTime = (ImageButton)findViewById(R.id.settimebtn);
        contactUs = (ImageButton)findViewById(R.id.contactusbtn);
        backClick = (ImageButton)findViewById(R.id.backbtn1);
        logoutbtn = (ImageButton)findViewById(R.id.logoutbtn);
        about.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        facebookShare.setOnClickListener(this);
        setDefaultTime.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        backClick.setOnClickListener(this);
        logoutbtn.setOnClickListener(this);
        prefs = getSharedPreferences("X", MODE_PRIVATE);
        //Session
        session = prefs.getInt("Session",0);//if 0 than he is not logged in.other the user is logged in.
        if(session == 0){
            logoutbtn.setClickable(false);
            logoutbtn.setVisibility(View.INVISIBLE);
        }
        else{
            logoutbtn.setClickable(true);
            logoutbtn.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aboutbtn:
                startActivity(new Intent(this,devPage.class));
                break;
            case R.id.rateusbtn:
                Toast.makeText(this,"Thank you! But this is only a beta version",Toast.LENGTH_SHORT).show();
                //goRating(this);
                break;
            case R.id.sharefacebookbtn:
                ShareOnFacebook();
                Toast.makeText(this, "Sharing through facebook...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settimebtn:
                startActivity(new Intent(this, setdefulattimeact.class));
                break;
            case R.id.contactusbtn:
                Toast.makeText(this, "Contact us...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Servicewfi@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backbtn1:
                Class<?> activityClass;
                try {
                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                    activityClass = Class.forName(
                            prefs.getString("lastActivity", settingcl.class.getName()));

                    SharedPreferences.Editor e = prefs.edit();
                    e.remove("lastActivity");
                    e.commit();
                } catch(ClassNotFoundException ex) {
                    activityClass = settingcl.class;
                }
                startActivity(new Intent(this, activityClass));
                break;
            case R.id.logoutbtn:
                ShowDialog("Are you sure you want to log out?", "Yes", "No");
        }
    }
    public void ShowDialog(String titleMessage,String posbuttonMessage,String negbuttonMessage){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(titleMessage);
// Add the buttons
        builder.setPositiveButton(posbuttonMessage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UserData.LogOutData(getApplicationContext());
                finish();
                startActivity(new Intent(getApplicationContext(),firstAct.class));
            }
        });
        builder.setNegativeButton(negbuttonMessage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void goRating(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void ShareOnFacebook(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Wait For It app")
                    .setContentDescription(
                            "Be exposed to the most trending content with - Wait For It!")
                    .setContentUrl(Uri.parse("https://www.facebook.com/waitforitapp/?fref=ts"))
                    .setImageUrl(Uri.parse("http://s4.postimg.org/tu95fe18p/wfilogo.png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

