package com.gnusl.actine.ui.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;


public class FeaturesSymbolRow extends ConstraintLayout {

    TextView tvTitle;
    ImageView ivInBasic, ivInStandard, ivInPremium;

    public FeaturesSymbolRow(Context context) {
        super(context);
        init(null);
    }

    public FeaturesSymbolRow(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (!isInEditMode()) {
//            init(attrs);
//        }
        init(attrs);
    }

    public FeaturesSymbolRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        if (!isInEditMode()) {
//            init(attrs);
//        }
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_symbol_row, FeaturesSymbolRow.this);

        tvTitle = view.findViewById(R.id.tv_title);
        ivInBasic = view.findViewById(R.id.iv_in_basic);
        ivInStandard = view.findViewById(R.id.iv_in_standard);
        ivInPremium = view.findViewById(R.id.iv_in_premium);

    }

    public void setData(String title,boolean inBasic,boolean inStandard,boolean inPremium){
        setRowTitle(title);
        setInBasic(inBasic);
        setInStandard(inStandard);
        setInPremium(inPremium);
    }

    private void setRowTitle(String title) {
        this.tvTitle.setText(title);
    }

    private void setInBasic(boolean inBasic) {
        if (inBasic)
            this.ivInBasic.setImageResource(R.drawable.icon_check);
        else
            this.ivInBasic.setImageResource(R.drawable.icon_cancel_grey);
    }

    private void setInStandard(boolean inStandard) {
        if (inStandard)
            this.ivInStandard.setImageResource(R.drawable.icon_check);
        else
            this.ivInStandard.setImageResource(R.drawable.icon_cancel_grey);
    }

    private void setInPremium(boolean inPremium) {
        if (inPremium)
            this.ivInPremium.setImageResource(R.drawable.icon_check);
        else
            this.ivInPremium.setImageResource(R.drawable.icon_cancel_grey);
    }

    public ImageView getIvInBasic() {
        return ivInBasic;
    }

    public ImageView getIvInPremium() {
        return ivInPremium;
    }

    public ImageView getIvInStandard() {
        return ivInStandard;
    }
}
