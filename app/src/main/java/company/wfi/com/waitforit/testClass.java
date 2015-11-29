package company.wfi.com.waitforit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class testClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        //Internet.getJSONofURL("http://www.wait4itapp.com/webservice/game_request.php?id=1243");
        mAsyncTask mAsyncTask = (company.wfi.com.waitforit.mAsyncTask) new mAsyncTask(new mAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(JSONObject object) {
                try {
                    String name = object.getString("name");
                    name = name.replaceAll("\r\n","");
                    String instruction = object.getString("instruction");
                    instruction = instruction.replaceAll("\r\n","");
                    String pic = object.getString("pic");
                    Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),instruction,Toast.LENGTH_SHORT).show();
                    Toast t = Toast.makeText(getApplicationContext(), pic, Toast.LENGTH_SHORT);
                    t.setGravity(0, 0, Gravity.CENTER);
                    t.setDuration(Toast.LENGTH_SHORT);
                    t.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("http://www.wait4itapp.com/webservice/game_request.php?id=1243");
    }
}
