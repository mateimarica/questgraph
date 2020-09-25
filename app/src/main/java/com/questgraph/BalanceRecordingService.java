package com.questgraph;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.TimeZone;

public class BalanceRecordingService extends Worker {

    private static Context context;
    private static Context contextReal;
    private static Calendar lastRecording;
    private static final String TAG = "BalanceRecordingService";

    static Context getContext() {

        return context;
    }

    public BalanceRecordingService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

    }

    @NonNull
    @Override
    public Result doWork() {

        TimeZone.setDefault(TimeZone.getTimeZone("Canada/Atlantic"));

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        //NYSE, NASDAQ, and TSX are open from 10:30 to 17:00 Atlantic time, Mon-Fri
        if((day != 1 && day != 7) && ((hour >= 10 && hour < 17) || (hour == 17 && min <= 20))) {

            try {
                Tools.recordBalances();

            } catch (InvalidAccessTokenException e) {
                try {
                    Thread.sleep(6000);
                    Tools.recordBalances();
                } catch (InterruptedException | InvalidAccessTokenException e1) {

                    return Result.failure();
                }
            }

            System.out.println(this.context.getFilesDir().getAbsolutePath());
            return Result.success();

            //Log.e(TAG, "doWork: Work wasn't done - 20 minutes hasn't passed since last recording");
            //Log.e(TAG, "work done");

            //return Result.failure();
        } else {
            System.out.println("Balance was not recorded after-hours");
            return Result.failure();
        }

    }
}
