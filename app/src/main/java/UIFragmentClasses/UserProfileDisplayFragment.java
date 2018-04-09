package UIFragmentClasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nurdan.lavaproject.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import ApplicationLogic.AccountApiInteractions;
import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.User;
import static android.content.ContentValues.TAG;


/*Holds logic for displaying the user profile information.
It quite simply grabs the JSONObject through the singleton and sets all of the fields one by one.
 */
public class UserProfileDisplayFragment extends Fragment{
    ProgramSingletonController currInstance;
    ListView dispalyMonitorees;
    JSONObject userInformation = null;
    int theUserId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_display, null);
    }
    //Set up all listeners using the provided instance of User class stored in the singleton
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // get user ID if -1 then display the logged in user's info
        if(this.getArguments() != null)
            theUserId = this.getArguments().getInt("theUserID", -1);

        currInstance = ProgramSingletonController.getCurrInstance();
        if(theUserId == -1) {
            userInformation = currInstance.getUserInfo();
            populateUserInfo(view, userInformation);
        } else {
            asyncGetUserInfo getinfo = new asyncGetUserInfo();
            getinfo.execute(view);
        }


    }

    private void populateUserInfo(View view, JSONObject userInformation) {

        //Set up information display
        TextView name = view.findViewById(R.id.nameInput);
        TextView email = view.findViewById(R.id.emailInput);
        TextView birthYear = view.findViewById(R.id.birthYearInput);
        TextView birthMonth = view.findViewById(R.id.birthMonthInput);
        TextView address = view.findViewById(R.id.addressInput);
        TextView homePhone = view.findViewById(R.id.homePhoneInput);
        TextView cellPhone = view.findViewById(R.id.cellPhoneInput);
        TextView grade = view.findViewById(R.id.userGradeInput);
        TextView teacher = view.findViewById(R.id.userTeacherInput);
        TextView emergencyInfo = view.findViewById(R.id.userEmergencyInput);

        try {
            //Populate text views
            Log.d(TAG, "onViewCreated: test" + userInformation.getString("birthYear"));
            name.setText(userInformation.getString("name"));
            email.setText(userInformation.getString("email"));
            //We use the value of
            if(userInformation.getInt("birthYear") == 0){
                birthYear.setText("Not Provided");
            }
            else {
                birthYear.setText(userInformation.getString("birthYear"));
            }
            if(userInformation.getInt("birthMonth") == 0){
                birthMonth.setText("Not Provided");
            }
            else {
                birthMonth.setText(userInformation.getString("birthMonth"));
            }
            address.setText(userInformation.getString("address"));
            homePhone.setText(userInformation.getString("homePhone"));
            cellPhone.setText(userInformation.getString("cellPhone"));
            grade.setText(userInformation.getString("grade"));
            teacher.setText(userInformation.getString("teacherName"));
            emergencyInfo.setText(userInformation.getString("emergencyContactInfo"));

        }
        catch (Exception e){
        }
    }

    private class asyncGetUserInfo extends AsyncTask<View, Void, Void> {
        View v;
        @Override
        protected Void doInBackground(View... views) {
            v = views[0];
            userInformation = currInstance.getUserInfoByID(theUserId, getContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            populateUserInfo(v, userInformation);
        }
    }
}



