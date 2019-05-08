package com.svr.techno;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.svr.techno.Adapters.CategoryRVAdapter;
import com.svr.techno.Adapters.MainMenuAdapter;
import com.svr.techno.Adapters.Models.CategoryModel;
import com.svr.techno.Adapters.Models.ItemModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Long userType;
    Context context;
    String[] titles;
    int[] images;

    public HomeFragment(Context context) {
        this.context = context;
    }

    GridView mainMenu;
    MainMenuAdapter mainMenuAdapter;

    public View view;
    public RecyclerView categoryRV;

    CategoryRVAdapter categoryRVAdapter;
    ArrayList<CategoryModel> categoryModels;
    ArrayList<ItemModel> itemModels;

    FirebaseFirestore firestoreInstance;
    FirebaseAuth authInstance;

    private void test(String input) {
        Toast.makeText(getActivity(), input, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        categoryRV = view.findViewById(R.id.content_home_rv_main);

        itemModels = new ArrayList<>();
        categoryModels = new ArrayList<>();

        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        categoryRVAdapter = new CategoryRVAdapter(getActivity(), categoryModels);

        categoryRV.setAdapter(categoryRVAdapter);

        firestoreInstance = FirebaseFirestore.getInstance();

        authInstance = FirebaseAuth.getInstance();

        initItem();

        mainMenu = view.findViewById(R.id.content_home_gridview);
        checkUserType();

        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (titles[position]) {

                    case "Account":
                        authInstance
                                .signOut();
                        Intent intentA = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intentA);
                        break;
                    case "Transaction History":
                        test("Next Update");
                        break;
                    case  "Favorite":
                        test("Next Update");
                        break;
                    case  "Create Product":
                        Intent intent = new Intent(getActivity(), SellActivity.class);
                        getActivity().startActivity(intent);
                        break;

                }
            }
        });

        return view;
    }

    private void initItem() {

        CollectionReference collectionReference = firestoreInstance.collection("items");

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                ItemModel itemModel = queryDocumentSnapshot.toObject(ItemModel.class);
                                itemModels.add(itemModel);
                            }

                            initCategory();

                        } else {

                        }
                    }
                });
    }

    private void checkUserType() {

        String uid = authInstance.getCurrentUser().getUid();
        DocumentReference docRef = firestoreInstance.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        userType = (Long) document.get("type");
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
//                        Log.d(TAG, "No such document");
                    }

                    initMainMenu();
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void initMainMenu() {

        if (userType == 0) {
            titles = new String[]{"Account", "Transaction History", "Favorite"};
            images = new int[]{R.drawable.accounts, R.drawable.bills, R.drawable.favorites};
        } else if (userType == 1) {
            titles = new String[]{"Account", "Transaction History", "Create Product"};
            images = new int[]{R.drawable.accounts, R.drawable.bills, R.drawable.adds};
        }




        mainMenuAdapter = new MainMenuAdapter(context, titles, images);
        mainMenu.setAdapter(mainMenuAdapter);

    }

    private void initCategory() {

        for (int i = 1; i <= 4; i++) {
            CategoryModel xCategoryModel = new CategoryModel();
            String xCategory = null;

            switch (i) {

                case 1:
                    xCategory = "Sayur Sop";
                    break;
                case 2:
                    xCategory = "Sayur Capcay";
                    break;
                case 3:
                    xCategory = "Sayur Kangkung";
                    break;
                case 4:
                    xCategory = "Ala carte";
                    break;

            }

            xCategoryModel.setTitle(xCategory);

            ArrayList<ItemModel> xItemModels;
            xItemModels = new ArrayList<>();

            for (ItemModel item : itemModels) {

                if (item.getCategory().equals(xCategoryModel.getTitle())) {

                    xItemModels.add(item);

                }

            }

            xCategoryModel.setItemModels(xItemModels);
            categoryModels.add(xCategoryModel);

            categoryRVAdapter.notifyDataSetChanged();
        }


    }

}