package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MedicalProfileFragment extends Fragment {

    RecyclerView recyclerViewDiseases, recyclerViewAllergies, recyclerViewVaccines;
    FirestoreRecyclerAdapter adapterDisease, adapterAllergy, adapterVaccine;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button btnAddDisease, btnAddAllergy, btnAddVaccine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerViewDiseases = getView().findViewById(R.id.recyclerViewDiseases);
        recyclerViewAllergies = getView().findViewById(R.id.recyclerViewAllergies);
        recyclerViewVaccines = getView().findViewById(R.id.recyclerViewVaccines);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        btnAddDisease = getView().findViewById(R.id.btnAddDisease);
        btnAddAllergy = getView().findViewById(R.id.btnAddAllergy);
        btnAddVaccine = getView().findViewById(R.id.btnAddVaccine);

        assert firebaseUser != null;
        showDiseases();
        showAllergies();
        showVaccines();

        btnAddDisease.setOnClickListener(v -> startActivity(new Intent(getContext(), DiseaseRegister.class)));
        btnAddAllergy.setOnClickListener(v -> startActivity(new Intent(getContext(), AllergyRegister.class)));
        btnAddVaccine.setOnClickListener(v -> startActivity(new Intent(getContext(), VaccineRegister.class)));
    }

    private class DiseaseViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView typeText;
        private TextView descriptionText;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.inputName);
            typeText = itemView.findViewById(R.id.inputType);
            descriptionText = itemView.findViewById(R.id.inputDescription);
        }
    }

    private class AllergyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView symptomsText;
        private TextView descriptionText;

        public AllergyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.inputName);
            symptomsText = itemView.findViewById(R.id.inputSymptoms);
            descriptionText = itemView.findViewById(R.id.inputDescription);
        }
    }

    private class VaccineViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView typeText;
        private TextView descriptionText;

        public VaccineViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.inputName);
            typeText = itemView.findViewById(R.id.inputType);
            descriptionText = itemView.findViewById(R.id.inputDescription);
        }
    }

    private void showDiseases() {
        Query query = firebaseFirestore.collection("Diseases").whereEqualTo("userId", firebaseUser.getUid());
        FirestoreRecyclerOptions<DiseaseModel> options = new FirestoreRecyclerOptions.Builder<DiseaseModel>()
                .setQuery(query, DiseaseModel.class)
                .build();

        adapterDisease = new FirestoreRecyclerAdapter<DiseaseModel, DiseaseViewHolder>(options) {
            @NonNull
            @Override
            public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medical_profile, parent, false);
                return new DiseaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position, @NonNull DiseaseModel model) {
                holder.nameText.setText(model.getName());
                holder.typeText.setText(model.getType());
                holder.descriptionText.setText(model.getDescription());
            }
        };

        recyclerViewDiseases.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiseases.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDiseases.setAdapter(adapterDisease);
    }

    private void showAllergies() {
        Query query = firebaseFirestore.collection("Allergies").whereEqualTo("userId", firebaseUser.getUid());
        FirestoreRecyclerOptions<AllergyModel> options = new FirestoreRecyclerOptions.Builder<AllergyModel>()
                .setQuery(query, AllergyModel.class)
                .build();

        adapterAllergy = new FirestoreRecyclerAdapter<AllergyModel, AllergyViewHolder>(options) {
            @NonNull
            @Override
            public AllergyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medical_profile2, parent, false);
                return new AllergyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllergyViewHolder holder, int position, @NonNull AllergyModel model) {
                holder.nameText.setText(model.getName());
                holder.symptomsText.setText(model.getSymptoms());
                holder.descriptionText.setText(model.getDescription());
            }
        };

        recyclerViewAllergies.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAllergies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAllergies.setAdapter(adapterAllergy);
    }

    private void showVaccines() {
        Query query = firebaseFirestore.collection("Vaccines").whereEqualTo("userId", firebaseUser.getUid());
        FirestoreRecyclerOptions<VaccineModel> options = new FirestoreRecyclerOptions.Builder<VaccineModel>()
                .setQuery(query, VaccineModel.class)
                .build();

        adapterVaccine = new FirestoreRecyclerAdapter<VaccineModel, VaccineViewHolder>(options) {
            @NonNull
            @Override
            public VaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medical_profile, parent, false);
                return new VaccineViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VaccineViewHolder holder, int position, @NonNull VaccineModel model) {
                holder.nameText.setText(model.getName());
                holder.typeText.setText(model.getType());
                holder.descriptionText.setText(model.getDescription());
            }
        };

        recyclerViewVaccines.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewVaccines.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVaccines.setAdapter(adapterVaccine);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterDisease.stopListening();
        adapterAllergy.stopListening();
        adapterVaccine.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterDisease.startListening();
        adapterAllergy.startListening();
        adapterVaccine.startListening();
    }
}
