package com.questgraph.database;

import com.questgraph.ui.AccountActivity;
import com.questgraph.control.Tools;
import com.questgraph.ui.AuthLoginActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    AppDatabase appDb;

    public void insertAccounts(String JSON) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            for (int i = 0; true; i++) {
                accounts.add(new Account(
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/type"),
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/number"),
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/status"),
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/isPrimary"),
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/isBilling"),
                        Tools.getValueFromJSON(JSON, "//" + i + "//accounts/clientAccountType")));
            }

        } catch (JSONException e) {
            System.out.println("Read in " + accounts.size() + " accounts");
        }

        appDb = AppDatabase.getInstance(AccountActivity.context);

        //if account doesn't already exist in database, add it
        for(int i = 0; i < accounts.size(); i++) {
            if(!appDb.accountDAO().accountExists(accounts.get(i).number)) {
                System.out.println("Adding account " + accounts.get(i).number + " into database");
                appDb.accountDAO().addAccount(accounts.get(i));
            } else {
                System.out.println("Account " + accounts.get(i).number + " already exists in database");
            }
        }

    }

    public List<Account> getAccounts() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.accountDAO().getAccountList();
    }

    public void updateAuthorization(String JSON) {
        Authorization authorization = null;
        try {
            authorization = new Authorization(
                    Tools.getValueFromJSON(JSON, "access_token"),
                    Tools.getValueFromJSON(JSON, "refresh_token"),
                    Tools.getValueFromJSON(JSON, "api_server")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateAuthorization(authorization);
    }

    void updateAuthorization(Authorization authorization) {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        appDb.authorizationDAO().resetAuthorization();
        appDb.authorizationDAO().insertAuthorization(authorization);
    }

    public void deleteAuthorization() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        appDb.authorizationDAO().resetAuthorization();
    }

    public String getAccessToken() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getAccessToken();
    }

    public String getRefreshToken() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getRefreshToken();
    }

    public String getApiServer() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getApiServer();
    }

    public boolean authorizationExists() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        if(appDb.authorizationDAO().getNumberOfRows() == 1) {
            return true;
        }

        return false;
    }





    public static DataManager getDataManager() {
        return new DataManager();
    }

}
