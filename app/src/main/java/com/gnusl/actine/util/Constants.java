package com.gnusl.actine.util;

public enum Constants {

    HomeDetailsExtra("Movie"),
    EditNewProfileExtra("Profile"),
    ManageProfilesExtra("ManageProfile"),
    ;

    private String name;

    Constants(String constName) {
        this.name = constName;
    }

    public String getConst() {
        return name;
    }
}
