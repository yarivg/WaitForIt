package company.wfi.com.waitforit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class UserData {
    private static final String TAG = "UserData";
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;
    public static void SaveUserData(Context context,String token,String id,String name,String email,String birthday,List<String> languages){
        prefs =  context.getSharedPreferences("X", context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("UserToken", token);
        editor.putString("UserId", id);
        editor.putString("UserName",name);
        if(email.contains("@"))
            editor.putString("UserEmail",email);
        else
            editor.putString("UserEmail","nomail");
        if(!birthday.equals("-1"))
            editor.putString("UserBirthday",birthday);
        else
            editor.putString("UserBirthday","-1");
        editor.putInt("Session", 2);
        editor.commit();
    }
    public static void LogOutData(Context context){
        prefs =  context.getSharedPreferences("X", context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.remove("UserToken");
        editor.remove("UserId");
        editor.remove("UserName");
        editor.remove("UserEmail");
        editor.remove("UserBirthday");
        if(prefs.getInt("Session",0) == 1){
            //Google
            firstAct.googleApiClient.disconnect();
            Log.d(TAG,"logged out from google");
        }
        if(prefs.getInt("Session",0) == 2){
            //Facebook
            LoginManager.getInstance().logOut();
            Log.d(TAG, "logged out from facebook");
        }
        editor.putInt("Session", 0);
        editor.commit();
    }
}
