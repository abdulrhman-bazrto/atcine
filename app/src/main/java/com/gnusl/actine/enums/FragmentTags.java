package com.gnusl.actine.enums;

public enum FragmentTags {

    LoginFragment(0),
    RegisterFragment(1),
    HomeFragment(2),
    ;


    int type;

    FragmentTags(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
