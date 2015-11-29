package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by User on 17/10/2015.
 */
public class waitingtime extends Activity implements View.OnClickListener {

    public static boolean[] arr1 = new boolean[constants.NUMOFCATEGORIES];
    public static int waitingPlace;
    private static final int  SIZEOFARRAY = 14;
    private ImageButton minus,plus,imageTime,startWaiting,backclick,setting;
    private int index;
    private int[] arr2 = new int[SIZEOFARRAY];
    private int[] arr = new int[SIZEOFARRAY];
    private int[] arr3 = new int[SIZEOFARRAY];
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosewaitingtime);

        minus = (ImageButton)findViewById(R.id.minusbutton);
        plus = (ImageButton)findViewById(R.id.plusbutton);
        imageTime = (ImageButton)findViewById(R.id.waitingtime);
        startWaiting = (ImageButton)findViewById(R.id.startwaiting);
        backclick = (ImageButton)findViewById(R.id.previousbutton);
        setting = (ImageButton)findViewById(R.id.settings);
        minus.setOnClickListener(this);
        minus.setVisibility(View.INVISIBLE);
        minus.setClickable(false);
        plus.setOnClickListener(this);
        imageTime.setOnClickListener(this);
        startWaiting.setOnClickListener(this);
        backclick.setOnClickListener(this);
        setting.setOnClickListener(this);


        try{
        prefs = getSharedPreferences("X", MODE_PRIVATE);
        index = prefs.getInt("defaultTimeIndex", index);
        editor = prefs.edit();
        editor.commit();
        }catch (Exception e){
            index = 1;
        }

        arr2[0] = 1; arr2[1] = 2; arr2[2] = 3; arr2[3] = 4;
        arr2[4] = 5; arr2[5] = 6; arr2[6] = 7; arr2[7] = 8;
        arr2[8] = 9; arr2[9] = 10; arr2[10] = 15;
        arr2[11] = 20; arr2[12] = 25; arr2[13] = 30;

        arr[0] = R.drawable.min1; arr[1] = R.drawable.min2; arr[2] = R.drawable.min3; arr[3] = R.drawable.min4;
        arr[4] = R.drawable.min5; arr[5] = R.drawable.min6; arr[6] = R.drawable.min7; arr[7] = R.drawable.min8;
        arr[8] = R.drawable.min9; arr[9] = R.drawable.min10; arr[10] = R.drawable.min15;
        arr[11] = R.drawable.min20; arr[12] = R.drawable.min25; arr[13] = R.drawable.min30;

        arr3[0] = R.drawable.min1clicked; arr3[1] = R.drawable.min2clicked; arr3[2] = R.drawable.min3clicked; arr3[3] = R.drawable.min4clicked;
        arr3[4] = R.drawable.min5clicked; arr3[5] = R.drawable.min6clicked; arr3[6] = R.drawable.min7clicked; arr3[7] = R.drawable.min8clicked;
        arr3[8] = R.drawable.min9clicked; arr3[9] = R.drawable.min10clicked; arr3[10] = R.drawable.min15clicked;
        arr3[11] = R.drawable.min20clicked; arr3[12] = R.drawable.min25clicked; arr3[13] = R.drawable.min30clicked;
        if(index != 0)
           imageTime.setImageResource(arr[index-1]);
        else {imageTime.setImageResource(arr[index]); index = 1;}
        if(index == SIZEOFARRAY){
            plus.setVisibility(View.INVISIBLE);
            plus.setClickable(false);
            minus.setVisibility(View.VISIBLE);
            minus.setClickable(true);
        }
        else if(index == 1){
            minus.setVisibility(View.INVISIBLE);
            minus.setClickable(false);
            plus.setVisibility(View.VISIBLE);
            plus.setClickable(true);
        }
        else {
            minus.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
            minus.setClickable(true);
            plus.setClickable(true);

        }

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
                imageTime.setImageResource(arr[index - 1]);
                plus.setVisibility(View.VISIBLE);
                plus.setClickable(true);
                break;
            case R.id.previousbutton:
                startActivity(new Intent(this,placepage.class));
                break;
            case R.id.waitingtime:
            case R.id.startwaiting:
                if(view.getId() == R.id.waitingtime) imageTime.setImageResource(arr3[index - 1]);
                Toast.makeText(this,"We Successfully Craeted Your Playlist!",Toast.LENGTH_SHORT).show();
                createWaitingDetails();
                if(Internet.isNetworkAvailable(this))
                    startActivity(new Intent(this, videopage.class));
                else
                    Internet.ShowDialog(this);
                break;
            case R.id.settings:
                prefs = getSharedPreferences("X", MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("lastActivity", String.valueOf(waitingtime.class.getName()));
                editor.commit();
                startActivity(new Intent(this, settingcl.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled.",Toast.LENGTH_SHORT).show();
    }
    private void createWaitingDetails() {
        prefs = getSharedPreferences("X", MODE_PRIVATE);
        editor = prefs.edit();
        editor.remove("WaitingTime");
        editor.commit();
        waitingDetails wd = new waitingDetails();
        if(index != 0) {
            editor.putInt("WaitingTime", arr2[this.index - 1] * 60);
            videopage.timeInSec = arr2[this.index-1]*60;
        }
        else {
            editor.putInt("WaitingTime", arr2[this.index] * 60);
            videopage.timeInSec = arr2[this.index]*60;
        }
        editor.commit();
        for(int i=0;i<arr1.length;i++){
            if(arr1[i])
                wd.setCategoryArr(i,true);
        }
        wd.setWaitingPlace(waitingPlace);
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
