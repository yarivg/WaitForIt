package company.wfi.com.waitforit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class testClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        //Internet.getJSONofURL("http://www.wait4itapp.com/webservice/game_request.php?id=1243");
//        mAsyncTask mAsyncTask = (company.wfi.com.waitforit.mAsyncTask) new mAsyncTask(new mAsyncTask.AsyncResponse() {
//            @Override
//            public void processFinish(JSONObject object) {
//
//            }
//        }).execute("http://www.wait4itapp.com/webservice/game_request.php?id=1243");
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.googledrive.com/host/0ByndIaCDcs86U29Wam1NY091azg/index.html");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(1000); // mTimeout is in seconds

                    urlc.connect();

//                    if (urlc.getResponseCode() == 200) {
//                        return;
//                    } else {
//                        return;
//                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static void testMethod() throws IOException {

    }
}
