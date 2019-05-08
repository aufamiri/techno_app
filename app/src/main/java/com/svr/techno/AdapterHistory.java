package com.svr.techno;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.svr.techno.Adapters.Models.orderModel;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

    private List<orderModel> mOrderModels;
    private LayoutInflater mInflater;
    private ContentClickListener mClickListener;

    AdapterHistory(Context context, List<orderModel> orderModels) {
        this.mInflater = LayoutInflater.from(context);
        this.mOrderModels = orderModels;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        orderModel orderModel = mOrderModels.get(position);

        holder.orderText.setText(orderModel.getId_item());
        holder.seller_text.setText(orderModel.getId_seller());



        if (orderModel.getConfr_buyer() == 1) {
            holder.buttonConfirm.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            holder.buttonConfirm.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.GONE);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mOrderModels.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout;
        TextView dateCompleted, orderText, seller_text;
        Button buttonConfirm;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.button_layout);
            dateCompleted = itemView.findViewById(R.id.date_completed_text);
            orderText = itemView.findViewById(R.id.order_text);
            seller_text = itemView.findViewById(R.id.seller_text);
            buttonConfirm = itemView.findViewById(R.id.konfirmasi_button);
            buttonConfirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    orderModel getItem(int id) {
        return mOrderModels.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ContentClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ContentClickListener {
        void onItemClick(View view, int position);
    }
}
