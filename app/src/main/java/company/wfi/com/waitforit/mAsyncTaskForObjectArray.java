package company.wfi.com.waitforit;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class mAsyncTaskForObjectArray extends AsyncTask<String, String, JSONArray> {
    /*Params-
        (first)The doInBackground gets it.
        (second)onProgressDialog gets it.
        (third)doInBackground return it to onPostExecute.
    */

    /*An interface to get the response of onPostExecute*/
    public interface AsyncResponse {
        void processFinish(JSONArray array);
    }

    public AsyncResponse delegate = null;

    public mAsyncTaskForObjectArray(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private String result = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray array = null;
        String mURL = params[0];
        try {
            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
            //put output stream into a string
            BufferedReader br = new BufferedReader(isReader);
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("mAsyncTask", line);
                result += line;
            }
            result = result.replace("[", "");
            result = result.replace("]", "");
            result = "[" + result + "]";
            result = result.replaceAll("<pre>", "");
            result = result.replaceAll("</pre>","");

            array = new JSONArray(result);
            } catch (IOException e) {
            e.printStackTrace();
            Log.e("WAITFORIT", "ERROR AT CONNECTION");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("WAITFORIT", "ERROR AT parsing string to jsonobjcet");
        }
        return array;
    }

    @Override
    protected void onPostExecute(JSONArray array) {
        //super.onPostExecute(object);
        delegate.processFinish(array);
    }
}