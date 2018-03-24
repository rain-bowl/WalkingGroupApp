package UIFragmentClasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nurdan.lavaproject.R;
import com.example.nurdan.lavaproject.UserProfile;

import org.json.JSONObject;

import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.User;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/* This fragment will gather all the edits which the user would like to make. It uses a series of temp
variables which will hold whatever edit the user wants to make. If they click on the back button due to
changing their mind then their data on the server is kept the same. If on they click on submit changes button
then whatever has been modified will be submitted to server.
 */
public class userProfileEditInfoFragment extends Fragment {
    String tempAddress, tempHomePhone, tempCellPhone, tempGrade, tempTeacheName, tempEmergencyInfo, tempName, tempEmail;
    int tempBirthMonth = 0;
    int tempBirthYear = 0;
    EditText birthYear, birthMonth, address, homePhone, cellPhone, grade, teacher, emergencyInfo, name, email;
    Button submitChanges;
    JSONObject userInformation;
    ProgramSingletonController currInstance;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_edit_fragment_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currInstance = ProgramSingletonController.getCurrInstance();
        try {
            userInformation = currInstance.getUserInfo();
            Log.d(TAG, "onViewCreated: USER INFO RETRIEVEd " + userInformation);
        }
        catch (Exception e){

        }

        //Local instances of inputs
        name = view.findViewById(R.id.nameInput);
        email = view.findViewById(R.id.emailInput);
        birthYear = view.findViewById(R.id.birthYearInput);
        birthMonth = view.findViewById(R.id.birthMonthInput);
        address = view.findViewById(R.id.addressInput);
        homePhone = view.findViewById(R.id.homePhoneInput);
        cellPhone = view.findViewById(R.id.cellPhoneInput);
        grade = view.findViewById(R.id.userGradeInput);
        teacher = view.findViewById(R.id.userTeacherInput);
        emergencyInfo = view.findViewById(R.id.userEmergencyInput);
        try {
            name.setText(userInformation.getString("name"));
            email.setText(userInformation.getString("email"));
            birthYear.setText(userInformation.getString("birthYear"));
            birthMonth.setText(userInformation.getString("birthMonth"));
            address.setText(userInformation.getString("address"));
            homePhone.setText(userInformation.getString("homePhone"));
            cellPhone.setText(userInformation.getString("cellPhone"));
            grade.setText(userInformation.getString("grade"));
            teacher.setText(userInformation.getString("teacherName"));
            emergencyInfo.setText(userInformation.getString("emergencyContactInfo"));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Button instance
        submitChanges = view.findViewById(R.id.submitChangesBtn);

        //Listeners to all inputs

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            tempName = s.toString();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempEmail = s.toString();
            }
        });
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
                createJsonBody("name", tempName);
                createJsonBody("email", tempEmail);
                createJsonBody("birthYear", tempBirthYear);
                createJsonBody("birthMonth", tempBirthMonth);
                createJsonBody("address", tempAddress);
                createJsonBody("cellPhone", tempCellPhone);
                createJsonBody("homePhone", tempHomePhone);
                createJsonBody("grade", tempGrade);
                createJsonBody("teacherName", tempTeacheName);
                createJsonBody("emergencyContactInfo", tempEmergencyInfo);
                userInformation.remove("lastGpsLocation");
                Boolean returnStatus;
               sendNewData editInfoServercall = new sendNewData();
               editInfoServercall.execute();


            }
        });


    }

//The following two overriden methods will take in an input, compare it to what we have and if it has changed then the
    //new input will replace the old one.
    private void createJsonBody(String key, String value){
        try {
            if ((value.compareTo(userInformation.getString(key)) != 0)) {
                Log.d(TAG, "createJsonBody: Value input" + value);
                this.userInformation.put(key, value);
            }
        }
        catch (Exception e){

        }
    }

       private void createJsonBody(String key, int value) {
           try {
               if (value != userInformation.getInt(key) || value != 0) {
                   userInformation.put(key, value+"");
               }
           }
           catch (Exception e){

           }
       }


       private class sendNewData extends AsyncTask<Void,Void, Boolean>{
           @Override
           protected Boolean doInBackground(Void... voids) {
               Log.d(TAG, "doInBackground: Background runner for editing information has been reached");
               Log.d(TAG, "doInBackground: Contents on edited info " + userInformation);
              currInstance.editUserInformation(userInformation, getContext());
              return null;
           }

           @Override
           protected void onPostExecute(Boolean aBoolean) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
               ((UserProfile)getActivity()).loadFragment(new userProfileDisplayFragment());
           }
       }

}
