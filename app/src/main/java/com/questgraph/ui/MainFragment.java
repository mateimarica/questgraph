package com.questgraph.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.questgraph.R;
import com.questgraph.control.FileManager;
import com.questgraph.control.Tools;

import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment {

    private View view;
    private TextView welcomeText;

    /**
     * Loading dialog.
     */
    private ProgressDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = container;
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //placeholder
    }

    @Override
    public void onStart() {
        super.onStart();

        welcomeText = view.findViewById(R.id.welcomeText);
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        String timeOfDay = "";
        if(d.getHours() < 12) {
            timeOfDay = "morning";
        } else if (d.getHours() > 17) {
            timeOfDay = "evening";
        } else {
            timeOfDay = "afternoon";
        }

        welcomeText.setText("Good " + timeOfDay + ", chief.\nLet's see how your money is doing.");
    }

    //Refreshes graph on resume
    @Override
    public void onResume() {
        super.onResume();

        if(FileManager.darkThemeEnabled()) {
            view.setBackgroundColor(AccountActivity.darkThemeBackground);
            welcomeText.setTextColor(AccountActivity.darkThemeText);
        }
        //new SomeTask().execute(""); //TODO
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
            //placeholder
        }


        protected void onPostExecute(Boolean... result) {
            //placeholder
        }

    }

}