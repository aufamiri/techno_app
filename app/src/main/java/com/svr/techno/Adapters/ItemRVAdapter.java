package com.svr.techno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.svr.techno.Adapters.Models.ItemModel;
import com.svr.techno.R;
import com.svr.techno.activityDetails;

import java.util.ArrayList;


public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.itemRVViewHolder> {

    Context context;
    ArrayList<ItemModel> itemModels;

    public ItemRVAdapter(Context context, ArrayList<ItemModel> itemModels) {
        this.context = context;
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public itemRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_item, parent, false);

        return new itemRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemRVViewHolder holder, int position) {

        final ItemModel itemModel = itemModels.get(position);
        holder.setCover(itemModel.getCover());
        holder.title.setText(itemModel.getName());
        holder.price.setText(itemModel.getPriceText());

        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activityDetails.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("item",itemModel);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class itemRVViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title, price;

        public itemRVViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.content_home_item_cover);
            title = itemView.findViewById(R.id.content_home_item_title);
            price = itemView.findViewById(R.id.content_home_item_price);
        }

        public void setCover(String url) {

            Glide
                    .with(context)
                    .load(url)
                    .into(cover);
        }
    }
}
