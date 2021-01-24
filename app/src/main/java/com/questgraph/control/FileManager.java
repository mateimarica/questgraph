package com.questgraph.control;

import com.questgraph.ui.AuthLoginActivity;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileManager {

    /**
     * The root file directory for this app.
     */
    private static File filesDir = getFileDirectory();

    private static File getFileDirectory() {
        //If there's an error here, then only the background service is active
        try{
            return AuthLoginActivity.context.getFilesDir();
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean darkThemeEnabled() {
        try {
            return Boolean.parseBoolean(JSONManager.getValueFromJSON(settingsFile, "darkThemeEnabled"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No settings file found");
        }
        return false;
    }

    /**
     * All the files that are saved and used.
     */
    private static File settingsFile = new File (filesDir + "/settings.json");


    public static void settingsFileExists() {
        if(!settingsFile.exists()) {
            writeToFile("{\"darkThemeEnabled\":false}", settingsFile);
            System.out.println("Settings file created.");
        }
    }

    public static void updateDarkTheme(boolean enabled) {
        writeToFile("{\"darkThemeEnabled\":" + enabled + "}",  settingsFile);
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
}
