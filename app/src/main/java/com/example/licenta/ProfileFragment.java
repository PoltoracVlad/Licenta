package com.example.licenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    TextView firstName, lastName, email, phoneNumber, emergencyPhoneNumber;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        firstName = getView().findViewById(R.id.firstNameInput);
        lastName = getView().findViewById(R.id.lastNameInput);
        email = getView().findViewById(R.id.emailInput);
        phoneNumber = getView().findViewById(R.id.phoneNumberInput);
        emergencyPhoneNumber = getView().findViewById(R.id.emergencyPhoneNumberInput);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            email.setText(firebaseUser.getEmail());
            DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
            df.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        String phone = documentSnapshot.getString("PhoneNumber");
                        phoneNumber.setText(phone);
                        String emergencyPhone = documentSnapshot.getString("EmergencyPhoneNumber");
                        emergencyPhoneNumber.setText(emergencyPhone);
                        String fName = documentSnapshot.getString("FirstName");
                        firstName.setText(fName);
                        String lName = documentSnapshot.getString("LastName");
                        lastName.setText(lName);
                    }
                }
            });
        }
    }
}
