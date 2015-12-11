package company.wfi.com.waitforit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class playlistInfo {
    private static final String serverURL = "http://wait4itapp.com/webservice/get_playlist.php?";
    public static int indexInList = 0;
    private boolean isOver = false;
    public static List<ItemInPlaylist> mPlaylist = new ArrayList<ItemInPlaylist>();

    /*
    A function that handles recieving the playlist as a json object.
    @params
    waitingTimeMinutes - The user's Waiting Time in Minutes.
    waitingPlace - The integer value of the place where the user is waiting.
    categories - an array of strings which contains the categories that the user want to get.
                Example - recieveJsonPlaylist(25,3,["Videos","Games"])
     */
    public static void recieveJsonPlaylist(final Context context, final int waitingTimeMinutes,int waitingPlace,String categories,String userId){
        String myFullRequest = serverURL +"total_time=" +waitingTimeMinutes + categories +"&user_id=" + userId;
        mAsyncTaskForObjectArray mAsyncTask = (company.wfi.com.waitforit.mAsyncTaskForObjectArray) new mAsyncTaskForObjectArray(new mAsyncTaskForObjectArray.AsyncResponse() {
            @Override
            public void processFinish(JSONArray array) {
                try {
                    JSONObject object;
                    String id;
                    String url;
                    String type;
                    int length = array.length();
                    for(int i=0;i<length;i++){
                        object = array.getJSONObject(i);
                        id = object.getString("id");
                        url = object.getString("url");
                        type = object.getString("object_type");
                        //if(type.equals("2"))
                            mPlaylist.add(new ItemInPlaylist(id,url,type));
                    }
                    //Log.d("playlistInfo", "AsyncTask Created the plalist");
                    if(mPlaylist.size() > 0){
                        Toast.makeText(context, "We Successfully Craeted Your Playlist!", Toast.LENGTH_SHORT).show();
                        if(mPlaylist.get(0).getType().equals("1")){//video
                            videopage.timeInSec = waitingTimeMinutes*60;
                            videopage.videoURL = mPlaylist.get(0).getUrl();
                            context.startActivity(new Intent(context,videopage.class));

                        }
                        else if(mPlaylist.get(0).getType().equals("2")){//game
                            discriptionAct.timeInSec = waitingTimeMinutes*60;
                            discriptionAct.currentGameId = mPlaylist.get(indexInList).getId();
                            context.startActivity(new Intent(context,discriptionAct.class));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("playlistInfo","Error Creating the playlist");
                }catch (NullPointerException e){
                    e.printStackTrace();
                    //Log.e("playlistInfo","NullPointExeption");
                }
            }
        }).execute(myFullRequest);
    }
    public String getCurrentId(){
        return mPlaylist.get(indexInList).getId();
    }
    public String getCurrentUrl(){
        return mPlaylist.get(indexInList).getUrl();

    }
    public String getCurrentType(){
        return mPlaylist.get(indexInList).getType();
    }
    public ItemInPlaylist getCurrentItem(){
        return mPlaylist.get(indexInList);
    }
    public void IncrementIndexInPlaylist(){
        indexInList++;
        if(mPlaylist.size() <= indexInList) {
            isOver = true;
        }
    }
    public static void StopPlaylist(){
        indexInList = 0;
    }
    public static void NextInPlaylist(Context context){
        indexInList++;
        if(indexInList < mPlaylist.size()){
            if(mPlaylist.get(indexInList).getType().equals("1")){//video
                videopage.videoURL = mPlaylist.get(indexInList).getUrl();
                context.startActivity(new Intent(context,videopage.class));
            }
            else if(mPlaylist.get(indexInList).getType().equals("2")){//game
                discriptionAct.currentGameId = mPlaylist.get(indexInList).getId();
                context.startActivity(new Intent(context,discriptionAct.class));
            }
        }
        else{
            context.startActivity(new Intent(context,waitingcompleteAct.class));
        }
    }
    public boolean IsOver(){
        return isOver;
    }
    public static void freePlaylist(){
        mPlaylist = null;
        mPlaylist = new ArrayList<ItemInPlaylist>();
        indexInList = 0;

    }


}
