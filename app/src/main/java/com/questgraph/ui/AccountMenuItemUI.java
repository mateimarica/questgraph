package com.questgraph.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.questgraph.R;
import com.questgraph.database.Account;

public class AccountMenuItemUI extends MenuItemUI {

    private Account account;

    AccountMenuItemUI(AccountActivity accountActivity, Fragment currentFragment, DrawerLayout drawer, Toolbar toolbar, Account account) {
        super(accountActivity, currentFragment, drawer, toolbar);
        this.account = account;

        MenuItem accountMenuItem = accountActivity.accountGroup.add(account.number + "  —  " + account.type);
        accountMenuItem.setIcon(R.drawable.ic_account);
        accountMenuItem.setCheckable(true);
        accountMenuItem.setChecked(false);

        accountMenuItem.setOnMenuItemClickListener(item -> accountMenuItemPressed());

    }

    boolean accountMenuItemPressed() {

        //Makes sure not to open the same fragment again if it is selected in the drawer and it's already open.
        if(currentFragment != null) {

            accountActivity.getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            currentFragment = null;
        }

        toolbar.setTitle(account.number + "  –  " + account.type);

        Bundle infoBundle = new Bundle();
        infoBundle.putString("accountNum", account.number + "");
        infoBundle.putString("accountType", account.type);

        AccountFragment newFragment = new AccountFragment();
        newFragment.setArguments(infoBundle);
        currentFragment = newFragment;
        accountActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment, account.number + "").addToBackStack("").commit();

        drawer.closeDrawers();

        return true;
    }
}