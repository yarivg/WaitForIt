package company.wfi.com.waitforit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by User on 18/10/2015.
 */
public class categorypage extends Activity implements View.OnClickListener {

    ImageButton vidoesbtn,gamesbtn,jokesbtn,puzzlebtn,newsbtn,musicbtn,nextbtn,settingbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecategory);

        vidoesbtn = (ImageButton)findViewById(R.id.videosbutton);
        gamesbtn = (ImageButton)findViewById(R.id.gamesbutton);
        jokesbtn = (ImageButton)findViewById(R.id.jokesbutton);
        puzzlebtn = (ImageButton)findViewById(R.id.puzzlesbutton);
        newsbtn = (ImageButton)findViewById(R.id.newsbutton);
        musicbtn = (ImageButton)findViewById(R.id.musicbutton);
        nextbtn = (ImageButton)findViewById(R.id.nextbutton);
        settingbtn = (ImageButton)findViewById(R.id.settings);

        vidoesbtn.setOnClickListener(this);
        gamesbtn.setOnClickListener(this);
        jokesbtn.setOnClickListener(this);
        puzzlebtn.setOnClickListener(this);
        newsbtn.setOnClickListener(this);
        musicbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        settingbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.videosbutton :
                vidoesbtn.setImageResource(R.drawable.videoschecked);
                break;
            case R.id.gamesbutton :
                vidoesbtn.setImageResource(R.drawable.gameschecked);
                break;
            case R.id.jokesbutton :
                Toast.makeText(this, "Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.puzzlesbutton :
                Toast.makeText(this, "Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newsbutton :
                Toast.makeText(this, "Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.musicbutton :
                Toast.makeText(this, "Will be available soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nextbutton :
                break;
            case R.id.settings :
                startActivity(new Intent(this,firstAct.class));
                break;
        }
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
