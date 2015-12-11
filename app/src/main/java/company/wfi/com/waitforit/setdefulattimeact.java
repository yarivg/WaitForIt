package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by User on 21/10/2015.
 */
public class setdefulattimeact extends Activity implements View.OnClickListener {

    public static boolean[] arr1 = new boolean[constants.NUMOFCATEGORIES];
    private static final int  SIZEOFARRAY = 14;
    private ImageButton minus,plus,imageTime,done,setting;
    private int index;
    private int[] arr = new int[SIZEOFARRAY];
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setdefaulttimepage);

        minus = (ImageButton)findViewById(R.id.minusbutton);
        plus = (ImageButton)findViewById(R.id.plusbutton);
        imageTime = (ImageButton)findViewById(R.id.waitingtime);
        done = (ImageButton)findViewById(R.id.donebtn);
        setting = (ImageButton)findViewById(R.id.settings);
        minus.setOnClickListener(this);
        minus.setVisibility(View.INVISIBLE);
        minus.setClickable(false);
        plus.setOnClickListener(this);
        imageTime.setOnClickListener(this);
        done.setOnClickListener(this);
        setting.setOnClickListener(this);
        index = 1;
        arr[0] = R.drawable.min1; arr[1] = R.drawable.min2; arr[2] = R.drawable.min3; arr[3] = R.drawable.min4;
        arr[4] = R.drawable.min5; arr[5] = R.drawable.min6; arr[6] = R.drawable.min7; arr[7] = R.drawable.min8;
        arr[8] = R.drawable.min9; arr[9] = R.drawable.min10; arr[10] = R.drawable.min15;
        arr[11] = R.drawable.min20; arr[12] = R.drawable.min25; arr[13] = R.drawable.min30;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.plusbutton:
                if(index == SIZEOFARRAY-1){
                    plus.setVisibility(View.INVISIBLE);
                    plus.setClickable(false);
                    index++;
                }
                else
                    index++;
                imageTime.setImageResource(arr[index-1]);
                minus.setVisibility(View.VISIBLE);
                minus.setClickable(true);
                break;
            case R.id.minusbutton:
                if(index == 2){
                    minus.setVisibility(View.INVISIBLE);
                    minus.setClickable(false);
                    index--;
                }
                else
                    index--;
                imageTime.setImageResource(arr[index-1]);
                plus.setVisibility(View.VISIBLE);
                plus.setClickable(true);
                break;
            case R.id.donebtn:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.remove("defaultTimeIndex");
                editor.apply();
                editor.putInt("defaultTimeIndex", index);
                editor.commit();
                //Toast.makeText(this,"Default Time Was Saved Successfully!",Toast.LENGTH_SHORT).show();
                if(prefs.getInt("Session",0) == 0) {
                    startActivity(new Intent(this, firstAct.class));
                }
                else{
                    Intent i2 = new Intent(getApplicationContext(),categorypage.class);
                    i2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i2);
                }
                finish();
            case R.id.waitingtime:
                break;
            case R.id.settings:
                Intent i = new Intent(getApplicationContext(),settingcl.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
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
