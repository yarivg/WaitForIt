package company.wfi.com.waitforit;

import java.util.ArrayList;
import java.util.List;


public class waitingDetails {
    private static final int NUMOFCATEGORIES = constants.NUMOFCATEGORIES;
    private static int waitingTime;
    public static int getWaitingTime(){
        return waitingDetails.waitingTime;
    }//Returns the length of time the user has to wait.
    private static int waitingPlace;
    public static int getWaitingPlace(){
        return waitingDetails.waitingPlace;
    }//Returns the place where the user has to wait.
    public static boolean[] categoryArr = new boolean[NUMOFCATEGORIES];

    public static void setWaitingTime(int waitingtime){
        waitingDetails.waitingTime = waitingtime;
    }
    public static void setWaitingPlace(int waitingplace){
        waitingDetails.waitingPlace = waitingplace;
    }
    public void setCategoryArr(int index,boolean b){
        this.categoryArr[index] = b;
    }
    public static int currentTimer;
    public String getCategoryString() {
        String str = "";
        str += "&videos=";
        if(categoryArr[0])//videos
            str += "1";
        else
            str += "0";
        str += "&games=";
        if(categoryArr[1])//games
            str += "1";
        else
            str += "0";
        str += "&news=1&puzzles=0&jokes=1&music=1";
        return str;
    }
    public String getName(int index){
        String result = "";
        switch (index){
            case 0:
                result = "VIDEOS";
                break;
            case 1:
                result = "GAMES";
                break;
            case 2:
                result = "NEWS";
                break;
            case 3:
                result = "PUZZLES";
                break;
            case 4:
                result = "JOKES";
                break;
            case 5:
                result = "MUSIC";
                break;
        }
        return result;

    }
    public static void ResetCategoryArray(){
        for(int i=0;i<NUMOFCATEGORIES;i++){
            categoryArr[i] = false;
        }
    }
}
