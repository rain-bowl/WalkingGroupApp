package UIFragmentClasses;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurdan.lavaproject.R;
import com.example.nurdan.lavaproject.RegisterActivity;


/*Fragment which handles the logic behind the input of mandatory information. It compares the passwords
to check if they match and checks if all of the fields have been entered.
 */
public class MandatoryRegisterInformationFragment extends Fragment{
     String userName, userEmail, userPassword, userConfirmPassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_user_mandatory_info_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button nextBtn = view.findViewById(R.id.nextRegisterBtn);
        EditText nameInput = view.findViewById(R.id.usernameInput);
        final EditText emailInput = view.findViewById(R.id.userEmail);
        EditText passwordInput = view.findViewById(R.id.passwordInput);
        EditText confirmPasswordInput = view.findViewById(R.id.passwordConfirmInput);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userName = s.toString();

            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            userEmail = s.toString();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            userPassword = s.toString();
            }
        });

        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            userConfirmPassword = s.toString();
            }
        });



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName == null || userEmail == null || userPassword == null || userConfirmPassword == null){
                    Toast.makeText(getContext(), R.string.missingFields, Toast.LENGTH_LONG).show();
                }
                else if(userPassword.compareTo(userConfirmPassword) == 0) {
                    ((RegisterActivity) getActivity()).addJson("name", userName);
                    ((RegisterActivity) getActivity()).addJson("email", userEmail);
                    ((RegisterActivity) getActivity()).addJson("password", userPassword);
                    ((RegisterActivity) getActivity()).replaceFragment(new NonMandatoryRegisterInfoFragment());
                }
                else{
                    Toast.makeText(getContext(), R.string.passwordMismatch, Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}
