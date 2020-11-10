package com.questgraph;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AuthLoginActivity extends AppCompatActivity  {

    /**
     * Reference to the context
     */
    static Context context;

    /**
     * The login button.
     */
    private Button authLoginBtn;

    /**
     * The button used for quickly pasting the authorization token.
     */
    private FloatingActionButton pasteBtn;

    /**
     * The text field where the authorization token is entered.
     */
    private EditText authTokenField;

    /**
     * The loading dialog that pops up when the login button is pressed.
     */
    private ProgressDialog loadingDialog;

    /**
     * The popup that appears at the bottom of the screen to show any errors logging in.
     */
    private Toast popup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        //The login button.
        authLoginBtn = findViewById(R.id.authLoginBtn);
        authLoginBtn.setEnabled(false);
        authLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();

                new AuthTokenTask().execute(authTokenField.getText().toString());
            }

        });

        //The text field where the authorization token is entered. The login button is disabled if it's empty.
        authTokenField = findViewById(R.id.authTokenField);
        authTokenField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(authTokenField.length() > 0) {
                    authLoginBtn.setEnabled(true);
                } else {

                    authLoginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Button for quickly pasting the authentication token.
        pasteBtn = findViewById(R.id.pasteBtn);
        pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                authTokenField.setText(item.getText());

            }
        });

        //The popup used for displaying errors
        popup = Toast.makeText(this, null, Toast.LENGTH_LONG);

        //Initializes the loading dialog.
        loadingDialog = new ProgressDialog(this);

        //References the context.
        context = this;

        //If this activity is recreated with a "refresh_token" intent, that means the refresh token was invalid,
        //and the appropriate message appears.
        if (getIntent().getStringExtra("refresh_token") != null) {
            popup.setText("Refresh token failed, please try again.");
            popup.show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        //Checks if init.json already exists. It contains the access and refresh tokens, so it will automatically login if found.
        if(Tools.initFileExists()) {
            startActivity(new Intent(this, AccountActivity.class));
            finish();
        }

    }

    //enum used to represent the current state of the login process.
    enum CurrentState {SUCCESSFUL, FAIL}

    /**
     * This task is executed when the login button is pressed. Checks validity of authorization token.
     */
    class AuthTokenTask extends AsyncTask<String, CurrentState, Boolean> {

        //Make the loading dialog appears
        protected void onPreExecute () {
            loadingDialog.setCancelable(false);
            loadingDialog.setTitle("Loading...");
            loadingDialog.setMessage("Retrieving access token...");
        }

        //Tries to get the access token with the authorization token.
        protected Boolean doInBackground(String... authToken) {

            try {
                Tools.getAccessToken(authToken[0]);
            } catch (InvalidManualAuthTokenException e) {
                publishProgress(CurrentState.FAIL);
                return false;
            }

            publishProgress(CurrentState.SUCCESSFUL);
            return true;
        }

        //If successful, go to next activity. If not, stay on this activity and show error popup.
        protected void onProgressUpdate(CurrentState... state) {

            switch(state[0]) {


                case SUCCESSFUL:
                    loadingDialog.dismiss();
                    startActivity(new Intent(context, AccountActivity.class));
                    finish();
                    break;
                case FAIL:
                    authTokenField.setText("");
                    loadingDialog.dismiss();
                    popup.setText("Authorization token failed, please try again.");
                    popup.show();
                    break;

            }
        }

        protected void onPostExecute(boolean result) {

        }
    }

}