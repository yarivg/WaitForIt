package company.wfi.com.waitforit;

import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class mAsyncTask extends AsyncTask<String,String,JSONObject>{
    /*Params-
        (first)The doInBackground gets it.
        (second)onProgressDialog gets it.
        (third)doInBackground return it to onPostExecute.
    */

    /*An interface to get the response of onPostExecute*/
    public interface AsyncResponse{
        void processFinish(JSONObject object);
    }
    public AsyncResponse delegate = null;

    public mAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    private String result = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject object = null;
        String mURL = params[0];
        try {
            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
            //put output stream into a string
            BufferedReader br = new BufferedReader(isReader );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                //Log.d("mAsyncTask", line);
                result += line;
            }
            result = result.replace("[","");
            result = result.replace("]","");

            object = new JSONObject(result);
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e("WAITFORIT","ERROR AT CONNECTION");
        }catch (JSONException e){
            e.printStackTrace();
            //Log.e("WAITFORIT", "ERROR AT parsing string to jsonobjcet");
        }
        return object;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        //super.onPostExecute(object);
        delegate.processFinish(object);
    }
}
