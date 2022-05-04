package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000;
    FirebaseUser currentUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            DocumentReference df = firebaseFirestore.collection("Users").document(currentUser.getUid());
            df.get().addOnSuccessListener(documentSnapshot -> {
                String document = documentSnapshot.getString("TypeOfUser");
                if (document.equals("NormalUser")) {
                    startActivity(new Intent(MainActivity.this, NormalUser.class));
                }
                else if (document.equals("MedicalUser")) {
                    startActivity(new Intent(MainActivity.this, MedicalUser.class));
                }
            });
        } else {
            new Handler().postDelayed(() -> {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }, TIME_OUT);
        }
    }
}