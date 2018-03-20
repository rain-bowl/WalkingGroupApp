package UIFragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nurdan.lavaproject.R;

import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.User;

/**
 * Created by yavor on 19/03/18.
 */

public class userProfileDisplayFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_display, null);
    }
    //Set up all listeners using the provided instance of User class stored in the singleton
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
        User userInstance = currInstance.getCurrLoggedInUser();

        TextView birthYear = view.findViewById(R.id.birthYearInput);
        TextView birthMonth = view.findViewById(R.id.birthMonthInput);
        TextView address = view.findViewById(R.id.addressInput);
        TextView homePhone = view.findViewById(R.id.homePhoneInput);
        TextView cellPhone = view.findViewById(R.id.cellPhoneInput);
        TextView grade = view.findViewById(R.id.userGradeInput);
        TextView teacher = view.findViewById(R.id.userTeacherInput);
        TextView emergencyInfo = view.findViewById(R.id.userEmergencyInput);

        //Setting text
        birthYear.setText(String.valueOf(userInstance.getBirthyear()));
        birthMonth.setText(String.valueOf(userInstance.getBirthmonth()));
        address.setText(userInstance.getUserAddress());
        homePhone.setText(userInstance.getHomePhoneNumber());
        cellPhone.setText(userInstance.getCellPhoneNumber());
        grade.setText(userInstance.getGrade());
        teacher.setText(userInstance.getTeacherName());
        emergencyInfo.setText(userInstance.getEmergencyContactInfoInstruction());





    }
}
