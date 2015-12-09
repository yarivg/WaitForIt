package company.wfi.com.waitforit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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


}
