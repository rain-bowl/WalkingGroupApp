package UIFragmentClasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nurdan.lavaproject.MessageInbox;
import com.example.nurdan.lavaproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.Permission;
import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.UserPermissions;

import static com.google.android.gms.wearable.DataMap.TAG;

//Class which contains the logic to display and accept/deny the user permissions
public class UserPermissionsDisplayFragment extends Fragment{
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    ArrayList<Permission> permissionsList = new ArrayList<>();
    ArrayList<String> permissionText = new ArrayList<>();
    ListView permissionsDisplay;
    MessageInbox mInbox;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_display_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        permissionsDisplay = view.findViewById(R.id.messageDisplayListivew);
        //Add listeners for listview
        permissionsDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Store information in parent activity to be communicated to the aler dialog which displays
                //the message contents
                Log.d(TAG, "onItemClick: Selected permission id " + permissionsList.get(position - 1).getPermissionId());
                ((MessageInbox)getActivity()).setPermissionID(permissionsList.get(position - 1).getPermissionId());
                ((MessageInbox)getActivity()).setPermissionMessage(permissionsList.get(position - 1).getMessage());

                //Call alert dialog to view the contents of the clicked message
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                UserPermissionsMessageFragment displayMessage = new UserPermissionsMessageFragment();
                displayMessage.show(fm, "Show fragment");
            }
        });

        //Call async class
        getAllPermissions getPerm = new getAllPermissions();
        getPerm.execute();

    }
    //Grab all of the permissions(pending, accepted and denied) to be displayed for the user.
    private class getAllPermissions extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            JSONArray deniedPerm, acceptedPerm, pendingPerm = null;
            JSONObject userData;
            userData = currInstance.getUserInfo();
            try{
                pendingPerm = userData.getJSONArray("pendingPermissionRequests");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            deniedPerm = currInstance.getDeniedRequests();
            acceptedPerm = currInstance.getAcceptedRequests();
            makePermissionList(pendingPerm, getString(R.string.pendingStatus));
            makePermissionList(deniedPerm, getString(R.string.denyStatus));
            makePermissionList(acceptedPerm, getString(R.string.acptStatus));


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Create array adapter and populate listview
            ArrayAdapter<String> viewAdapter = new ArrayAdapter<String>(getContext(), R.layout.user_listview_display_layout, permissionText);
            permissionsDisplay.setAdapter(viewAdapter);
        }
    }

    //Method which extracts all the necessary information to display and store/reference a particular
    //request at a later time. Method simply takes in a Json array and a status. The results are kept
    //in two array lists. The first keeps a Permission objects which store id, message and status of a
    //permission. The second arraylist called permissionText will store a "preview" version of the text
    //which limits the length of the displayed message to save space.
    private void makePermissionList(JSONArray permissionArr, String status){
        int tempPermId = -1;
        String tempMessage;
        JSONObject tempObject;

        //If the array containing permissions is not null then they will be added. Otherwise, nothing is done.
        if(permissionArr != null && permissionArr.length() != 0){
            for(int i = 0; i < permissionArr.length(); i++) {
                try {
                    tempObject = permissionArr.getJSONObject(i);
                    tempPermId = tempObject.getInt("id");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tempMessage = currInstance.getPermMessage(tempPermId);
                permissionsList.add(new Permission(status, tempMessage, tempPermId));
                permissionText.add(status + " " + tempMessage.substring(0, 60));
            }
        }
        else{
            permissionText.add("You have no " + status + " requests");
        }
    }
}
