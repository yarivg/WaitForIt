package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class devPage extends Activity implements View.OnClickListener {

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developerspage);

        back = (ImageButton)findViewById(R.id.backbtn);
        back.setOnClickListener(this);


        Class<?> activityClass;
        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(
                    prefs.getString("lastActivityforDev", settingcl.class.getName()));
            if(String.valueOf(activityClass).contains("firstAct")){
                back.setTag("firstAct");
                back.setImageResource(R.drawable.backtologin);
            //if the user came from login page than login image shold appear. Otherwise setting image
            }
            else {
                back.setImageResource(R.drawable.settings);
                back.setTag("settings");
            }
                SharedPreferences.Editor e = prefs.edit();
            e.remove("lastActivityforDev");
            e.commit();
        } catch(ClassNotFoundException ex) {
            back.setImageResource(R.drawable.settings);
            back.setTag("settings");
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backbtn:
                if(back.getTag().equals("firstAct"))
                    startActivity(new Intent(this,firstAct.class));
                else
                    startActivity(new Intent(this,settingcl.class));
                finish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
