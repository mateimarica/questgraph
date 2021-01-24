package com.questgraph.database;

import com.questgraph.control.JSONManager;
import com.questgraph.ui.AccountActivity;
import com.questgraph.control.Tools;
import com.questgraph.ui.AuthLoginActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private DataManager() {}

    private static AppDatabase appDb;

    public static void insertAccounts(String JSON) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            for (int i = 0; true; i++) {
                accounts.add(new Account(
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/type"),
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/number"),
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/status"),
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/isPrimary"),
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/isBilling"),
                        JSONManager.getValueFromJSON(JSON, "//" + i + "//accounts/clientAccountType")));
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

    public static List<Account> getAccounts() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.accountDAO().getAccountList();
    }

    public static void updateAuthorization(String JSON) {
        Authorization authorization = null;
        try {
            authorization = new Authorization(
                    JSONManager.getValueFromJSON(JSON, "access_token"),
                    JSONManager.getValueFromJSON(JSON, "refresh_token"),
                    JSONManager.getValueFromJSON(JSON, "api_server")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateAuthorization(authorization);
    }

    public static void updateAuthorization(Authorization authorization) {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        appDb.authorizationDAO().resetAuthorization();
        appDb.authorizationDAO().insertAuthorization(authorization);
    }

    public static void deleteAuthorization() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        appDb.authorizationDAO().resetAuthorization();
    }

    public static String getAccessToken() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getAccessToken();
    }

    public static String getRefreshToken() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getRefreshToken();
    }

    public static String getApiServer() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        return appDb.authorizationDAO().getApiServer();
    }

    public static boolean authorizationExists() {
        appDb = AppDatabase.getInstance(AuthLoginActivity.context);
        if(appDb.authorizationDAO().getNumberOfRows() == 1) {
            return true;
        }

        return false;
    }


}
