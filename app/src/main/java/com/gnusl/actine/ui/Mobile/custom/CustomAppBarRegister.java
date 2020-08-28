package com.gnusl.actine.ui.Mobile.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;

import com.gnusl.actine.R;


public class CustomAppBarRegister extends ConstraintLayout {

    Button btnLogin, btnHelp;

    public CustomAppBarRegister(Context context) {
        super(context);
        init(null);
    }

    public CustomAppBarRegister(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomAppBarRegister(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_app_bar_register, CustomAppBarRegister.this);

        btnHelp = findViewById(R.id.btn_help);
        btnLogin = findViewById(R.id.btn_login);

    }

    public Button getBtnLogin() {
        return btnLogin;
    }

    public Button getBtnHelp() {
        return btnHelp;
    }
}
