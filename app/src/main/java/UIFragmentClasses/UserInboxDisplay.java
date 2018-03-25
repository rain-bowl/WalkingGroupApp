package UIFragmentClasses;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nurdan.lavaproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

import static com.google.android.gms.wearable.DataMap.TAG;

/*This class will configure the fragment to display the inbox of the current logged in user

 */
public class UserInboxDisplay extends Fragment {
    ListView inbox;
    ProgramSingletonController currSingletonInstance;
    JSONArray rawMessages;
    ArrayList<String> messageText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_display_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inbox = (ListView) view.findViewById(R.id.messageDisplayListivew);
        messageText = new ArrayList<>();
        GetMessages loadMessages = new GetMessages();
        loadMessages.execute();
    }

    private class GetMessages extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            currSingletonInstance = ProgramSingletonController.getCurrInstance();
            rawMessages = currSingletonInstance.getMessagesForUser(getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(rawMessages == null || rawMessages.length() == 0){
                messageText.add("You do not have any messages");
                messageText.add("TESTETEST");
            }
            else {
                for(int i = 0; i < rawMessages.length(); i++){
                    try {
                        JSONObject tempObject = rawMessages.getJSONObject(i);
                        messageText.add(tempObject.getString("text"));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "onPostExecute: About to load arrayAdapter!!!");
            ArrayAdapter<String> messageAdapter = new ArrayAdapter<String>(getContext(),R.layout.user_listview_display_layout, messageText);
            inbox.setAdapter(messageAdapter);
            
        }
    }

}
