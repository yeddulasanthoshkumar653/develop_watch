package com.dv19;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    boolean running;
    long startTime, elapsedTime, pauseTime;
    boolean wasRunning;
    Handler handler;
    TextView time;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.timetext);
        handler = new Handler();
        animationView = findViewById(R.id.animationView2);
        animationView.pauseAnimation();
    }

    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
            handler.post(runnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    public void onStart(View view) {
        if (pauseTime == 0) {
            startTime = SystemClock.elapsedRealtime();
        } else {
            startTime += (SystemClock.elapsedRealtime() - pauseTime);
        }
        running = true;
        handler.post(runnable);
        animationView.playAnimation();
    }

    public void onReset(View view) {
        stopTimer();
        startTime = 0;
        elapsedTime = 0;
        pauseTime = 0;
        String times = String.format(Locale.getDefault(), "%02d:%02d:%02d", 0, 0, 0);
        time.setText(times);
        animationView.pauseAnimation();
        animationView.setProgress(0);
    }

    public void onStop(View view) {
        running = false;
        pauseTime = SystemClock.elapsedRealtime();
        elapsedTime += pauseTime - startTime;
        handler.removeCallbacks(runnable);
        animationView.pauseAnimation();
    }

    private void stopTimer() {
        running = false;
        handler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            int minutes = (int) (elapsedTime / 60000);
            int seconds = (int) (elapsedTime / 1000) % 60;
            int milliseconds = (int) (elapsedTime % 1000 / 10);

            String times = String.format(Locale.getDefault(),"%02d:%02d:%02d", minutes, seconds, milliseconds);
            time.setText(times);
            if (running) {
                handler.postDelayed(this, 10);
            }
        }
    };
}