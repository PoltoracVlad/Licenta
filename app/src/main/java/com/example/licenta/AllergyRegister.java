package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AllergyRegister  extends AppCompatActivity {

    EditText allergyName, allergySymptoms, description;
    Button addAllergyBtn;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        allergyName = findViewById(R.id.allergyName);
        allergySymptoms = findViewById(R.id.allergySymptoms);
        description = findViewById(R.id.description);
        addAllergyBtn = findViewById(R.id.addAllergyBtn);

        addAllergyBtn.setOnClickListener(v -> {
            checkField(allergyName);
            checkField(allergySymptoms);
            checkField(description);

            if (valid) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                DocumentReference df = firebaseFirestore.collection("Allergies").document();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", allergyName.getText().toString());
                userInfo.put("symptoms", allergySymptoms.getText().toString());
                userInfo.put("description", description.getText().toString());
                userInfo.put("userId", firebaseUser.getUid());
                df.set(userInfo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AllergyRegister.this, "Allergy added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllergyRegister.this, "Failed to add allergy", Toast.LENGTH_SHORT).show();
                    }
                });


                checkTypeOfUser(firebaseUser.getUid());
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicalProfileFragment()).commit();
                finish();
            } else {
                Toast.makeText(AllergyRegister.this, "Please complete all the fields!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void checkTypeOfUser(String uid) {
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            String document = documentSnapshot.getString("TypeOfUser");
            if (document.equals("NormalUser")) {
                startActivity(new Intent(getApplicationContext(), NormalUser.class));
            } else if (document.equals("MedicalUser")) {
                startActivity(new Intent(getApplicationContext(), MedicalUser.class));
            }
        });
    }
}
