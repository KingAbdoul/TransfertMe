package com.example.transfertme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> items;
    private int[] imageResources;

    public CustomArrayAdapter(Context context, List<String> items, int[] imageResources) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.imageResources = imageResources;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.sendImageIds);
            holder.textView = view.findViewById(R.id.sendNames);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Définir les données pour la vue
        String item = items.get(position);
        int imageResource = imageResources[position];
        holder.imageView.setImageResource(imageResource);
        holder.textView.setText(item);

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
