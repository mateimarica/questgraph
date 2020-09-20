package com.questgraph;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class SettingsFragment extends Fragment {
    final static int darkThemeSwitchHighlight = Color.rgb(255, 255, 255);


    private static View view;
    private View divider;

    private Switch darkThemeSwitch;



    /**
     * Loading dialog.
     */
    private ProgressDialog loadingDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = container;

        return inflater.inflate(R.layout.fragment_settings, container, false);

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.settingsFileExists();

        /*mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });*/



    }


    @Override
    public void onStart() {
        super.onStart();

        darkThemeSwitch = view.findViewById(R.id.darkModeSwitch);
        divider = view.findViewById(R.id.divider);
        if(Tools.darkThemeEnabled()) {
            turnOnDarkTheme();
        } else {
            turnOffDarkTheme();
        }

        //if(preferences.getBoolean("darkTheme", true)) {



        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked) {

                    Tools.updateDarkTheme(true);
                    AccountActivity.restartSettings = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);






                } else {
                    Tools.updateDarkTheme(false);

                    AccountActivity.restartSettings = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });


    }
    private void turnOffDarkTheme() {
        view.setBackgroundColor(AccountActivity.lightThemeBackground);
        AccountActivity.navigationView.setBackgroundColor(AccountActivity.lightThemeBackground);
        darkThemeSwitch.setTextColor(AccountActivity.lightThemeText);
    }
    private void turnOnDarkTheme() {
        view.setBackgroundColor(AccountActivity.darkThemeBackground);
        AccountActivity.navigationView.setBackgroundColor(AccountActivity.darkThemeBackground);
        darkThemeSwitch.setTextColor(AccountActivity.darkThemeText);
        darkThemeSwitch.setChecked(true);
        divider.setBackgroundColor(AccountActivity.darkThemeElements);

        ColorStateList thumbStates = new ColorStateList(
                new int[][]{

                        new int[]{android.R.attr.state_checked},  //MAIN CHECKED COLOR
                        new int[]{} //MAIN UNCHECKED COLOR
                },
                new int[]{

                        Color.rgb(37, 120, 193), //MAIN CHECKED COLOR
                        Color.rgb(255, 255,255),//MAIN UNCHECKED COLOR
                        //CHECKED TRACK COLOR DEFINED IN XML
                }
        );
        darkThemeSwitch.setThumbTintList(thumbStates);
    }


    //Refreshes graph on resume
    @Override
    public void onResume() {
        super.onResume();
        //new SomeTask().execute(""); //TODO ghhdfh
    }


    class SomeTask extends AsyncTask<String, String, Boolean> {
        protected void onPreExecute () {

            loadingDialog.show();
            loadingDialog.setMessage("Generating graph...");

        }


        protected Boolean doInBackground(String... refreshToken) {


            publishProgress("");

            return true;
        }






        protected void onProgressUpdate(String... balances) {




        }


        protected void onPostExecute(Boolean... result) {

        }

    }





}