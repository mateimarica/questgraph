package com.questgraph.control;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONManager {
    /**
     * Gets the value associated with the given key path in the given JSON file.
     * Eg: "//0//accounts/number" --> //0// before the accounts means accounts is an array. //0//accounts means the 0th index of accounts.
     *                                //0//accounts/number means find the value for the number key in the 0th index of the accounts array.
     * @param JSONIn The JSON file from which the value is to be retrieved.
     * @param key The key whose value is to be returned.
     * @return The value associated with the given key.
     */
    static String getValueFromJSON (File JSONIn, String key) throws JSONException {

        Scanner scan = null;
        try {
            scan = new Scanner(JSONIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return getValueFromJSON(scan.nextLine(), key);
    }

    /**
     * Gets the value associated with the given key path in the given JSON file.
     * Eg: "//0//accounts/number" --> //0// before the accounts means accounts is an array. //0//accounts means the 0th index of accounts.
     *                                //0//accounts/number means find the value for the number key in the 0th index of the accounts array.
     * @param JSONContents A string containing JSON format data.
     * @param key The key whose value is to be returned.
     * @return The value associated with the given key.
     */
    public static String getValueFromJSON(String JSONContents, String key) throws JSONException {
        String value = "error";


        Scanner scan;
        String currentKey;

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

         /*catch (JSONException e) {
            //System.err.println("JSONException occurred in getValueFromJSON() " + e.getMessage());
            return null;
        }*/
        return value;
    }
}
