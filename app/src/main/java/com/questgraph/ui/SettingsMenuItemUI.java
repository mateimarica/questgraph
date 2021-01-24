package com.questgraph.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.questgraph.R;

class SettingsMenuItemUI extends MenuItemUI {

    SettingsMenuItemUI(AccountActivity accountActivity, Fragment currentFragment, DrawerLayout drawer, Toolbar toolbar) {
        super(accountActivity, currentFragment, drawer, toolbar);

        MenuItem settingsMenuItem = accountActivity.menu.add("Settings");
        settingsMenuItem.setIcon(R.drawable.ic_settings);
        settingsMenuItem.setCheckable(true);
        settingsMenuItem.setChecked(false);

        settingsMenuItem.setOnMenuItemClickListener(item -> settingsMenuItemPressed());

    }

    private boolean settingsMenuItemPressed() {

        //TODO Make this open a settings fragment which includes a dark mode option + option to reset app
        //Removes any active fragments to go back to the base activity interface
        if (currentFragment != null) {
            accountActivity.getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            currentFragment = null;
        }

        SettingsFragment newFragment = new SettingsFragment();

        currentFragment = newFragment;

        accountActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).addToBackStack("").commit();


        toolbar.setTitle("Settings");
        drawer.closeDrawers();

        return true;
    }
}