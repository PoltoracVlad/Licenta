package com.example.licenta;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MedicalProfileFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;

    Button btnAddDisease;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = getView().findViewById(R.id.recyclerViewDiseases);
        firebaseFirestore = FirebaseFirestore.getInstance();
        btnAddDisease = getView().findViewById(R.id.btnAddDisease);

        Query query = firebaseFirestore.collection("Diseases");
        FirestoreRecyclerOptions<MedicalProfile> options = new FirestoreRecyclerOptions.Builder<MedicalProfile>()
                .setQuery(query, MedicalProfile.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MedicalProfile, DiseaseViewHolder>(options) {
            @NonNull
            @Override
            public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medical_profile, parent, false);
                return new DiseaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position, @NonNull MedicalProfile model) {
                holder.nameText.setText(model.getName());
                holder.typeText.setText(model.getType());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private class DiseaseViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView typeText;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.inputName);
            typeText = itemView.findViewById(R.id.inputType);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
