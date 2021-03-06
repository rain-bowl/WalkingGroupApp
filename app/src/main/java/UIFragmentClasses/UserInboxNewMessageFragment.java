package UIFragmentClasses;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nurdan.lavaproject.MessageInboxActivity;
import com.example.nurdan.lavaproject.R;

import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

import static com.google.android.gms.wearable.DataMap.TAG;


/* This fragment class will handle all of the logic needed for controlling the sending of a new message to
whatever group/user is necessary.
 */
public class UserInboxNewMessageFragment extends Fragment{
    String message;
    Boolean parentFlag = false;
    Boolean groupFlag = false;             //Flags which decide whether something is clicked on or not.
    ProgramSingletonController currSingletonInstance;
    JSONObject userInfo;
    ArrayList<Integer> groupIDList;
    int userID;
    int groupID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_new_message_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currSingletonInstance = ProgramSingletonController.getCurrInstance();
        //Grab the list of group ids hosted in singleton
        groupIDList = currSingletonInstance.getGroupIDList();
        final MessageInboxActivity messageInstance = (MessageInboxActivity) getActivity();
        //Attach widgets
        final ToggleButton sendToParents = view.findViewById(R.id.sendToPrntsTgl);
        final ToggleButton sendToGroup = view.findViewById(R.id.sendToGrpTgl);
        Button sendMessage = view.findViewById(R.id.sendBtn);
        EditText messageBody = view.findViewById(R.id.messageInput);
        sendToGroup.setBackgroundColor(Color.RED);
        sendToParents.setBackgroundColor(Color.RED);
        //Attach listeners
        sendToParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendToParents.isChecked()){
                    sendToParents.setBackgroundColor(Color.GREEN);
                    parentFlag = true;
                }
                else{
                    sendToParents.setBackgroundColor(Color.RED);
                    parentFlag = false;
                }
            }
        });

        sendToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendToGroup.isChecked()){
                    sendToGroup.setBackgroundColor(Color.GREEN);
                    groupFlag = true;
                    FragmentManager fm = getFragmentManager();
                    newMessageTargetsDialogFragment frag = new newMessageTargetsDialogFragment();
                    frag.show(fm, "Selecting a group");
                }
                else{
                    sendToGroup.setBackgroundColor(Color.RED);
                    groupFlag = false;
                }
            }
        });
        //Listener for the button responsible for sending a message. Handles all of the different possible combinations
        //of users to which the person would like to send a message to.
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int idIndex = messageInstance.getGroupID();
               if (idIndex != -1) {
                   groupID = groupIDList.get(idIndex);
               }

                Log.d(TAG, "onClick: Group Id Check " + groupID);
                if(parentFlag && groupFlag){
                    currSingletonInstance.sendMsgToGroup(message, groupID, false, getContext());
                    currSingletonInstance.sendMsgToParents(message, false, getContext());
                }
                else if(parentFlag){
                    currSingletonInstance.sendMsgToParents(message, false, getContext());
                }
                else if(groupFlag){
                    currSingletonInstance.sendMsgToGroup(message, groupID, false, getContext());
                }
                else if(message.length() == 0){
                    Toast.makeText(getContext(), R.string.emptyMessageWarning, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),R.string.noSendDestinationWarning,Toast.LENGTH_LONG).show();
                }
                MessageInboxActivity instance = (MessageInboxActivity) getActivity();
                instance.setFragment(new UserInboxDisplayFragment());
            }
        });

        //Listener for text input for the message
        messageBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                message = s.toString();
            }
        });


    }

    }

