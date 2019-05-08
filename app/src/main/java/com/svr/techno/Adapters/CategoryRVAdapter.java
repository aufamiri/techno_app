package com.svr.techno.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svr.techno.Adapters.Models.CategoryModel;
import com.svr.techno.Adapters.Models.ItemModel;
import com.svr.techno.R;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryRVViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryRVAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_category, parent, false);

        return new CategoryRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVViewHolder holder, int position) {

        final CategoryModel categoryModel = categoryModels.get(position);
        ArrayList<ItemModel> singleItem = categoryModel.getItemModels();
        String categoryName = categoryModel.getTitle();


        holder.setCategoryName(categoryName);

        ItemRVAdapter itemRVAdapter = new ItemRVAdapter(context, singleItem);

        holder.categoryRV.setHasFixedSize(true);
        holder.categoryRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.categoryRV.setAdapter(itemRVAdapter);
        holder.moreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryRVViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView getCategoryRV() {
            return categoryRV;
        }

        public void setCategoryRV(RecyclerView categoryRV) {
            this.categoryRV = categoryRV;
        }

        public TextView getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName.setText(categoryName);
        }

        public TextView getMoreItem() {
            return moreItem;
        }

        public void setMoreItem(TextView moreItem) {
            this.moreItem = moreItem;
        }

        RecyclerView categoryRV;
        TextView categoryName, moreItem;

        public CategoryRVViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryRV = itemView.findViewById(R.id.content_home_rv_category);
            categoryName = itemView.findViewById(R.id.content_home_category_title);
            moreItem = itemView.findViewById(R.id.content_home_category_more);

        }
    }
}
