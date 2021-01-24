package com.questgraph.control;

import com.questgraph.database.DataManager;
import com.questgraph.exception.BadResponseCodeException;
import com.questgraph.exception.InvalidAccessTokenException;
import com.questgraph.exception.InvalidManualAuthTokenException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIManager {

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

    /**
     * Connect to a URL that requires a start date and an end date.
     * @param URLIn The URL that is to be connected to.
     * @param accessToken The access token.
     * @param startTime Start of time range in ISO format. By default – start of today, 12:00am. Eg of date: startTime=2020-08-23T21:14:07+00:00
     * @param endTime End of time range in ISO format. By default – end of today, 11:59pm
     * @return Returns a reference to the BufferedReader from info can be read from.
     * @throws BadResponseCodeException If the website's response code is not 200 (200 = OK)
     */
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

    public static void retrieveAccounts() throws InvalidAccessTokenException {
        BufferedReader in = null;
        try {
            in = connectToURL(DataManager.getApiServer() + "v1/accounts/", DataManager.getAccessToken());

        } catch (BadResponseCodeException e) {
            throw new InvalidAccessTokenException(e.getMessage() + "\nAccess token is invalid or has expired.");
        }

        try {
            DataManager.insertAccounts(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveAuthorization(String authToken) throws InvalidManualAuthTokenException {
        BufferedReader in;

        try {
            in = connectToURL("https://login.questrade.com/oauth2/token?grant_type=refresh_token&refresh_token=" + authToken);
        } catch (BadResponseCodeException e) {
            throw new InvalidManualAuthTokenException(e.getMessage() + "\nAuth token is invalid.");
        }

        //saves to database
        try {
            DataManager.updateAuthorization(in.readLine());
            System.out.println("Saved authorization in database");
            in.close();
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

