package com.svr.techno.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svr.techno.R;

public class MainMenuAdapter extends BaseAdapter {
    private Context context;
    private String[] titles;
    private int[] images;
    private LayoutInflater inflater;

    public MainMenuAdapter(Context context, String[] titles, int[] images) {
        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_item, null);
        //convertView = inflater.inflate(R.layout.main_menu_item, null);

        if (inflater == null) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.main_menu, null);

        }

        ImageView imageView = convertView.findViewById(R.id.main_menu_item_image);
        TextView textView = convertView.findViewById(R.id.main_menu_item_title);

        imageView.setImageResource(images[position]);
        textView.setText(titles[position]);

        return convertView;
    }
}
