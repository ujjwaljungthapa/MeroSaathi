package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BusTimeFragment extends Fragment {
    String initial_stop, final_stop;
    float total_time = 30;
    TextView mCountDown, mPathInfo;

    private CountDownTimer countDownTimer;
    private long mTimeLeftMillis;
    long check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initial_stop = getArguments().getString("initial");
        final_stop = getArguments().getString("final");
        total_time = getArguments().getFloat("avgTime");

        mTimeLeftMillis = (long) total_time * 60 * 1000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_time, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mCountDown = view.findViewById(R.id.tv_time_taken_countdown);
        mPathInfo = view.findViewById(R.id.tv_path_info1);

        startTimer();

        mPathInfo.setText("You are going from \n" + initial_stop + "\nto\n" + final_stop);

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
               /* long check = millisUntilFinished;
                int hour = (int) check / (1000 * 60 * 60);
                check = (int) (check - (hour * 1000 * 60 * 60));
                int min = (int) check / (1000 * 60);
                check = (int) (check - (min * 1000 * 60));
                int sec = (int) check / (1000);
                check = (int) (check - sec * 1000);
                int centi = (int) check / (100);

                mCountDown.setText(hour + " : " + min + " : " + sec);*/

                //mCountDown.setText(hour + " : " + min + " : " + sec + " : " + centi);

                check = millisUntilFinished;
                updateCountDownText();
                mTimeLeftMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                mCountDown.setText("Reached");
            }
        }.start();
    }
    private void updateCountDownText() {
        long hour = TimeUnit.MILLISECONDS.toHours(check);
        check = check - TimeUnit.HOURS.toMillis(hour);
        long min = TimeUnit.MILLISECONDS.toMinutes(check);
        check = check - TimeUnit.MINUTES.toMillis(min);
        long sec = TimeUnit.MILLISECONDS.toSeconds(check);

        String timeleftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, sec);

        mCountDown.setText(timeleftFormatted);
    }
    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

}
