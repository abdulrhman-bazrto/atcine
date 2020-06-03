package com.gnusl.actine.ui.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.CategoryItem;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<CategoryItem> items;
    private final int mResource;
    private final int mResource1;

    public CategorySpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List objects, @LayoutRes int resource1) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mResource1 = resource1;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, true, mResource1);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, false, mResource
        );
    }

    private View createItemView(int position, View convertView, ViewGroup parent, boolean isDropDown, int mResource) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView tvStatusText = view.findViewById(R.id.tv_category_name);
        if (!isDropDown) {
            view.setBackgroundResource(R.color.transparent);
            tvStatusText.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.icon_arrow_drop_down), null);
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tvStatusText.getLayoutParams();
            int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                    mContext.getResources().getDisplayMetrics());
            params.setMargins(20 * dp1, 0, 20 * dp1, 0); //substitute parameters for left, top, right, bottom
            tvStatusText.setLayoutParams(params);
        }

        tvStatusText.setText(items.get(position).getText());

        return view;
    }
}