package com.svr.techno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.svr.techno.Adapters.CategoryRVAdapter;
import com.svr.techno.Adapters.Models.CategoryModel;
import com.svr.techno.Adapters.Models.ItemModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public View view;
    public RecyclerView categoryRV;

    CategoryRVAdapter categoryRVAdapter;
    ArrayList<CategoryModel> categoryModels;
    ArrayList<ItemModel> itemModels;

    FirebaseFirestore firestoreInstance;

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

        initItem();

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

    private void initHome() {
        for (int i = 1; i <= 2; i++) {
            String category = null;
            CategoryModel categoryModel = new CategoryModel();

            switch (i) {
                case 1:
                    category = "Makanan";
                    break;
                case 2:
                    category = "Minuman";
                    break;
            }
            categoryModel.setTitle(category);

            ArrayList<ItemModel> pItemModels = new ArrayList<>();

            for (ItemModel item : itemModels) {

                if (item.getCategory().equals(categoryModel.getTitle())) {

                    //test(item.getName());
                    pItemModels.add(item);
                }

                initCategory();
            }

            categoryModel.setItemModels(pItemModels);
            categoryModels.add(categoryModel);
            test(String.valueOf(pItemModels.size()));
            categoryRVAdapter.notifyDataSetChanged();
        }

    }

    private void initCategory() {

        for (int i = 1; i <= 6; i++) {
            CategoryModel xCategoryModel = new CategoryModel();
            String xCategory = null;

            switch (i) {

                case 1:
                    xCategory = "Makanan";
                    break;
                case 2:
                    xCategory = "Minuman";
                    break;
                case 3:
                    xCategory = "Kategori 3";
                    break;
                case 4:
                    xCategory = "Kategori 4";
                    break;
                case 5:
                    xCategory = "Kategori 5";
                    break;
                case 6:
                    xCategory = "Kategori 6";
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

    private void initData() {


        for (int i = 1; i <= 2; i++) {
            String category = null;
            CategoryModel categoryModel = new CategoryModel();

            switch (i) {
                case 1:
                    category = "Makanan";
                    break;
                case 2:
                    category = "Minuman";
                    break;
            }

            categoryModel.setTitle(category);

            CollectionReference collectionReference = firestoreInstance.collection("items");
            Query query = collectionReference.whereEqualTo("category", category);

            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                    ItemModel itemModel = queryDocumentSnapshot.toObject(ItemModel.class);
                                    itemModels.add(itemModel);
                                }

                            }
                        }
                    });

            categoryModel.setItemModels(itemModels);
            categoryModels.add(categoryModel);

            categoryRVAdapter.notifyDataSetChanged();
        }
    }
}