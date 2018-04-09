package UIFragmentClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nurdan.lavaproject.MessageInboxActivity;
import com.example.nurdan.lavaproject.R;

import ApplicationLogic.ProgramSingletonController;

import static com.google.android.gms.wearable.DataMap.TAG;

//Alert fragment to display the permission selected by the user and let them make a decision about it(accept/deny)
public class UserPermissionsMessageFragment extends DialogFragment{
    Button acpt, deny, back;
    TextView messageDisplay;
    int permId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.permission_choice_fragment_layout,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
        //Set up instances
        acpt = view.findViewById(R.id.permAccept);
        deny = view.findViewById(R.id.permDeny);
        back = view.findViewById(R.id.permBack);
        messageDisplay = view.findViewById(R.id.permMessageDisplay);
        //Show full message
        messageDisplay.setText(((MessageInboxActivity)getActivity()).getPermissionMessage());
        Log.d(TAG, "onViewCreated: Message " + ((MessageInboxActivity)getActivity()).getPermissionMessage());
        //Store permission id
        Log.d(TAG, "onViewCreated: Recieved permission id " + ((MessageInboxActivity)getActivity()).getPermissionID());
        permId = ((MessageInboxActivity)getActivity()).getPermissionID();

        //Button listeners. If a permission is denied or accepted then the user returns to their mail inbox
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currInstance.respondToRequest(permId, false);
                dismiss();
                Intent mailboxIntent = MessageInboxActivity.getInboxIntent(getContext());
                startActivity(mailboxIntent);
            }
        });

        acpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currInstance.respondToRequest(permId, true);
                dismiss();
                Intent mailboxIntent = MessageInboxActivity.getInboxIntent(getContext());
                startActivity(mailboxIntent);
            }
        });

        //Grab status of the selected permission from host activity. If it is accepted or denied then we hide the
        //accept and deny buttons from the user
        String tempPermStatus = ((MessageInboxActivity)getActivity()).getPermissionStatus();
        if(tempPermStatus.compareTo(getString(R.string.denyStatus)) == 0 || tempPermStatus.compareTo(getString(R.string.acptStatus)) == 0){
            acpt.setVisibility(View.INVISIBLE);
            deny.setVisibility(View.INVISIBLE);
        }
    }
}
