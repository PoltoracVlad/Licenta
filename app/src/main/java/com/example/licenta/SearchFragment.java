package com.example.licenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = getView().findViewById(R.id.listRecycler);
        searchView = getView().findViewById(R.id.searchInput);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        showUsers();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUser(newText);
                return true;
            }
        });
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView firstName;
        private TextView lastName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
        }
    }

    private void showUsers() {
        Query query = firebaseFirestore.collection("Users");
        FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
                .setQuery(query, SearchModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SearchModel, SearchViewHolder>(options) {
            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_item, parent, false);
                return new SearchViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull SearchModel model) {
                holder.firstName.setText(model.getFirstName());
                holder.lastName.setText(model.getLastName());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void searchUser(String text) {
        Query query = firebaseFirestore.collection("Users").whereArrayContains("FirstName", text);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            adapter.notifyDataSetChanged();
        });
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
