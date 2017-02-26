package com.blundell.tut;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String BUZZER_PIN = "PWM1";

    private Pwm bus;
    private Handler buzzerSongHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeripheralManagerService service = new PeripheralManagerService();
        try {
            bus = service.openPwm(BUZZER_PIN);
        } catch (IOException e) {
            throw new IllegalStateException(BUZZER_PIN + " bus cannot be opened.", e);
        }

        try {
            bus.setPwmDutyCycle(50);
        } catch (IOException e) {
            throw new IllegalStateException(BUZZER_PIN + " bus cannot be configured.", e);
        }

        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        buzzerSongHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        buzzerSongHandler.post(play);
    }

    private final Runnable play = new Runnable() {
        @Override
        public void run() {
            try {
                bus.setPwmFrequencyHz(333);
                bus.setEnabled(true);
                SystemClock.sleep(250);
                bus.setEnabled(false);
            } catch (IOException e) {
                throw new IllegalStateException(BUZZER_PIN + " bus cannot play note.", e);
            }
            buzzerSongHandler.post(this);
        }
    };

    @Override
    protected void onStop() {
        buzzerSongHandler.removeCallbacks(play);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            bus.close();
        } catch (IOException e) {
            Log.e("TUT", BUZZER_PIN + " bus cannot be closed, you may experience errors on next launch.", e);
        }
        super.onDestroy();
    }
}
