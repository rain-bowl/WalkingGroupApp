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
public class userProfileDisplayFragment extends Fragment{
    ArrayList<String> monitorees;
    ProgramSingletonController currInstance;
    ListView dispalyMonitorees;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_display, null);
    }
    //Set up all listeners using the provided instance of User class stored in the singleton
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       currInstance = ProgramSingletonController.getCurrInstance();
       JSONObject userInformation = currInstance.getUserInfo();
       monitorees = new ArrayList<>();


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
        getUserYouMonitor getMonitorees = new getUserYouMonitor();
        getMonitorees.execute();

        try {
            Log.d(TAG, "onViewCreated: test" + userInformation.getString("emergencyContactInfo"));
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
        }

        dispalyMonitorees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
;

    }

    private class getUserYouMonitor extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
           monitorees = currInstance.getUsersMonitored(getContext());
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter monitoreesAdapter = new ArrayAdapter(getContext(), R.layout.user_listview_display_layout, monitorees);
            dispalyMonitorees.setAdapter(monitoreesAdapter);
        }
    }


}



