package com.questgraph;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TimeZone;

public class AccountFragment extends Fragment {

    private TextView helpText, currencyDisplayText;
    private View view;
    private int accountNum;
    private String accountType;
    private final int green = Color.rgb(0, 175, 51);
    private final int red = Color.rgb(172, 41, 10);

    Spinner dropdown;
    GraphView graph = null;
    SimpleDateFormat compressedTime = new SimpleDateFormat("d-M-yyyy/H:m");

    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Double> totalCAD = new ArrayList<>();
    ArrayList<Double> totalUSD = new ArrayList<>();
    ArrayList<Double> combinedCAD = new ArrayList<>();
    ArrayList<Double> combinedUSD = new ArrayList<>();
    boolean nextOneInvisible;

    /**
     * Loading dialog.
     */
    private ProgressDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = container;
        return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            accountNum = Integer.parseInt(getArguments().getString("accountNum"));
            accountType = getArguments().getString("accountType");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        helpText = view.findViewById(R.id.help_text);
        helpText.setText("Account Overview");

        currencyDisplayText = view.findViewById(R.id.currenciesDisplayText);
        dropdown = view.findViewById(R.id.timePeriodSpinner);
        graph = view.findViewById(R.id.graph);

        loadingDialog = new ProgressDialog(AccountActivity.context);
        loadingDialog.setCancelable(false);
    }


    //Refreshes graph on resume
    @Override
    public void onResume() {
        super.onResume();
        new RefreshGraph().execute("");
    }

    /*public int getAccountNum() {
        return accountNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getID() {
        return accountNum + "";
    }*/

    class RefreshGraph extends AsyncTask<String, String, Boolean> {
        protected void onPreExecute () {

            loadingDialog.show();
            loadingDialog.setMessage("Generating graph...");
        }

        protected Boolean doInBackground(String... refreshToken) {
            System.out.println("Account Fragment opened");

            //Gets the CURRENT account balance
            try {
                //
                Tools.recordBalances();

                //For rounding CAD and USD to 2 decimal places (cents)
                DecimalFormat df = new DecimalFormat("#.##");

                String CAD = Tools.getValueFromJSON(
                            Tools.getBalanceJSON(accountNum + ""),"//0//perCurrencyBalances/totalEquity");
                CAD = df.format(Double.parseDouble(CAD)/2);


                String USD = Tools.getValueFromJSON(
                        Tools.getBalanceJSON(accountNum + ""),"//1//perCurrencyBalances/totalEquity");
                USD = df.format(Double.parseDouble(USD)/2);


                String balanceHist = Tools.getBalanceHistory(accountNum + "");
                Scanner scan = new Scanner(balanceHist);
                while(scan.hasNext()) {
                    Scanner sectionScan = new Scanner(scan.nextLine());

                    Date date = compressedTime.parse(sectionScan.next());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.YEAR, -50);
                    date = cal.getTime();

                    dates.add(date);

                    String JSONContents = sectionScan.next();
                    totalCAD.add(Double.parseDouble(Tools.getValueFromJSON(JSONContents, "//0//perCurrencyBalances/totalEquity"))/2);
                    totalUSD.add(Double.parseDouble(Tools.getValueFromJSON(JSONContents, "//1//perCurrencyBalances/totalEquity")));
                    combinedCAD.add(Double.parseDouble(Tools.getValueFromJSON(JSONContents, "//0//combinedBalances/totalEquity")));
                    combinedUSD.add(Double.parseDouble(Tools.getValueFromJSON(JSONContents, "//1//combinedBalances/totalEquity")));
                }

                publishProgress(CAD, USD);

            } catch(InvalidAccessTokenException e) {
                System.out.println("InvalidAccessTokenException thrown when getting account " +
                                   "balances in AccountFragment.java: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("ParseException thrown when getting account " +
                        "balances in AccountFragment.java: " + e.getMessage());
            }

            return true;
        }

        //balances[0] = current CAD
        //balances[1] = current USD
        protected void onProgressUpdate(String... balances) {
            //System.out.println("BALANCES:" + balances[0]);
           // Scanner scan = new Scanner(balances[0]);

            switch(dropdown.getSelectedItem().toString()) {
                case "Past Day":
                    generateGraph(1, balances);
                    break;
                case "Past 5 Days":
                    generateGraph(5, balances);
                    break;
                case "Past Month":
                    generateGraph(30, balances);
                    break;
                case "Past Year":
                    generateGraph(365, balances);
                    break;
                case "All Time":
                    generateGraph(0, balances);
                    break;
            }

            loadingDialog.dismiss();
        }

        protected void onPostExecute(Boolean... result) {

        }
    }

    private void generateGraph(int periodInDays, String[] currentBalances) {
        TimeZone.setDefault(TimeZone.getTimeZone("Canada/Atlantic"));
        Calendar cal = Calendar.getInstance();

        //Displays current $ in CAD and USD
        currencyDisplayText.setText("CAD: $" + currentBalances[0] + "\nUSD: $" + currentBalances[1]);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPointArr);

        series.setColor(green);
        series.setThickness(20);
        graph.addSeries(series);*/

        GraphView graph = (GraphView) this.graph;
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });*/

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

        double firstValue = totalCAD.get(0);
        double lastValue = totalCAD.get(totalCAD.size() - 1);

        for(int i = 0; i < dates.size(); i++) {
            series.appendData(new DataPoint(dates.get(i), totalCAD.get(i)), true, 10000);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    if(nextOneInvisible) {
                        nextOneInvisible = false;
                        return "";
                    }
                    // show normal x values
                    //super.formatLabel(value, isValueX) = milliseconds with commas
                    String removeCommas = super.formatLabel(value, isValueX).replaceAll("," , "");
                    long milliAsInt = Long.parseLong((removeCommas));
                    Date dateOfXValue = new Date(milliAsInt);
                    SimpleDateFormat onlyHoursandMinsFormat = new SimpleDateFormat("h:mm");
                    nextOneInvisible = true;
                    return onlyHoursandMinsFormat.format(dateOfXValue);
                } else {
                    // show currency for y values
                    return "$" + super.formatLabel(value, isValueX);
                }
            }
        });

        series.setThickness(10);

        if(lastValue - firstValue > 0) {
            series.setColor(green);
        } else {
            series.setColor(red);
        }
        //series.setAnimated(true);

        graph.addSeries(series);

        //graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(9); // only 4 because of the space

    }
}