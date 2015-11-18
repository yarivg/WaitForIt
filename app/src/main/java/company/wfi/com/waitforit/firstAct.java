package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

public class firstAct extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    String registerWay;
    ImageButton google;
    ImageButton facebook;
    ImageButton set;
    ImageButton wfi;
    int session;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "My Login App";
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.firstscreen);


        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        callbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //--------------------
        wfi = (ImageButton)findViewById(R.id.wfibutton);
        facebook = (ImageButton)findViewById(R.id.facebookbutton);
        google = (ImageButton)findViewById(R.id.googlebutton);
        set = (ImageButton)findViewById(R.id.setbtn);
        set.setOnClickListener(this);
        wfi.setOnClickListener(this);
        prefs = getSharedPreferences("X", MODE_PRIVATE);
        //Session
        session = prefs.getInt("Session",0);
        if(session != 0){
            //When the user is logged in
            Toast.makeText(this, String.valueOf("Connected Successfully through Google!"),Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this,categorypage.class));
            //TODO return after I finish to check session
            //finish();
        }

        //TODO logout app makes session zero
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(new Scope(Scopes.PLUS_LOGIN)).requestIdToken(getString(R.string.server_client_id))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWay = "google";
                signIn();
            }
        });
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wfibutton:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivityforDev", String.valueOf(firstAct.class.getName()));
                editor.commit();
                startActivity(new Intent(this,devPage.class));
                break;
            case R.id.googlebutton:
                break;
            case R.id.facebookbutton:
                registerWay = "facebook";
                loginOnClick();
                Toast.makeText(this,"Logging through Facebook",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this, categorypage.class));
                //TODO(later) login with facebook and update database
                break;
            case R.id.setbtn:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivity", String.valueOf(firstAct.class.getName()));
                editor.commit();
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
        if(!isNetworkAvailable(this)){
            ShowDialog();
        }
    }
    @Override
    public void onBackPressed() {
    }
    private void ShowDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No Internet Connection");
// Add the buttons
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!isNetworkAvailable(getApplicationContext())) {
                    builder.show();
                }
            }
        });
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(registerWay.equals("google")) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
        else if(registerWay.equals("facebook")){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            //acct.geteverything
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String personToken = acct.getIdToken();
            prefs = getSharedPreferences("X", MODE_PRIVATE);
            editor = prefs.edit();
            editor.putString("UserName",personName);
            editor.putString("UserEmail",personEmail);
            editor.putString("UserId",personId);
            editor.putString("UserPhoto", String.valueOf(personPhoto));
            editor.putString("UserToken", personToken);
            editor.putInt("Session",1);
            editor.commit();
            //String x1 = acct.zzmw();
            //Set<Scope> myScopes = acct.getGrantedScopes();//2,4
            Toast.makeText(this, String.valueOf("Connected Successfully!"),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, categorypage.class));

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            Toast.makeText(this,"ID Token: null",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void loginOnClick(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                Toast.makeText(getApplicationContext(), "Successfull login.", Toast.LENGTH_SHORT).show();
//                ((TextView)findViewById(R.id.ftoken)).setText(String.valueOf(accessToken));
//                ((TextView)findViewById(R.id.fid)).setText(String.valueOf(accessToken.getUserId()));
//                ((TextView)findViewById(R.id.fname)).setText(String.valueOf(profile.getName()));

                /* make the API call */
                new GraphRequest(
                        accessToken,
                        "/me",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                                try {
                                    JSONArray arr = response.getJSONArray();
                                    JSONObject jObj = arr.getJSONObject(0);
                                    String date = jObj.getString("id");
                                } catch (JSONException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
                Toast.makeText(getApplicationContext(),"Facebook login succedd",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), categorypage.class));

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "Login was cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Exeption: " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("VIVZ", "" + currentAccessToken);
                //TODO Save to shared preferences
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("VIVZ", "" + currentProfile);
                //Save to shared preferences
            }
        };
    }
}