package company.wfi.com.waitforit;

/**
 * Created by User on 20/10/2015.
 */

public class waitingDetails {
    private static final int NUMOFCATEGORIES = constants.NUMOFCATEGORIES;
    private static int waitingTime;
    public static int getWaitingTime(){
        return waitingDetails.waitingTime;
    }//Returns the length of time the user has to wait.
    int waitingPlace;
    boolean[] categoryArr = new boolean[NUMOFCATEGORIES];

    public waitingDetails(){
        waitingTime = 0;
        waitingPlace = 0;
        for(int i=0;i<NUMOFCATEGORIES;i++)
            categoryArr[i] = false;
    }

    public static void setWaitingTime(int waitingtime){
        waitingDetails.waitingTime = waitingtime;
    }
    public void setWaitingPlace(int waitingplace){
        this.waitingPlace = waitingplace;
    }
    public void setCategoryArr(int index,boolean b){
        this.categoryArr[index] = b;
    }
    public static int currentTimer;
    private int numberOfLinks = 1;
    public void setNumberOfLinks(int x){
        this.numberOfLinks = x;
    }
    //public static String[][] LinksArray = new String[0][3];
}
