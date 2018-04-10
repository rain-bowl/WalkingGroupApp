package UIFragmentClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import ApplicationLogic.ProgramSingletonController;
import static android.content.ContentValues.TAG;
import com.example.nurdan.lavaproject.R;

public class AddUserDialogFragment extends AppCompatDialogFragment{
    EditText usrInput;
    String emailInput = "";
    TextView usrWarning;
    CheckBox addUsrMonitor, addUsrMonitoree;
    Button cancelBtn, addUsrBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_user_dialog_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Instances for all widgets
        usrInput = view.findViewById(R.id.emailInput);
        usrWarning = view.findViewById(R.id.emptyInptWrn);
        addUsrMonitor = view.findViewById(R.id.addUsrMonitor);
        addUsrMonitoree = view.findViewById(R.id.addUsrMonitorThis);
        cancelBtn = view.findViewById(R.id.backBtn);
        addUsrBtn = view.findViewById(R.id.addUsrBtn);
        usrWarning.setVisibility(View.INVISIBLE);

        //Listener for text input
        usrInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            emailInput = s.toString();
            }
        });

        addUsrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If input is not empty then we search for user
                if(emailInput.length() != 0){
                    asyncReq instance = new asyncReq();
                    instance.execute();
                    dismiss();
                }
                else {
                    //Warn that inpput is emtpty
                    usrWarning.setVisibility(View.VISIBLE);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });

    }

    //Async private class. Calls the relevant methods to add a user to be monitored
    private class asyncReq extends AsyncTask<Void,Void,Void>{
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
        @Override
        protected Void doInBackground(Void... voids) {
            //Go through all 3 combinations of the checkboxes and execute the needed methods
            if(addUsrMonitor.isChecked() && addUsrMonitoree.isChecked()){
                currInstance.addUsrMonitor(emailInput, getContext());
                currInstance.addUsrMonitorYou(emailInput, getContext());
            }
            else if(addUsrMonitor.isChecked()){
                Boolean flag = currInstance.addUsrMonitor(emailInput, getContext());
                Log.d(TAG, "doInBackground: FLAG:" + flag);
                if(flag){
                    Log.d(TAG, "doInBackground: Added!");
                }
            }
            else if (addUsrMonitoree.isChecked()){
                Boolean flag = currInstance.addUsrMonitorYou(emailInput, getContext());
                if(flag){
                    Log.d(TAG, "doInBackground: Adding user to monitor you: " + flag);
                }
            }
            return null;
        }
    }
}


