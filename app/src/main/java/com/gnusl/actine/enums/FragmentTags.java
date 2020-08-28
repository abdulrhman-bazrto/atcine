package com.gnusl.actine.enums;

public enum FragmentTags {

    LoginFragment(0),
    RegisterFragment(1),
    GuestFragment(2),
    HomeFragment(3),
    SearchFragment(4),
    ComingSoonFragment(5),
    DownloadsFragment(6),
    MoreFragment(7),
    HomeContainerFragment(8),
    ShowDetailsFragment(9),
    SearchResultFragment(10),
    EditNewProfileFragment(11),
    ManageProfileFragment(12),
    MyListFragment(13),
    HelpFragment(14),
    AppSettingsFragment(15),
    ShowSeasonsFragment(16),
    HelpDetailsFragment(17),
    PaymentLessFragment(18),
    CategoriesFragment(19),
    ToWatchFragment(20),
    MainAuthFragment(21),
    ;


    int type;

    FragmentTags(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
