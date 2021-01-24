package com.questgraph.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.questgraph.R;

class HomeMenuItemUI extends MenuItemUI {


    HomeMenuItemUI(AccountActivity accountActivity, Fragment currentFragment, DrawerLayout drawer, Toolbar toolbar) {
        super(accountActivity, currentFragment, drawer, toolbar);

        MenuItem homeMenuItem = accountActivity.menu.add("Home");
        homeMenuItem.setIcon(R.drawable.ic_home);
        homeMenuItem.setCheckable(true);
        homeMenuItem.setChecked(true);

        homeMenuItem.setOnMenuItemClickListener(item -> homeMenuItemPressed());

    }

    private boolean homeMenuItemPressed() {

            //Removes any active fragments to go back to the base activity interface
            if (currentFragment != null) {
                accountActivity.getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                currentFragment = null;
            }

            MainFragment newFragment = new MainFragment();
            currentFragment = newFragment;
            accountActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).addToBackStack("").commit();

            drawer.closeDrawers();
            toolbar.setTitle("QuestGraph");
            return true;
    }
}
