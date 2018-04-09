package UIFragmentClasses;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nurdan.lavaproject.R;
import com.example.nurdan.lavaproject.single_message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

import static com.google.android.gms.wearable.DataMap.TAG;

/*This class will configure the fragment to display the inbox of the current logged in user

 */
public class UserInboxDisplayFragment extends Fragment {
    ListView inbox;
    ProgramSingletonController currSingletonInstance;
    JSONArray rawMessages;
    ArrayList<Integer> messageId;
    ArrayList<String> messageText;
    ArrayAdapter<String> messageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_display_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inbox = (ListView) view.findViewById(R.id.messageDisplayListivew);
        //Two array lists to hold the text and the id of a message
        messageText = new ArrayList<>();
        messageId = new ArrayList<>();
        //Run async method to get messages
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
            //If there are no messages, display a notification
            if(rawMessages == null || rawMessages.length() == 0){
                messageText.add("You do not have any messages");
            }
            else {
                for(int i = 0; i < rawMessages.length(); i++){
                    try {
                        JSONObject tempObject = rawMessages.getJSONObject(i);
                        String stringObj = tempObject.getString("text");

                        // truncate messages
                        String subText = stringObj.substring(0, Math.min(stringObj.length(), 10));
                        messageText.add(subText + "...");

                        // get corresponding id of messages
                        Integer mId = tempObject.getInt("id");
                        messageId.add(mId);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "messageIds: " + messageId.toString());

            setupEventListener(getContext());
        }
    }

    // get full message when message is clicked
    private void setupEventListener(final Context context) {

        Log.d(TAG, "onPostExecute: About to load arrayAdapter!!!");
        messageAdapter = new ArrayAdapter<String>(context,R.layout.user_listview_display_layout, messageText);
        inbox.setAdapter(messageAdapter);
        inbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent singleMessage = new Intent(getContext(), single_message.class);
                singleMessage.putExtra("messageId", messageId.get(position));
                startActivity(singleMessage);


            }
        });
    }


}
