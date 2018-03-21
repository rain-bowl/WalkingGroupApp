package UIFragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nurdan.lavaproject.R;

import org.json.JSONObject;

import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.User;


/* This fragment will gather all the edits which the user would like to make. It uses a series of temp
variables which will hold whatever edit the user wants to make. If they click on the back button due to
changing their mind then their data on the server is kept the same. If on they click on submit changes button
then whatever has been modified will be submitted to server.
 */
public class userProfileEditInfoFragment extends Fragment {
    String tempAddress, tempHomePhone, tempCellPhone, tempGrade, tempTeacheName, tempEmergencyInfo;
    int tempBirthMonth = 0;
    int tempBirthYear = 0;
    EditText birthYear, birthMonth, address, homePhone, cellPhone, grade, teacher, emergencyInfo;
    Button submitChanges;
    JSONObject jsonBody;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_edit_fragment_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
        User userInstance = currInstance.getCurrLoggedInUser();

        //Local instances of inputs
        birthYear = view.findViewById(R.id.birthYearInput);
        birthMonth = view.findViewById(R.id.birthMonthInput);
        address = view.findViewById(R.id.addressInput);
        homePhone = view.findViewById(R.id.homePhoneInput);
        cellPhone = view.findViewById(R.id.cellPhoneInput);
        grade = view.findViewById(R.id.userGradeInput);
        teacher = view.findViewById(R.id.userTeacherInput);
        emergencyInfo = view.findViewById(R.id.userEmergencyInput);

        //Button instance
        submitChanges = view.findViewById(R.id.submitChangesBtn);

        //Listeners to all inputs
        birthYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    tempBirthYear = Integer.parseInt(s.toString());
                }
                catch (Exception e){

                }

            }
        });

        birthMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    tempBirthMonth = Integer.parseInt(s.toString());
                }
                catch (Exception e){

                }
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempAddress = s.toString();
            }
        });

        homePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempHomePhone = s.toString();
            }
        });

        cellPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempCellPhone = s.toString();
            }
        });

        grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempGrade = s.toString();
            }
        });

        teacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempTeacheName = s.toString();
            }
        });

        emergencyInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempEmergencyInfo = s.toString();
            }
        });

        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonBody("birthYear", tempBirthYear);
                createJsonBody("birthMonth", tempBirthMonth);
                createJsonBody("address", tempAddress);
                createJsonBody("cellPhone", tempCellPhone);
                createJsonBody("homePhone", tempHomePhone);
                createJsonBody("grade", tempGrade);
                createJsonBody("teacherName", tempTeacheName);
            }
        });


    }


    private void createJsonBody(String key, String value){
        if(value != null){
            try{
                jsonBody.put(key, value);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

       private void createJsonBody(String key, int value){
        if(value != 0){
            try{
                jsonBody.put(key, value);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
