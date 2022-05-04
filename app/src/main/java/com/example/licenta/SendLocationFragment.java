package com.example.licenta;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SendLocationFragment extends Fragment {

    Button btnLocation;
    TextView txt1, txt2, txt3, txt4, txt5;
    FusedLocationProviderClient fusedLocationProviderClient;
    String phoneNumber;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_location, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnLocation = getView().findViewById(R.id.btn_location);
        txt1 = getView().findViewById(R.id.txt_location1);
        txt2 = getView().findViewById(R.id.txt_location2);
        txt3 = getView().findViewById(R.id.txt_location3);
        txt4 = getView().findViewById(R.id.txt_location4);
        txt5 = getView().findViewById(R.id.txt_location5);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
            df.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        phoneNumber = documentSnapshot.getString("EmergencyPhoneNumber");
                    }
                }
            });
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        btnLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, 100);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1
                    );
                    txt1.setText(Html.fromHtml(
                            "<font color='#6200EE'><b>Latitude: </b><br></font>"
                                    + addresses.get(0).getLatitude()
                    ));
                    txt2.setText(Html.fromHtml(
                            "<font color='#6200EE'><b>Longitude: </b><br></font>"
                                    + addresses.get(0).getLongitude()
                    ));
                    txt3.setText(Html.fromHtml(
                            "<font color='#6200EE'><b>Country: </b><br></font>"
                                    + addresses.get(0).getCountryName()
                    ));
                    txt4.setText(Html.fromHtml(
                            "<font color='#6200EE'><b>Locality: </b><br></font>"
                                    + addresses.get(0).getLocality()
                    ));
                    txt5.setText(Html.fromHtml(
                            "<font color='#6200EE'><b>Address: </b><br></font>"
                                    + addresses.get(0).getAddressLine(0)
                    ));

                    String SMS = "I need your help, I just had a car accident! Here is my location:\n" + "\nLatitude: " + addresses.get(0).getLatitude() + "\nLongitude: " + addresses.get(0).getLongitude() + "\nCountry: " + addresses.get(0).getCountryName() + "\nLocality: " + addresses.get(0).getLocality();
                    String phone = phoneNumber;

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phone, null, SMS, null, null);
                        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
