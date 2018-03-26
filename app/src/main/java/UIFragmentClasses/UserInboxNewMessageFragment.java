package UIFragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nurdan.lavaproject.R;

import org.json.JSONObject;

import ApplicationLogic.ProgramSingletonController;


/* This fragment class will handle all of the logic needed for controlling the sending of a new message to
whatever group/user is necessary.
 */
public class UserInboxNewMessageFragment extends Fragment{
    String message;
    Boolean parentFlag, groupFlag;             //Flags which decide whether something is clicked on or not.
    ProgramSingletonController currSingletonInstance;
    JSONObject userInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_new_message_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currSingletonInstance = ProgramSingletonController.getCurrInstance();
        userInfo = currSingletonInstance.getUserInfo();
        //Attach widgets
        final ToggleButton sendToParents = view.findViewById(R.id.sendToPrntsTgl);
        final ToggleButton sendToGroup = view.findViewById(R.id.sendToGrpTgl);
        Button sendMessage = view.findViewById(R.id.sendBtn);
        EditText messageBody = view.findViewById(R.id.messageInput);

        //Attach listeners
        sendToParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendToParents.isChecked()){
                    parentFlag = true;
                    FragmentManager fm = getFragmentManager();
                    newMessageTargetsDialogFragment frag = new newMessageTargetsDialogFragment();
                    frag.show(fm, "tag");
                }
                else{
                    parentFlag = false;
                }
            }
        });

        sendToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendToGroup.isChecked()){
                    groupFlag = true;
                }
                else{
                    groupFlag = false;
                }
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parentFlag && groupFlag){
                    currSingletonInstance.sendMsgToGroup(message, 31, false, getContext());
                    currSingletonInstance.sendMsgToParents(message, 31, false, getContext());
                }
                else if(parentFlag){
                    currSingletonInstance.sendMsgToParents(message, 31, false, getContext());
                }
                else if(groupFlag){
                    currSingletonInstance.sendMsgToGroup(message, 31, false, getContext());
                }
                else{
                    Toast.makeText(getContext(),R.string.noSendDestinationWarning,Toast.LENGTH_LONG).show();
                }

            }
        });

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
