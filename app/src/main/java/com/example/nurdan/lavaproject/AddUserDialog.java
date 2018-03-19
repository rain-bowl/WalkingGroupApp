package com.example.nurdan.lavaproject;

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

/**
 * Created by yavor on 15/03/18.
 */

public class AddUserDialog extends AppCompatDialogFragment{
    View currView;
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
        usrInput = view.findViewById(R.id.emailInput);
        usrWarning = view.findViewById(R.id.emptyInptWrn);
        addUsrMonitor = view.findViewById(R.id.addUsrMonitor);
        addUsrMonitoree = view.findViewById(R.id.addUsrMonitorThis);
        cancelBtn = view.findViewById(R.id.backBtn);
        addUsrBtn = view.findViewById(R.id.addUsrBtn);

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
                if(emailInput.length() != 0){
                    asyncReq instance = new asyncReq();
                    instance.execute();
                }
                else {
                    usrWarning.setText("Please enter a valid email address!");
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

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Add a user to monitor");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        currView = inflater.inflate(R.layout.add_user_dialog_layout, null);
        dialogBuilder.setView(currView);
        createUsrInput();
        dialogBuilder.setPositiveButton("Add user", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    if(emailInput.length() != 0){
                        Log.d(TAG, "onClick: I get here!k");
                    usrWarning.setText(emailInput);
                    }
                    else {
                        usrWarning.setText("Please enter a valid email into the input!");

                    }
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return dialogBuilder.create();
    }
    //This method simply initiates all of the inputs.
    private void createUsrInput(){
        usrInput =  currView.findViewById(R.id.emailInput);
        usrWarning = (TextView) currView.findViewById(R.id.emptyInptWrn);
        addUsrMonitor = (CheckBox) currView.findViewById(R.id.addUsrMonitor);
        addUsrMonitoree = (CheckBox) currView.findViewById(R.id.addUsrMonitorThis);
        usrInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            emailInput= usrInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            emailInput = usrInput.getText().toString();
            }
        });
    }*/
    private class asyncReq extends AsyncTask<Void,Void,Void>{
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstantce();
        @Override
        protected Void doInBackground(Void... voids) {
            if(addUsrMonitor.isChecked() && addUsrMonitoree.isChecked()){
                currInstance.addUsrMonitor(emailInput, getContext());
                currInstance.addUsrMonitorYou(emailInput, getContext());
            }
            else if(addUsrMonitor.isChecked()){
                Boolean flag = currInstance.addUsrMonitor(emailInput, getContext());
                Log.d(TAG, "doInBackground: FLAG:" + flag);
                if(flag){
                    Log.d(TAG, "doInBackground: Added!!!!!");
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


