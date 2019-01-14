package com.blundell.tut;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class MainActivity extends Activity {

    private static final Queue<Music.Note> SONG = new ArrayDeque<>();

    private Pwm bus;
    private Handler buzzerSongHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager service = PeripheralManager.getInstance();
        try {
            bus = service.openPwm(BoardConfig.getBuzzerPin());
        } catch (IOException e) {
            throw new IllegalStateException(BoardConfig.getBuzzerPin() + " bus cannot be opened.", e);
        }

        try {
            bus.setPwmDutyCycle(50);
        } catch (IOException e) {
            throw new IllegalStateException(BoardConfig.getBuzzerPin() + " bus cannot be configured.", e);
        }

        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        buzzerSongHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        SONG.addAll(Music.POKEMON_ANIME_THEME);
        buzzerSongHandler.post(playSong);
    }

    private final Runnable playSong = new Runnable() {
        @Override
        public void run() {
            if (SONG.isEmpty()) {
                return;
            }

            Music.Note note = SONG.poll();

            if (note.isRest()) {
                SystemClock.sleep(note.getPeriod());
            } else {
                try {
                    bus.setPwmFrequencyHz(note.getFrequency());
                    bus.setEnabled(true);
                    SystemClock.sleep(note.getPeriod());
                    bus.setEnabled(false);
                } catch (IOException e) {
                    throw new IllegalStateException(BoardConfig.getBuzzerPin() + " bus cannot play note.", e);
                }
            }
            buzzerSongHandler.post(this);
        }
    };

    @Override
    protected void onStop() {
        buzzerSongHandler.removeCallbacks(playSong);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            bus.close();
        } catch (IOException e) {
            Log.e("TUT", BoardConfig.getBuzzerPin() + " bus cannot be closed, you may experience errors on next launch.", e);
        }
        super.onDestroy();
    }
}
