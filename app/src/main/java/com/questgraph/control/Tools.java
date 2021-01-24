package com.questgraph.control;

/*import com.questgraph.database.Account;
import com.questgraph.database.DataManager;
import com.questgraph.exception.BadResponseCodeException;
import com.questgraph.exception.InvalidAccessTokenException;
import com.questgraph.exception.InvalidManualAuthTokenException;
import com.questgraph.ui.AuthLoginActivity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONException;*/

public class Tools {


    //private static File filesDir = BalanceRecordingService.context.getFilesDir();

    //private static boolean isInternetConnection = true;
    //static boolean accessedByBackgroundService;


    //private static File initFile = new File (filesDir + "/init.json");
    //private static File accFile = new File (filesDir + "/accounts.json");



    /**
     * General path for each account's balance history. Filepath = /balanceHist[accountNum]
     */
    //private static String balancesPath = filesDir + "/balanceHist";

    //Unused b/c accounts are determined at runtime
    //private static File balanceHistoryFile = new File (balancesPath);

    //final static SimpleDateFormat compressedTime = new SimpleDateFormat("d-M-yyyy/H:m");

    /*private static File getBalancesFile(String accountNum) {
        return new File(balancesPath + accountNum);
    }*/

    //checks if the balances file exists for the first account, which means they all exist
    /*private static boolean balanceFilesExist() {
        try {
            return (new File(balancesPath + getAccounts().get(0))).exists();
        } catch (InvalidAccessTokenException e) {
            e.printStackTrace();
            return false;
        }
    }*/






    /*public static boolean authorizationExists() {
        return new DataManager().authorizationExists();
    }*/
    /**
     * Checks if init.json exists (means that access token already exists)
     * @return True or false depending on if init.json exists
     */
    /*public static boolean initFileExists() {
        return initFile.exists();
    }*/

    /**
     * Returns the refresh token from init.json
     * @return The refresh token.
     */
    /*public static String getRefreshToken(){
        return new DataManager().getRefreshToken();
    }*/

    /**
     * Resets the general files, but not the account balance history files
     */
    /*public static void resetFiles() {
        if(initFile.exists()) {
            initFile.delete();
            System.out.println("init.json deleted.");
        }
        if(accFile.exists()) {
            accFile.delete();
            System.out.println("accounts.json deleted.");
        }
        /*if(balancesFile.exists()) {
            balancesFile.delete();
            System.out.println("balances.json deleted.");
        }

    }*/

    /**
     * Gets access token using auth token and creates init.json
     * @return The access token
     * @throws InvalidManualAuthTokenException If authorization token doesn't work.
     */
    /*public static String getAccessToken() {
        return new DataManager().getAccessToken();
    }*/



    /*public static String getApiServer() {
        return new DataManager().getApiServer();
    }*/

    /**
     * Tries the access token or refresh token from the file. Always gets new account file.
     * @return ArrayList containing account nums and types. Eg: ArrayList = {account1Num, account1Type, account2Num, account2Type}
     * @throws InvalidAccessTokenException If the access token is invalid
     */
    /*public static List<Account> getAccounts() throws InvalidAccessTokenException {


        /*try {
            Scanner scan = new Scanner(accFile);
            JSONObject accounts = new JSONObject(scan.next());
            scan.close();
            JSONArray accountsArray = accounts.getJSONArray("accounts");

            for (int i = 0; true; i++) {
                //each account's number is at the
                accountNums.add(accountsArray.getJSONObject(i).getString("number"));
                accountNums.add(accountsArray.getJSONObject(i).getString("type"));

                new DataManager().insertAccount(accounts.toString());
            }

        } catch (ArrayIndexOutOfBoundsException | JSONException | FileNotFoundException e) {
            //TODO Should probably put something here, but under proper conditions the program shouldn't reach this
            //Actually, who knows
        }

        List<Account> accounts = new DataManager().getAccounts();
        return accounts;
    }*/

    //delete old authorization if refresh token fails
    /*public static void deleteAuthorization() {
        new DataManager().deleteAuthorization();
    }*/







    /**
     * Appends a string to a given file.
     * @param fileContents The string that is to be appended to the file.
     * @param destination The destination file.
     */
    /*private static void appendToFile(String fileContents, File destination) {
        FileWriter fileWriter = null;
        System.out.println("Appending history to file...");
        try {
            fileWriter = new FileWriter(destination.getPath(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.write("\n" + fileContents);  //New line
        printWriter.flush();
        printWriter.close();

        System.out.println("Successfully appended history to file...");
    }*/




    /**
     * Requests and returns a CURRENT balance JSON for a specific account.
     * @param accNum The number of the account whose balance is being accessed.
     * @return The balance info for the specific account
     * @throws IOException Never
     */
    /*public static String getBalanceJSON(String accNum) throws InvalidAccessTokenException {
        BufferedReader in = null;

        try {

            in = connectToURL(getValueFromJSON(initFile, "api_server")
                            + "v1/accounts/"
                            + accNum
                            + "/balances", getValueFromJSON(initFile, "access_token"));

        } catch (BadResponseCodeException e) {
            System.out.println("BadResponseCodeException" +e.getMessage());
            throw new InvalidAccessTokenException();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String JSONContents = null;

        try {
             JSONContents = in.readLine();


            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //No internet, so return last record in balance history.

            String balanceHistory = null; //getBalanceHistory(accNum);
            if(balanceHistory == null) {
                return null;
            }

            Scanner scan = new Scanner(balanceHistory);

            //JSONContent =  This is one line of the balance history for this account;

            while(scan.hasNextLine()) {

                JSONContents = scan.nextLine();
                System.out.println(JSONContents);
            }

            //Getting rid of date and just getting information
            scan = new Scanner(JSONContents);
            scan.next();
            JSONContents = scan.next();

            scan.close();

            isInternetConnection = false;
        }

       return JSONContents;
    }*/

    //Records current real-time balances and saves them to a file
    //returns true if balances successfully recorded
    //false is not recorded
    /*public static boolean recordBalances() throws InvalidAccessTokenException {

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        boolean balanceFileBeingMadeNow = !balanceFilesExist();

        if(balanceFileBeingMadeNow) {
            System.out.println("Balance file does not exist... Creating balance file(s) for first time");
        }

        //If after hours, don't record balances. But if no account file exists, proceed anyway
        if(!((day != 1 && day != 7) && ((hour >= 10 && hour < 17) || (hour == 17 && min <= 20)))
            && !balanceFileBeingMadeNow) {
            System.out.println("Balance was not recorded after-hours");
            return false;
        }

        System.out.println("Recording balances...");

        //old line
        //ArrayList<String> accountNums = accountNums = getAccounts();
        ArrayList<Account> accountNums = new ArrayList<>(getAccounts());

        //EXPERIMENT FOR FUTURE RETROACTIVE GRAPHING
        /*try {
            System.out.println("Phase 1");
            BufferedReader in = connectToURL(getValueFromJSON(initFile, "api_server")
                    + "v1/accounts/"
                    + accountNums.get(0)
                    + "/activities", getValueFromJSON(initFile, "access_token"), "2020-08-16", "2020-09-19");
            System.out.println("Phase 2");
            String CONTENTS = in.readLine();
            System.out.println("Phase 3");
            writeToFile(CONTENTS, new File(filesDir + "/executions"));
            System.out.println("Execution success");
        } catch (BadResponseCodeException | IOException e) {
            e.printStackTrace();
        }
        // put * / here
        //Uses Atlantic time as standardized timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Canada/Atlantic"));

        //Compressed time/date
        String timeStr = compressedTime.format(cal.getTime());

        //Making sure 5 minutes have passed before recording new balance
        if(new File(balancesPath + accountNums.get(0)).exists()) {
            Scanner scan = new Scanner(getBalanceHistory(getAccounts().get(0)));
            String newestTimeInHistoryFile = "";
            while (scan.hasNextLine()) {
                newestTimeInHistoryFile = scan.next();
                scan.nextLine();
            }

            Date oldTime = null, newTime = null;
            try {
                oldTime = compressedTime.parse(newestTimeInHistoryFile);
                newTime = compressedTime.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //if the balance file doesn't exist now, let program know that this is first time being made
            //name is verbose and sucks, fix later



            //if balance file doesn't exist, proceed anyway
            if (newTime.getTime() - oldTime.getTime() < (1000 * 60 * 5)) {
                System.out.println("Didn't successfully record because less than 5 minutes had passed since last recording");
                return false;
            }
        }

        //accountsNum array looks like {acc1Num, acc1Type, acc2Num, acc2Type, .....}
        for(int i = 0; i < accountNums.size(); i += 2) {
            File accFile = new File(balancesPath + accountNums.get(i));
            String balanceInfo = getBalanceJSON(accountNums.get(i));

            //If no internet
            if(isInternetConnection == false) {
                isInternetConnection = true;
                System.out.println("No internet... Didn't record new balances.");
                return false;
            }

            String dateAndBalance = timeStr + " " + balanceInfo;

            if(!accFile.exists()) {
                writeToFile(dateAndBalance, accFile);
            } else {
                appendToFile(dateAndBalance, accFile);
            }
        }

        System.out.println("Successfully recorded balances.");

        //successfully returned balances
        return true;
    }*/

    //Gets the total history of the balances from the /balanceHist[accountNum] directory
    /*public static String getBalanceHistory(String accNum) {
        Scanner scan = null;

        try {
            scan = new Scanner(new File(balancesPath + accNum));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read the total balance history...");
            e.printStackTrace();
            return null;
        }

        String totalHistoryStr = "";
        if(scan.hasNextLine()) {
            totalHistoryStr += scan.nextLine();
        }
        while(scan.hasNextLine()) {
            totalHistoryStr += "\n" + scan.nextLine();
        }

        return totalHistoryStr;
    }*/
}