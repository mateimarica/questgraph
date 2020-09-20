package com.questgraph;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Tools {


    /**
     * The root file directory for this app.
     */
    private static File filesDir = getFileDirectory();
    //private static File filesDir = BalanceRecordingService.context.getFilesDir();

    private static boolean isInternetConnection = true;
    //static boolean accessedByBackgroundService;

    /**
     * All the files are saved and used.
     */
    private static File initFile = new File (filesDir + "/init.json");
    private static File accFile = new File (filesDir + "/accounts.json");
    private static File settingsFile = new File (filesDir + "/settings.json");



    /**
     * General path for each account's balance history. Filepath = /balanceHist[accountNum]
     */
    static String balancesPath = filesDir + "/balanceHist";

    //Unused b/c accounts are determined at runtime
    //private static File balanceHistoryFile = new File (balancesPath);


    final static SimpleDateFormat compressedTime = new SimpleDateFormat("d-M-yyyy/H:m");

    static void settingsFileExists() {
        if(!settingsFile.exists()) {
            writeToFile("{\"darkThemeEnabled\":false}", settingsFile);
            System.out.println("Settings file created.");
        }
    }
    static void updateDarkTheme(boolean enabled) {
        writeToFile("{\"darkThemeEnabled\":" + enabled + "}",  settingsFile);
    }

    static boolean darkThemeEnabled() {
        return Boolean.parseBoolean(getValueFromJSON(settingsFile, "darkThemeEnabled"));
    }

    private static File getFileDirectory() {
        //If there's an error here, then only the background service is active
        try{
            return AuthLoginActivity.getContext().getFilesDir();
        } catch(Exception e) {
            return BalanceRecordingService.getContext().getFilesDir();
        }


    }

    /**
     * Checks if init.json exists (means that access token already exists)
     * @return True or false depending on if init.json exists
     */
    static boolean initFileExists() {
        return initFile.exists();
    }



    /**
     * Returns the refresh token from init.json
     * @return The refresh token.
     */
    static String getRefreshToken(){
        return getValueFromJSON(initFile, "refresh_token");
    }

    /**
     * Resets the general files, but not the account balance history files
     */
    static void resetFiles() {
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
        }*/

    }


    /**
     * Gets access token using auth token and creates init.json
     * @param authToken The manual authorization token (or the refresh token)
     * @return The access token
     * @throws InvalidManualAuthTokenException If authorization token doesn't work.
     */


    static String getAccessToken(String authToken) throws InvalidManualAuthTokenException {




        BufferedReader in;

        try {
            in = connectToURL("https://login.questrade.com/oauth2/token?grant_type=refresh_token&refresh_token=" + authToken);
        } catch (BadResponseCodeException e) {



            throw new InvalidManualAuthTokenException(e.getMessage() + "\nAuth token is invalid.");


        }


        //saves to json
        try {

            writeToFile(in.readLine(), initFile);
            System.out.println("init.json created");
            in.close();
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }


        return getValueFromJSON(initFile, "access_token");

    }


    /**
     * Tries the access token or refresh token from the file. Always gets new account file.
     * @return ArrayList containing account nums and types. Eg: ArrayList = {account1Num, account1Type, account2Num, account2Type}
     * @throws InvalidAccessTokenException If the access token is invalid
     */
    static ArrayList<String> getAccounts() throws InvalidAccessTokenException {


        BufferedReader in;
        ArrayList<String> accountNums = new ArrayList<>();

        try {

            in = connectToURL(getValueFromJSON(initFile, "api_server")
                            + "v1/accounts/",
                    getValueFromJSON(initFile, "access_token"));

        } catch (BadResponseCodeException e) {
            throw new InvalidAccessTokenException(e.getMessage() + "\nAccess token is invalid or has expired.");
        }
        try {
            //saves to json
            try {
                writeToFile(in.readLine(), accFile);
                System.out.println("accounts.json created");
                in.close();
            } catch (NullPointerException e) {
                System.out.println("NullPointerException when reading accounts file... No internet? Using file that already exists.");
            }


        } catch (IOException e) {
            System.err.println("IOException occured in getAccInfo(): " + e.getMessage());
        }

        try {
            Scanner scan = new Scanner(accFile);
            JSONObject accounts = new JSONObject(scan.next());
            scan.close();
            JSONArray accountsArray = accounts.getJSONArray("accounts");


            for (int i = 0; true; i++) {
                //each account's number is at the
                accountNums.add(accountsArray.getJSONObject(i).getString("number"));
                accountNums.add(accountsArray.getJSONObject(i).getString("type"));
            }

        } catch (ArrayIndexOutOfBoundsException | JSONException | FileNotFoundException e) {
            //TODO Should probably put something here, but under proper conditions the program shouldn't reach this
        }


        return accountNums;

    }

    /**
     * Connect to a URL that doesn't require an access token.
     * @param URLIn The URL that is to be connected to.
     * @return Returns a reference to the BufferedReader from info can be read from.
     * @throws BadResponseCodeException If the website's response code is not 200 (200 = OK)
     */
    private static BufferedReader connectToURL(String URLIn) throws BadResponseCodeException {
        return connectToURL(URLIn, null);

    }

    /**
     * Connect to a URL that requires an access token.
     * @param URLIn The URL that is to be connected to.
     * @param accessToken The access token.
     * @return Returns a reference to the BufferedReader from info can be read from.
     * @throws BadResponseCodeException If the website's response code is not 200 (200 = OK)
     */
    private static BufferedReader connectToURL(String URLIn, String accessToken) throws BadResponseCodeException {

        return connectToURL(URLIn, accessToken, null, null);

    }
    private static BufferedReader connectToURL(String URLIn, String accessToken, String startTime, String endTime) throws BadResponseCodeException {

        BufferedReader in = null;

        try {

            //FOR FUTURE RETROACTIVE GRAPHING
            /*if(startTime != null) {
                URLIn += "?startTime=2020-08-23T21:14:07+00:00";
                System.out.println(URLIn);
            }*/


            URL URL = new URL(URLIn);


            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();


            if (accessToken != null) {

                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                //connection.setDoOutput(true);
                connection.setRequestMethod("GET");
            }

            if (connection.getResponseCode() != 200) {

                throw new BadResponseCodeException(" Bad response code " + connection.getResponseCode() + " recieved."
                        + "\nReason: " + connection.getResponseMessage());
            }
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return in;

    }




    /**
     * Saves a string to a given file.
     * @param fileContents The string that is to be written to the file.
     * @param destination The destination file.
     */
    private static void writeToFile(String fileContents, File destination) {
        try {
            PrintWriter outFile = new PrintWriter(destination);
            outFile.write(fileContents);
            outFile.flush();
            outFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File could not be written at " + destination.getAbsolutePath() + ". " + e.getMessage());
        }

    }

    /**
     * Appends a string to a given file.
     * @param fileContents The string that is to be appended to the file.
     * @param destination The destination file.
     */
    private static void appendToFile(String fileContents, File destination) {
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
    }

    /**
     * Gets the value associated with the given key path in the given JSON file.
     * Eg: "//0//accounts/number" --> //0// before the accounts means accounts is an array. //0//accounts means the 0th index of accounts.
     *                                //0//accounts/number means find the value for the number key in the 0th index of the accounts array.
     * @param JSONIn The JSON file from which the value is to be retrieved.
     * @param key The key whose value is to be returned.
     * @return The value associated with the given key.
     */
    static String getValueFromJSON (File JSONIn, String key) {
        String value = "error";

        try {
            Scanner scan = new Scanner(JSONIn);
            String currentKey, JSONContents = scan.nextLine();

            scan.close();

            if(JSONContents.equals("")) {
                return null;
            }

            //commented-out code is for checking what happens when both access and refresh token don't work.
            //String a = "{\"access_token\":\"6pI2KSiGztS-kGd-86-zmlfA18Bk-el0\",\"refresh_token\":\"CfzndDcYyw86AEse0i1YJQHq4CdPR-70\",\"api_server\":\"https://api03.iq.questrade.com/\",\"token_type\":\"Bearer\",\"expires_in\":1800}";
            //JSONObject JSONFile = new JSONObject(a);
            JSONObject JSONFile = new JSONObject(JSONContents);
            scan = new Scanner(key).useDelimiter("/");

            while (true) {

                currentKey = scan.next();
                key = key.substring(currentKey.length());

                //if finish parsing JSON
                if (key.length() == 0) {
                    value = JSONFile.getString(currentKey);
                    break;

                    //if next part of JSON is array
                } else if (currentKey.equals("")) {
                    int arrayIndex = scan.nextInt();
                    scan.next();
                    currentKey = scan.next();

                    JSONFile = JSONFile.getJSONArray(currentKey).getJSONObject(arrayIndex);

                } else {
                    if (scan.hasNext()) {
                        JSONFile = JSONFile.getJSONObject(currentKey);
                    } else {

                        value = JSONFile.get(currentKey) + "";
                        break;
                    }
                }

            }

            scan.close();

        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException occurred in getValueFromJSON() " + e.getMessage());
        } catch (JSONException e) {
            //System.err.println("JSONException occurred in getValueFromJSON() " + e.getMessage());
            return null;
        }
        return value;
    }

    static String getValueFromJSON (String JSONIn, String key) {
        String value = "error";

        try {
            Scanner scan = new Scanner(JSONIn);
            String currentKey, JSONContents = scan.nextLine();

            scan.close();

            if(JSONContents.equals("")) {
                return null;
            }

            //commented-out code is for checking what happens when both access and refresh token don't work.
            //String a = "{\"access_token\":\"6pI2KSiGztS-kGd-86-zmlfA18Bk-el0\",\"refresh_token\":\"CfzndDcYyw86AEse0i1YJQHq4CdPR-70\",\"api_server\":\"https://api03.iq.questrade.com/\",\"token_type\":\"Bearer\",\"expires_in\":1800}";
            //JSONObject JSONFile = new JSONObject(a);
            JSONObject JSONFile = new JSONObject(JSONContents);
            scan = new Scanner(key).useDelimiter("/");

            while (true) {

                currentKey = scan.next();
                key = key.substring(currentKey.length());

                //if finish parsing JSON
                if (key.length() == 0) {
                    value = JSONFile.getString(currentKey);
                    break;

                    //if next part of JSON is array
                } else if (currentKey.equals("")) {
                    int arrayIndex = scan.nextInt();
                    scan.next();
                    currentKey = scan.next();

                    JSONFile = JSONFile.getJSONArray(currentKey).getJSONObject(arrayIndex);

                } else {
                    if (scan.hasNext()) {
                        JSONFile = JSONFile.getJSONObject(currentKey);
                    } else {

                        value = JSONFile.get(currentKey) + "";
                        break;
                    }
                }

            }

            scan.close();


        } catch (JSONException e) {
            //System.err.println("JSONException occurred in getValueFromJSON() " + e.getMessage());
            return null;
        }
        return value;
    }


    /**
     * Requests and returns a CURRENT balance JSON for a specific account.
     * @param accNum The number of the account whose balance is being accessed.
     * @return The balance info for the specific account
     * @throws IOException Never
     */
    static String getBalanceJSON(String accNum) throws InvalidAccessTokenException {
        BufferedReader in = null;


        try {

            in = connectToURL(getValueFromJSON(initFile, "api_server")
                            + "v1/accounts/"
                            + accNum
                            + "/balances", getValueFromJSON(initFile, "access_token"));

        } catch (BadResponseCodeException e) {
            System.out.println("BadResponseCodeException" +e.getMessage());
            throw new InvalidAccessTokenException();
        }
        String JSONContents = null;

        try {
             JSONContents = in.readLine();


            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //No internet, so return last record in balance history.

            //
            Scanner scan = new Scanner(getBalanceHistory(accNum));

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



    }


    static void recordBalances() throws InvalidAccessTokenException {

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);




        if(!((day != 1 && day != 7) && ((hour >= 10 && hour < 17) || (hour == 17 && min <= 20)))) {
            System.out.println("Balance was not recorded after-hours");
            return;
        }


        System.out.println("Recording balances...");



        ArrayList<String> accountNums = accountNums = getAccounts();

        //EXPERIMENT FOR FUTURE RETROACTIVE GRAPHING
        /*try {
            System.out.println("TRY???");
            BufferedReader in = connectToURL(getValueFromJSON(initFile, "api_server")
                    + "v1/accounts/"
                    + accountNums.get(0)
                    + "/activities", getValueFromJSON(initFile, "access_token"), "2020-08-16", "2020-09-19");
            System.out.println("TRY???2");
            String CONTENTS = in.readLine();
            System.out.println("TRY???3");
            writeToFile(CONTENTS, new File(filesDir + "/executions"));
            System.out.println("successssssssssssssssssssssss EXECUTINOS");
        } catch (BadResponseCodeException | IOException e) {
            e.printStackTrace();
        }*/


        //Uses Atlantic time as standardized timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Canada/Atlantic"));

        //Gets current time


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

            if (newTime.getTime() - oldTime.getTime() < (1000 * 60 * 5)) {
                System.out.println("Didn't successfully record because less than 5 minutes had passed since last recording");
                return;
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
                return;
            }


            String dateAndBalance = timeStr + " " + balanceInfo;

            if(!accFile.exists()) {
                writeToFile(dateAndBalance, accFile);
            } else {
                appendToFile(dateAndBalance, accFile);
            }


        }

        System.out.println("Successfully recorded balances.");
    }



    //Gets the total history of the balances from the /balanceHist[accountNum] directory
    static String getBalanceHistory(String accNum) {
        Scanner scan = null;

        try {
            scan = new Scanner(new File(balancesPath + accNum));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read the total balance history...");
            e.printStackTrace();
        }

        String totalHistoryStr = "";
        if(scan.hasNextLine()) {
            totalHistoryStr += scan.nextLine();
        }
        while(scan.hasNextLine()) {
            totalHistoryStr += "\n" + scan.nextLine();
        }




        return totalHistoryStr;

    }





}