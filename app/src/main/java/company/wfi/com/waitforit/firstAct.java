package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by User on 17/10/2015.
 */
public class firstAct extends Activity implements View.OnClickListener {

    ImageButton wfi,google,facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);

        wfi = (ImageButton)findViewById(R.id.wfibutton);
        facebook = (ImageButton)findViewById(R.id.facebookbutton);
        google = (ImageButton)findViewById(R.id.googlebutton);
        wfi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wfibutton:
                startActivity(new Intent(this,devPage.class));
                break;
            case R.id.googlebutton:
                Toast.makeText(this,"Logging through Google",Toast.LENGTH_SHORT).show();
                break;
            case R.id.facebookbutton:
                Toast.makeText(this,"Logging through Facebook",Toast.LENGTH_SHORT).show();
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
