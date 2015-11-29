package company.wfi.com.waitforit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Internet {
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void ShowDialog(final Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No Internet Connection");
// Add the buttons
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!Internet.isNetworkAvailable(context)) {
                    builder.show();
                }
            }
        });
// Create the AlertDialog
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void ShowScreenGroup(Context context){
        float density = context.getResources().getDisplayMetrics().density;
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(context,"HDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(context,"xHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Toast.makeText(context,"xxHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Toast.makeText(context,"xxxHDPI",Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_560:
                Toast.makeText(context,"560DPI",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, String.valueOf(context.getResources().getDisplayMetrics().densityDpi), Toast.LENGTH_SHORT).show();
        }
    }

    public static JSONObject getJSONofURL(String mUrl){
        JSONObject object = null;
        String result = "";
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
            //put output stream into a string
            BufferedReader br = new BufferedReader(isReader );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("mAsyncTask", line);
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{//Converting result String to JSON OBJECT
            object = new JSONObject(result);

        }catch(JSONException e){
            e.printStackTrace();
            Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
        }
        return object;
    }
}
