package com.questgraph;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static View view;
    private View divider;

    private Switch darkThemeSwitch;

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
}