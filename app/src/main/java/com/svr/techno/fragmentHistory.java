package com.svr.techno;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.svr.techno.Adapters.Models.ItemModel;
import com.svr.techno.Adapters.Models.orderModel;

import java.util.ArrayList;

public class fragmentHistory extends Fragment implements AdapterHistory.ContentClickListener {

    private AdapterHistory adapter;
    ArrayList<orderModel> orderModels;
    Long userType;

    FirebaseFirestore firestoreInstance;
    FirebaseAuth authInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authInstance = FirebaseAuth.getInstance();
        firestoreInstance = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, parent, false);

        orderModels = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_history);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new AdapterHistory(getActivity(), orderModels);
        adapter.setClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getItem();

        return view;

    }

    private void getItem() {
        final CollectionReference collectionReference = firestoreInstance.collection("orders");
        final String uid = authInstance.getCurrentUser().getUid();
        DocumentReference docRef = firestoreInstance.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        userType = (Long) document.get("type");
                        if (userType == 1){
                            collectionReference
                                    .whereEqualTo("id_seller", uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                                    orderModel orderModel = queryDocumentSnapshot.toObject(orderModel.class);
                                                    orderModels.add(orderModel);
                                                }
                                                adapter.notifyDataSetChanged();

                                            } else {

                                            }
                                        }
                                    });
                        }
                        else {
                            collectionReference
                                    .whereEqualTo("id_buyer", uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                                    orderModel orderModel = queryDocumentSnapshot.toObject(orderModel.class);
                                                    orderModels.add(orderModel);
                                                }
                                                adapter.notifyDataSetChanged();
                                            } else {

                                            }
                                        }
                                    });
                        }
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
//                        Log.d(TAG, "No such document");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("Test", "Item Clicked, Status : " + adapter.getItem(position));
    }
}
