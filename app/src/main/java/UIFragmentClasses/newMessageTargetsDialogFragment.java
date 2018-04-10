package UIFragmentClasses;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nurdan.lavaproject.MessageInboxActivity;
import com.example.nurdan.lavaproject.R;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

/*This fragment will contain the logic to display which users/groups the new message should be sent to

 */
public class newMessageTargetsDialogFragment extends DialogFragment{
    ArrayList<Integer> groupID;             //Contains the id's of all groups
    ProgramSingletonController currInsance;
    ListView targetDisplay;                 //Display all groups

    public newMessageTargetsDialogFragment(){
        //empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_message_target_layout, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        targetDisplay = view.findViewById(R.id.messageTargetsListview);
        currInsance = ProgramSingletonController.getCurrInstance();
        //Load up all group id's
        groupID = currInsance.getGroupIDList();

        //Set adapter and display them
        ArrayAdapter<String> groupNameAdapter = new ArrayAdapter<String>(getContext(), R.layout.user_listview_display_layout, currInsance.getGroupNamesList());
        targetDisplay.setAdapter(groupNameAdapter);
        //Set the group id in the hosting activity to communicate between the 2 fragments
        targetDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageInboxActivity instance = (MessageInboxActivity) getActivity();
                instance.setGroupID(position);
                dismiss();
            }
        });

    }




}
