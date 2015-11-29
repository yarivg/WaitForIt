package company.wfi.com.waitforit;

/**
 * Created by User on 09/11/2015.
 */
public class Timer implements Runnable{

    public void setNumOfSec(int numOfSec) {
        this.numOfSec = numOfSec;
    }

    //A class which stores the time the user has left in his playlist.
    private int numOfSec;//Number of Seconds in the playlist.
    private boolean stop;
    public int getNumOfSec() {
        return numOfSec;
    }
    public Timer(int numOfSec){
        this.numOfSec = numOfSec;
        this.stop = false;
    }
    @Override
    public void run() {
        if(!this.stop)
            this.numOfSec--;
    }
    public void resumeThreda(){
        this.stop = false;
    }
    public void pauseThread(){
        this.stop = true;
    }
}
