package UIFragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nurdan.lavaproject.R;

/*This class contains all of the logic related to displaying and handling user input related to
any available permissions
 */
public class UserPermissionsFragment extends Fragment{
    ListView permissionDisplay;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_inbox_display_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        permissionDisplay = view.findViewById(R.id.messageDisplayListivew);
    }

    
}
