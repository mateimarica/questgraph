package com.questgraph;

import android.provider.ContactsContract;

import androidx.room.Room;

import java.util.List;

public class DataManager {
    void insertAccount(String JSON) {

        Account account = new Account(
                Tools.getValueFromJSON(JSON, "//0//accounts/type"),
                Tools.getValueFromJSON(JSON, "//0//accounts/number"),
                Tools.getValueFromJSON(JSON, "//0//accounts/status"),
                Tools.getValueFromJSON(JSON, "//0//accounts/isPrimary"),
                Tools.getValueFromJSON(JSON, "//0//accounts/isBilling"),
                Tools.getValueFromJSON(JSON, "//0//accounts/clientAccountType"));
        AppDatabase appDb = AppDatabase.getInstance(AccountActivity.context);

        //if account doesn't already exist in database, add it
        if(!appDb.accountDAO().accountExists(account.number)) {
            System.out.println("Adding account " + account.number + " into database");
            appDb.accountDAO().addAccount(account);
        } else {
            System.out.println("Account " + account.number + " already exists in database");
        }

        //List<Account> list = appDb.accountDAO().getAccountList();
        //System.out.println("list = " + list.get(0).type);

    }
    static DataManager getDataManagerInstance() {
        return new DataManager();
    }

}
