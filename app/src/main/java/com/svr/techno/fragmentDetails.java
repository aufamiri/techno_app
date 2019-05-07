package com.svr.techno;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.svr.techno.Adapters.Models.ItemModel;

public class fragmentDetails extends Fragment implements View.OnClickListener {

    private ItemModel itemModel;
    private Button priceButton;

    //TODO: add constructor

    @Override
    public void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);

        if (getArguments() != null) {
            itemModel = (ItemModel) getArguments().getSerializable("item");
        }

        Fragment fragment = new fragmentYoutube();
        Bundle bundle = new Bundle();

        bundle.putSerializable("item",itemModel);

        fragment.setArguments(bundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.youtube_fragment, fragment)
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_button:
                Fragment newFragment = new fragmentMap();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", itemModel);
                newFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.details_fragment, newFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameText = view.findViewById(R.id.details_page_title);
        TextView detailsText = view.findViewById(R.id.details_page_factory);
        priceButton = view.findViewById(R.id.buy_button);
        priceButton.setOnClickListener(this);
        nameText.setText(itemModel.getName());
        priceButton.setText(itemModel.getPriceText());
        detailsText.setText(itemModel.getCategory());

        ImageView cover = view.findViewById(R.id.cover_img);
        Glide
                .with(getContext())
                .load(itemModel.getCover())
                .into(cover);

        return  view;
    }
}
