package com.gnusl.actine.ui.enums;

public enum FragmentTags {

    LoginFragment(0),
    RegisterFragment(1),
    ;


    int type;

    FragmentTags(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
