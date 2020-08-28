package com.gnusl.actine.ui.Mobile.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceHeight;
    private Drawable divider;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public MarginItemDecoration(int spaceHeight, Context context, int resId) {
        this.spaceHeight = spaceHeight;
        if (resId != 0)
            divider = ContextCompat.getDrawable(context, resId);

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceHeight;
        }
//        outRect. left =  spaceHeight;
//        outRect.right = spaceHeight;
        outRect.bottom = spaceHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (divider == null)
            return;
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
