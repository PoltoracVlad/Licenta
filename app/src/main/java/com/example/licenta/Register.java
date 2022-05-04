package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText firstName, lastName, email, password, phone, emergencyPhone;
    Button registerBtn;
    TextView goToLogin;
    CheckBox normalUserCheckbox;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.registerFirstName);
        lastName = findViewById(R.id.registerLastName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        emergencyPhone = findViewById(R.id.registerEmergencyPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.rediLoginTv);
        normalUserCheckbox = findViewById(R.id.isNormalUser);

        registerBtn.setOnClickListener(v -> {
            checkField(firstName);
            checkField(lastName);
            checkField(email);
            checkField(password);
            checkField(phone);
            checkField(emergencyPhone);

            if (valid) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {
                    if (normalUserCheckbox.isChecked()) {
                        registering("NormalUser");
                    } else {
                        registering("MedicalUser");
                    }
                }).addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to Create Account", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Register.this, "Please complete all the fields!", Toast.LENGTH_SHORT).show();
            }
        });

        goToLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("This field must be completed!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public void registering(String typeOfUser) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
        DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("FirstName", firstName.getText().toString());
        userInfo.put("LastName", lastName.getText().toString());
        userInfo.put("UserEmail", email.getText().toString());
        userInfo.put("PhoneNumber", phone.getText().toString());
        userInfo.put("EmergencyPhoneNumber", emergencyPhone.getText().toString());
        userInfo.put("TypeOfUser", typeOfUser);
        df.set(userInfo);

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
