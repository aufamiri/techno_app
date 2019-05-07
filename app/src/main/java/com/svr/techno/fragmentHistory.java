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

import java.util.ArrayList;

public class fragmentHistory extends Fragment implements AdapterHistory.ContentClickListener {

    private AdapterHistory adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, parent, false);

        ArrayList<Boolean> orderStatus = new ArrayList<>();
        orderStatus.add(Boolean.FALSE);
        orderStatus.add(Boolean.FALSE);
        orderStatus.add(Boolean.TRUE);
        orderStatus.add(Boolean.TRUE);
        orderStatus.add(Boolean.TRUE);
        orderStatus.add(Boolean.TRUE);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_history);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new AdapterHistory(getActivity(), orderStatus);
        adapter.setClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("Test", "Item Clicked, Status : " + adapter.getItem(position));
    }
}
