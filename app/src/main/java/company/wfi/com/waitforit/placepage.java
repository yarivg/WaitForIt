package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.FacebookRequestError;

/**
 * Created by User on 17/10/2015.
 */
public class placepage extends Activity implements View.OnClickListener {

    ImageButton next,prev,settingg,bus,goverment,airport,park,train,clinic,restaurent,postoffice,vehicle,others;
    private int Choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseplace);

        next = (ImageButton)findViewById(R.id.nextbutton);
        prev = (ImageButton)findViewById(R.id.previousbutton);
        settingg = (ImageButton)findViewById(R.id.settings1);
        bus = (ImageButton)findViewById(R.id.bus);
        goverment = (ImageButton)findViewById(R.id.goverment);
        airport = (ImageButton)findViewById(R.id.airport);
        park = (ImageButton)findViewById(R.id.park);
        train = (ImageButton)findViewById(R.id.train);
        clinic = (ImageButton)findViewById(R.id.clinic);
        restaurent = (ImageButton)findViewById(R.id.restaurent);
        postoffice = (ImageButton)findViewById(R.id.postoffice);
        vehicle = (ImageButton)findViewById(R.id.vehicle);
        others = (ImageButton)findViewById(R.id.others);


        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        settingg.setOnClickListener(this);
        bus.setOnClickListener(this);
        goverment.setOnClickListener(this);
        airport.setOnClickListener(this);
        park.setOnClickListener(this);
        train.setOnClickListener(this);
        clinic.setOnClickListener(this);
        restaurent.setOnClickListener(this);
        postoffice.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        others.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        FrameLayout.LayoutParams thisView;
        switch (view.getId()){
            case R.id.bus:
                waitingtime.waitingPlace = 0;
                ResizeImage(view);
                break;
            case R.id.goverment:
                waitingtime.waitingPlace = 1;
                ResizeImage(view);
                break;
            case R.id.airport:
                waitingtime.waitingPlace = 2;
                ResizeImage(view);
                break;
            case R.id.park:
                waitingtime.waitingPlace = 3;
                ResizeImage(view);
                break;
            case R.id.train:
                waitingtime.waitingPlace = 4;
                ResizeImage(view);
                break;
            case R.id.clinic:
                waitingtime.waitingPlace = 5;
                ResizeImage(view);
                break;
            case R.id.restaurent:
                waitingtime.waitingPlace = 6;
                ResizeImage(view);
                break;
            case R.id.postoffice:
                waitingtime.waitingPlace = 7;
                ResizeImage(view);
                break;
            case R.id.vehicle:
                waitingtime.waitingPlace = 8;
                ResizeImage(view);
                break;
            case R.id.others:
            case R.id.nextbutton:
                if(view.getId() == R.id.others) ResizeImage(view);
                else{
                    Intent mIntent = new Intent(this, waitingtime.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                }
                break;
            case R.id.settings1:
                SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastActivity", String.valueOf(placepage.class.getName()));
                editor.apply();
                startActivity(new Intent(this,settingcl.class));
                break;
            case R.id.previousbutton:
                Intent mIntent = new Intent(this, categorypage.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                break;
            }
    }
    private void ResizeImage(View view){
        FrameLayout.LayoutParams thisView = (FrameLayout.LayoutParams)view.getLayoutParams();
        thisView.height = view.getHeight() +15;
        thisView.width = view.getWidth() +15;
        view.setLayoutParams(thisView);

        Intent mIntent = new Intent(this, waitingtime.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled.",Toast.LENGTH_SHORT).show();
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
