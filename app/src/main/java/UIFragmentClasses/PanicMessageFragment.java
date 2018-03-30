package UIFragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurdan.lavaproject.R;

import ApplicationLogic.ProgramSingletonController;
//Class will handle all the necessary logic for the fragment which sends an emergency message.
public class PanicMessageFragment extends DialogFragment{
    private String emergencyMessage;
    private ProgramSingletonController currSingletonInstance;
    private Button backBtn, sendMsgBtn;
    private EditText emergencyMsgText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.panic_message_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        backBtn = view.findViewById(R.id.panicMsgCnclBtn);
        sendMsgBtn = view.findViewById(R.id.panicMsgSendBtn);
        emergencyMsgText = view.findViewById(R.id.panicMsgInput);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emergencyMessage != null && emergencyMessage.length() != 0){
                    currSingletonInstance = ProgramSingletonController.getCurrInstance();
                    currSingletonInstance.sendMsgToParents(emergencyMessage, true, getContext());
                }
                else {
                    Toast.makeText(getContext(), R.string.emptyMessageWarning, Toast.LENGTH_LONG).show();
                }

            }
        });


        emergencyMsgText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emergencyMessage = s.toString();
            }
        });


    }
}
