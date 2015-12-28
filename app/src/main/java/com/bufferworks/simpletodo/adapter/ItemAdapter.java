package com.bufferworks.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bufferworks.simpletodo.R;
import com.bufferworks.simpletodo.entity.Item;
import com.bufferworks.simpletodo.entity.Priority;
import com.bufferworks.simpletodo.entity.Status;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(final Context context, final List<Item> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        // Lookup view for data population
        final TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        final TextView tvNotes = (TextView) convertView.findViewById(R.id.tvNotes);
        tvTitle.setText(item.getTitle());
        tvNotes.setText(item.getNotes());

        final CheckBox cbStatus = (CheckBox) convertView.findViewById(R.id.cbStatus);
        cbStatus.setChecked(false);
        if (Status.DONE.equals(item.getStatus())) {
            cbStatus.setChecked(true);
        }

        final ImageView ivPriority = (ImageView) convertView.findViewById(R.id.ivPriority);
        if (Priority.HIGH.equals(item.getPriority())) {
            ivPriority.setImageResource(R.drawable.high);
        } else if (Priority.MEDIUM.equals(item.getPriority())) {
            ivPriority.setImageResource(R.drawable.medium);
        } else if (Priority.LOW.equals(item.getPriority())) {
            ivPriority.setImageResource(R.drawable.low);
        }

        return convertView;
    }
}
