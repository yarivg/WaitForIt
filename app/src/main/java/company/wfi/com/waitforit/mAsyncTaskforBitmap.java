package company.wfi.com.waitforit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class mAsyncTaskforBitmap extends AsyncTask<String,String,Bitmap> {
    /*Params-
        (first)The doInBackground gets it.
        (second)onProgressDialog gets it.
        (third)doInBackground return it to onPostExecute.
    */

    /*An interface to get the response of onPostExecute*/
    public interface AsyncResponse {
        void processFinish(Bitmap bitmap);
    }

    public AsyncResponse delegate = null;

    public mAsyncTaskforBitmap(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private String result = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Internet", "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //super.onPostExecute(object);
        delegate.processFinish(bitmap);
    }
}
