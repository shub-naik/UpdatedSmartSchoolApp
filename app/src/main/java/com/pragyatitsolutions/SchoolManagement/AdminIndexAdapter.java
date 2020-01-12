package com.pragyatitsolutions.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminIndexAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    String[] users_text;
    int[] users_images;

    public AdminIndexAdapter(Context context, String[] users, int[] users_images) {
        this.context = context;
        this.users_text = users;
        this.users_images = users_images;
    }

    @Override
    public int getCount() {
        return users_text.length;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.row_layout, parent, false);
        ImageView User_Image = convertView.findViewById(R.id.Image);
        TextView User_Text = convertView.findViewById(R.id.Text);

        User_Image.setImageResource(users_images[position]);
        User_Text.setText(users_text[position]);

        return convertView;
    }
}
