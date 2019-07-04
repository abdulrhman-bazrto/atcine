package com.gnusl.actine.util;

public enum Constants {

    HomeDetailsExtra("Movie"),
    EditNewProfileExtra("Profile"),
    ManageProfilesExtra("ManageProfile"),
    HelpExtra("HelpExtra"),
    ;

    private String name;

    Constants(String constName) {
        this.name = constName;
    }

    public String getConst() {
        return name;
    }
}
