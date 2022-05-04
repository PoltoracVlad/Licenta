package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView goToRegister;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.rediRegister);

        goToRegister.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));

        loginBtn.setOnClickListener(v -> {
            checkField(email);
            checkField(password);

            if (valid) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    checkTypeOfUser(authResult.getUser().getUid());
                }).addOnFailureListener(e -> Toast.makeText(Login.this, "The Email or Password is incorrect", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void checkTypeOfUser(String uid) {
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            String document = documentSnapshot.getString("TypeOfUser");
            if (document.equals("NormalUser")) {
                startActivity(new Intent(getApplicationContext(), NormalUser.class));
            }
            else if (document.equals("MedicalUser")) {
                startActivity(new Intent(getApplicationContext(), MedicalUser.class));
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
}
