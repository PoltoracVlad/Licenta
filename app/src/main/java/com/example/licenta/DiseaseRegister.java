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

public class DiseaseRegister extends AppCompatActivity {

    EditText diseaseName, diseaseType, description;
    Button addDiseaseBtn;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        diseaseName = findViewById(R.id.diseaseName);
        diseaseType = findViewById(R.id.diseaseType);
        description = findViewById(R.id.description);
        addDiseaseBtn = findViewById(R.id.addDiseaseBtn);

        addDiseaseBtn.setOnClickListener(v -> {
            checkField(diseaseName);
            checkField(diseaseType);
            checkField(description);

            if (valid) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                DocumentReference df = firebaseFirestore.collection("Diseases").document();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", diseaseName.getText().toString());
                userInfo.put("type", diseaseType.getText().toString());
                userInfo.put("description", description.getText().toString());
                userInfo.put("userId", firebaseUser.getUid());
                df.set(userInfo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(DiseaseRegister.this, "Disease added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DiseaseRegister.this, "Failed to add disease", Toast.LENGTH_SHORT).show();
                    }
                });


                checkTypeOfUser(firebaseUser.getUid());
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicalProfileFragment()).commit();
                finish();
            } else {
                Toast.makeText(DiseaseRegister.this, "Please complete all the fields!", Toast.LENGTH_SHORT).show();
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
