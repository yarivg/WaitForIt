package company.wfi.com.waitforit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class firstAct extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
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

    public static boolean CameFromLogOut = false;
    private final String urlRequest = "http://wait4itapp.com/webservice/social_auth.php?";
    private final String mType = "type";
    private final String mId = "id";
    private final String mToken = "token";
    private final String mFullName = "fullname";
    private final String mEmail = "email";
    private final String mBirthday = "birthday";
    private final String mGender = "gender";
    private static final int RC_SIGN_IN = 9001;
    public static GoogleApiClient googleApiClient;

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

        mTokenTracker.startTracking();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //--------------------
        wfi = (ImageButton)findViewById(R.id.wfibutton);
        facebook = (ImageButton)findViewById(R.id.facebookbutton);
        set = (ImageButton)findViewById(R.id.setbtn);
        set.setOnClickListener(this);
        wfi.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(new Scope(Scopes.PLUS_LOGIN)).requestProfile()
                .build();
        Log.d("firstAct","onCreate");
        if(!CameFromLogOut) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addConnectionCallbacks(this)
                    .build();
        }

        prefs = getSharedPreferences("X", MODE_PRIVATE);
        //Session
        session = prefs.getInt("Session", 0);
        if(session == 1){
            //Toast.makeText(this, String.valueOf("Connected Successfully through Google!"),Toast.LENGTH_SHORT).show();
            //googleApiClient.connect();
            startActivity(new Intent(this,categorypage.class));
            //finish();
        }
        else if(session == 2){
            //Toast.makeText(this, String.valueOf("Connected Successfully through Facebook!"),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,categorypage.class));
            //finish();
        }

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        setGooglePlusButtonText(signInButton, "Sign in with Google");
        
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(findViewById(R.id.facebookbutton)).setClickable(false);
                registerWay = "google";
                signInFromGitHub();
            }
        });
        //Internet.ShowScreenGroup(this);
    }
    private void signInFromGitHub()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("firstAct", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    //hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
            signIn();
        }
    }
    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15f);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wfibutton:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivityforDev", String.valueOf(firstAct.class.getName()));
                editor.apply();
                Intent i = new Intent(getApplicationContext(),devPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                break;
            case R.id.facebookbutton:
                //(findViewById(R.id.sign_in_button)).setClickable(false);
                registerWay = "facebook";
                loginOnClick();
                //Toast.makeText(this,"Logging with Facebook",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setbtn:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivity", String.valueOf(firstAct.class.getName()));
                editor.commit();
                Intent i2 = new Intent(getApplicationContext(),settingcl.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i2);
                //finish();
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
        if(!Internet.isNetworkAvailable(this)){
            Internet.ShowDialog(this);
        }
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
        //if(!CameFromLogOut)
        //    signInFromGitHub();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
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
        if(registerWay != null) {
            if (registerWay.equals("google")) {
                if (requestCode == RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                    (findViewById(R.id.facebookbutton)).setClickable(true);
                }
            } else if (registerWay.equals("facebook")) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
                (findViewById(R.id.sign_in_button)).setClickable(true);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        String idToken = "";
        if (result.isSuccess()) {
            googleApiClient.connect();
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String personToken = acct.getIdToken();
            prefs = getSharedPreferences("X", MODE_PRIVATE);
            editor = prefs.edit();
            editor.putString("UserName", personName);
            editor.putString("UserEmail", personEmail);
            editor.putString("UserId", personId);
            if (personToken != null)
                editor.putString("UserToken", personToken);
            else
                editor.putString("UserToken", "0");
            // TODO(user): send token to server and validate server-side
            editor.putInt("Session", 1);
            editor.commit();

            SendSignInDetails("google",personId,personToken,personName,personEmail,"0","0");
            //TODO get gender for google sign in

            //Toast.makeText(this, String.valueOf("Connected Successfully!"), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),categorypage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            //finish();
        } else {
            // Signed out
            googleApiClient.disconnect();
            //Toast.makeText(this, "Please sign in again", Toast.LENGTH_SHORT).show();
        }
    }
    private void SendSignInDetails(String site, String personId,String personToken,String personName,String personEmail,String personBirthday,String personGender){
        String myRequest = urlRequest+mType+"=" + site + "&" + mId + "=" + personId + "&" +mToken + "=" + personToken +"&" +mFullName +"=" + personName + "&" + mEmail +"=" +personEmail + "&" + mBirthday + "=" + personBirthday + "&" + mGender + "=" + personGender;
        myRequest = myRequest.replace(" ","_");
        mAsyncTaskforString mAsyncTaskforString = (company.wfi.com.waitforit.mAsyncTaskforString) new mAsyncTaskforString(new mAsyncTaskforString.AsyncResponse() {
            @Override
            public void processFinish(String str) {
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                String mUserID = prefs.getString("UserId","-1");
                if(str.equals("exists")){
                    //sign in complete

                }
                else if(str.equals(mUserID)){
                    //Registeration complete
                }
            }
        }).execute(myRequest);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("firstAct",connectionResult.toString());
    }
    private void loginOnClick(){
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                //Toast.makeText(getApplicationContext(), "Successfull login.", Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    String birthday, email;
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    try {
                                        email = object.getString("email");
                                    } catch (NullPointerException e) {
                                        email = "-1";
                                    } catch (JSONException e) {
                                        email = "-1";
                                    }
                                    try {
                                        birthday = object.getString("birthday");
                                    } catch (NullPointerException e) {
                                        birthday = "-1";
                                    } catch (JSONException e) {
                                        birthday = "-1";
                                    }
                                    SendSignInDetails("facebook", id, accessToken.getToken(), name, email, birthday, gender);
                                    UserData.SaveUserData(getApplicationContext(), String.valueOf(accessToken.getToken()), id, name, email, birthday, null);
                                    startActivity(new Intent(getApplicationContext(), categorypage.class));
                                    //finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                //startActivity(new Intent(getApplicationContext(), categorypage.class));
            }

            @Override
            public void onCancel() {
                // App code
                //Toast.makeText(getApplicationContext(), "Login was cancelled.", Toast.LENGTH_SHORT).show();
                UserData.LogOutData(getApplicationContext());
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(firstAct.this, Arrays.asList("public_profile", "email", "user_birthday"));
                    //TODO check this thing again..
                }
                //Log.e("Login Error : ",exception.toString());
                //Toast.makeText(getApplicationContext(), "Exeption: " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        //UserData.LogOutData(getApplicationContext());
                    }
                });
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //TODO Save to shared preferences
            }
        };
    }
    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("firstAct",String.valueOf(googleApiClient.isConnected()));
        if (CameFromLogOut) {
            signOut();
            Log.d("firstAct",String.valueOf(googleApiClient.isConnected()));
            //googleApiClient.disconnect();
            CameFromLogOut = false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("firstAct",String.valueOf(i));
    }
}