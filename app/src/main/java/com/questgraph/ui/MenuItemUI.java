package com.questgraph.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public abstract class MenuItemUI {
    protected AccountActivity accountActivity;
    protected Fragment currentFragment;
    protected DrawerLayout drawer;
    protected Toolbar toolbar;

    MenuItemUI(AccountActivity accountActivity, Fragment currentFragment, DrawerLayout drawer, Toolbar toolbar) {
        this.accountActivity = accountActivity;
        this.currentFragment = currentFragment;
        this.drawer = drawer;
        this.toolbar = toolbar;
    }
}
