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

import com.example.nurdan.lavaproject.R;
import com.example.nurdan.lavaproject.RegisterActivity;
//Fragment which handles the input of all non mandatory user information during registration.
public class NonMandatoryRegisterInfoFragment extends Fragment{
    String address, cellphone, homePhone, grade, teacherName, emergencyInfo;
    int birthYear, birthMonth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_user_optional_information, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    EditText birthYearInput = view.findViewById(R.id.birthYearInput);
    EditText birthMonthInput = view.findViewById(R.id.birthMonthInput);
    EditText addressInput = view.findViewById(R.id.addressInput);
    EditText gradeInput = view.findViewById(R.id.userGradeInput);
    EditText teacherInput = view.findViewById(R.id.userTeacherInput);
    EditText homePhoneNum = view.findViewById(R.id.homePhoneInput);
    EditText cellPhoneNum = view.findViewById(R.id.cellPhoneInput);
    EditText emergencyInstrctuctions = view.findViewById(R.id.userEmergencyInput);
    Button newUserBtn = view.findViewById(R.id.registerBtn);

    //Listeners for all inputs
        birthYearInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    birthYear = Integer.parseInt(s.toString());
                }
                catch (Exception e){
                    birthYear = -1;
                }
            }
        });


        birthMonthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
              try {
                  birthMonth = Integer.parseInt(s.toString());
              }
              catch (Exception e){
                  birthMonth = -1;
              }
            }
        });

        addressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            address = s.toString();
            }
        });

        gradeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                grade = s.toString();
            }
        });

        teacherInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            teacherName = s.toString();
            }
        });

        homePhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            homePhone = s.toString();
            }
        });
        cellPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            cellphone = s.toString();
            }
        });

        emergencyInstrctuctions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            emergencyInfo = s.toString();
            }
        });

        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add the inputs to the host activity
                ((RegisterActivity)getActivity()).addJson("birthYear", birthYear);
                ((RegisterActivity)getActivity()).addJson("birthMonth", birthMonth);
                ((RegisterActivity)getActivity()).addJson("address", address);
                ((RegisterActivity)getActivity()).addJson("cellPhone", cellphone);
                ((RegisterActivity)getActivity()).addJson("homePhone", homePhone);
                ((RegisterActivity)getActivity()).addJson("grade", grade);
                ((RegisterActivity)getActivity()).addJson("teacherName", teacherName);
                ((RegisterActivity)getActivity()).addJson("emergencyContactInfo", emergencyInfo);
                ((RegisterActivity)getActivity()).executeRegisterAction();


            }
        });


    }
}
