package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.CategoryItem;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<CategoryItem> items;
    private final int mResource;

    public CategorySpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, true);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, false
        );
    }

    private View createItemView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        final View view = mInflater.inflate(mResource, parent, false);

        if (!isDropDown)
            view.setBackgroundResource(R.color.transparent);
        TextView tvStatusText = view.findViewById(R.id.tv_category_name);
        tvStatusText.setText(items.get(position).getText());


        return view;
    }
}