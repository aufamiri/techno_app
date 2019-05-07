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


import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

    private List<Boolean> mStatus;
    private LayoutInflater mInflater;
    private ContentClickListener mClickListener;

    AdapterHistory(Context context, List<Boolean> status) {
        this.mInflater = LayoutInflater.from(context);
        this.mStatus = status;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Boolean status = mStatus.get(position);

        Log.i("status", Boolean.toString(status));

        if (status == Boolean.TRUE) {
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
        return mStatus.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout;
        TextView dateCompleted;
        Button buttonConfirm;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.button_layout);
            dateCompleted = itemView.findViewById(R.id.date_completed_text);
            buttonConfirm = itemView.findViewById(R.id.konfirmasi_button);
            buttonConfirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Boolean getItem(int id) {
        return mStatus.get(id);
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
