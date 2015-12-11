package company.wfi.com.waitforit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class categorypage extends Activity implements View.OnClickListener {

    public String[] categoryNames = new String[constants.NUMOFCATEGORIES];

    ImageButton vidoesbtn,gamesbtn,jokesbtn,puzzlebtn,newsbtn,musicbtn,nextbtn,settingbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecategory);

        waitingDetails.ResetCategoryArray();
        categoryNames[0] = "VIDEOS";categoryNames[1] = "GAMES";
        categoryNames[2] = "NEWS";categoryNames[3] = "PUZZLES";
        categoryNames[4] = "JOKES";categoryNames[5] = "MUSIC";

        vidoesbtn = (ImageButton)findViewById(R.id.videosbutton);
        gamesbtn = (ImageButton)findViewById(R.id.gamesbutton);
        jokesbtn = (ImageButton)findViewById(R.id.jokesbutton);
        puzzlebtn = (ImageButton)findViewById(R.id.puzzlesbutton);
        newsbtn = (ImageButton)findViewById(R.id.newsbutton);
        musicbtn = (ImageButton)findViewById(R.id.musicbutton);
        nextbtn = (ImageButton)findViewById(R.id.nextbuttonnn);
        settingbtn = (ImageButton)findViewById(R.id.settings);

        vidoesbtn.setTag(false);
        gamesbtn.setTag(false);
        Check();
        vidoesbtn.setOnClickListener(this);
        gamesbtn.setOnClickListener(this);
        jokesbtn.setOnClickListener(this);
        puzzlebtn.setOnClickListener(this);
        newsbtn.setOnClickListener(this);
        musicbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        settingbtn.setOnClickListener(this);

        //Internet.ShowScreenGroup(this);
    }

    private void Check() {
        if(vidoesbtn.getTag().equals(true) || gamesbtn.getTag().equals(true)){
            nextbtn.setVisibility(View.VISIBLE);
            nextbtn.setClickable(true);
        }
        else{
            nextbtn.setVisibility(View.INVISIBLE);
            nextbtn.setClickable(false);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.videosbutton :
                if(vidoesbtn.getTag().equals(false)) {
                    vidoesbtn.setImageResource(R.drawable.videoschecked);
                    vidoesbtn.setTag(true);
                }
                else {
                    vidoesbtn.setImageResource(R.drawable.videos);
                    vidoesbtn.setTag(false);
                }
                waitingDetails.categoryArr[0] = vidoesbtn.getTag().equals(true);
                Check();
                break;
            case R.id.gamesbutton :
                if(gamesbtn.getTag().equals(false)) {
                    gamesbtn.setImageResource(R.drawable.gameschecked);
                    gamesbtn.setTag(true);

                }
                else {
                    gamesbtn.setImageResource(R.drawable.games);
                    gamesbtn.setTag(false);
                }
                waitingDetails.categoryArr[1] = gamesbtn.getTag().equals(true);
                Check();
                break;
            case R.id.jokesbutton :
                Toast.makeText(this, "Jokes Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.puzzlesbutton :
                Toast.makeText(this, "Puzzles Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newsbutton :
                Toast.makeText(this, "News Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.musicbutton :
                Toast.makeText(this, "Music Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nextbuttonnn :
                Intent i = new Intent(getApplicationContext(),placepage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
                break;
            case R.id.settings :
                SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastActivity", String.valueOf(categorypage.class.getName()));
                editor.commit();
                startActivity(new Intent(this,settingcl.class));
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Back button is currently disabled.",Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        boolean mybool = false;
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        try {
            mybool = prefs.getBoolean("gameChecked", mybool);
            if (mybool) {
                this.gamesbtn.setTag(mybool);
                gamesbtn.setImageResource(R.drawable.gameschecked);
                waitingDetails.categoryArr[1] = true;
            }
            else{
                waitingDetails.categoryArr[1] = false;
            }
            mybool = prefs.getBoolean("videoChecked", mybool);
            if (mybool) {
                this.vidoesbtn.setTag(mybool);
                vidoesbtn.setImageResource(R.drawable.videoschecked);
                waitingDetails.categoryArr[0] = true;
            }
            else{
                waitingDetails.categoryArr[0] = false;
            }
            Check();
            SharedPreferences.Editor e = prefs.edit();
            e.remove("videoChecked");
            e.remove("gameChecked");
            e.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("gameChecked", (boolean) gamesbtn.getTag());
        editor.putBoolean("videoChecked", (boolean) vidoesbtn.getTag());

        editor.commit();
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
