package com.visionarymindszm.examsresults.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.utils.PupilSessionManager;
import java.util.HashMap;

/**
 * This is a Profile fragment
 */

public class Profile extends Fragment {
    TextView pupilName, schoolName, intakeYear;
    Button logoutButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        pupilName = view.findViewById(R.id.pupilName);
        schoolName = view.findViewById(R.id.schoolName);
        intakeYear = view.findViewById(R.id.intakeYear);
        logoutButton = view.findViewById(R.id.logoutButton);

        init();
        return  view;
    }

    /**
     * Initialize the object, get the
     */
    private void init() {
        final PupilSessionManager sessionManager = new PupilSessionManager(requireContext());
        HashMap<String, String> pupils = sessionManager.getUserDetails();
        String pupilName_ = "Pupil Name: "+pupils.get(PupilSessionManager.KEY_PUPIL_NAME);
        String schoolName_ =  "School: "+pupils.get(PupilSessionManager.KEY_PUPIL_SCHOOL);
        String intakeYear_ = "Intake: "+pupils.get(PupilSessionManager.KEY_PUPIL_INTAKE);
        pupilName.setText(pupilName_);
        schoolName.setText(schoolName_);
        intakeYear.setText(intakeYear_);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });

    }
}
