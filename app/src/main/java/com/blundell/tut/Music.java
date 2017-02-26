package com.blundell.tut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

interface Music {
    Duration EIGTH = new Duration(0.125);
    Duration QUARTER = new Duration(0.25);
    Duration HALF = new Duration(0.5);
    Duration WHOLE = new Duration(1);

    Frequency C4 = new Frequency(261.63);
    Frequency E4 = new Frequency(329.63);
    Frequency F4 = new Frequency(349.23);
    Frequency G4 = new Frequency(392.00);
    Frequency A4 = new Frequency(440.00);
    Frequency A4_SHARP = new Frequency(466.16);

    Note E4_Q = new FrequencyNote(QUARTER, E4);
    Note F4_Q = new FrequencyNote(QUARTER, F4);
    Note G4_Q = new FrequencyNote(QUARTER, G4);
    Note G4_H = new FrequencyNote(HALF, G4);
    Note A4_Q = new FrequencyNote(QUARTER, A4);
    Note A4_Q_SHARP = new FrequencyNote(QUARTER, A4_SHARP);
    Note A4_E = new FrequencyNote(EIGTH, A4);
    Note A4_W = new FrequencyNote(WHOLE, A4);
    Note C4_Q = new FrequencyNote(QUARTER, C4);
    Note R_E = new RestNote(EIGTH);
    Note R_H = new RestNote(HALF);

    /**
     * http://www.musicnotes.com/sheetmusic/mtd.asp?ppn=MN0145355
     */
    List<Note> POKEMON_ANIME_THEME = new ArrayList<>(Arrays.asList(
        R_E, A4_E, A4_E, A4_E, A4_Q, A4_Q,
        G4_Q, E4_Q, C4_Q, C4_Q,
        A4_Q, A4_Q, G4_Q, F4_Q,
        G4_H, R_H,
        F4_Q, A4_Q_SHARP, A4_Q_SHARP, A4_Q_SHARP,
        A4_Q, G4_Q, F4_Q, F4_Q,
        A4_Q, A4_Q, G4_Q, F4_Q,
        A4_W
    ));

    class Duration {
        private static final long TEMPO = TimeUnit.MILLISECONDS.toMillis(1500);

        private final double beatPercent;

        private Duration(double beatPercent) {
            this.beatPercent = beatPercent;
        }

        long asPeriod() {
            return (long) (TEMPO * beatPercent);
        }

    }

    class Frequency {

        private final double frequency;

        private Frequency(double frequency) {
            this.frequency = frequency;
        }

        double getFrequency() {
            return frequency;
        }
    }

    interface Note {
        long getPeriod();

        double getFrequency();

        boolean isRest();
    }

    class FrequencyNote implements Note {
        private final Duration duration;
        private final Frequency frequency;

        private FrequencyNote(Duration duration, Frequency frequency) {
            this.duration = duration;
            this.frequency = frequency;
        }

        @Override
        public long getPeriod() {
            return duration.asPeriod();
        }

        @Override
        public double getFrequency() {
            return frequency.getFrequency();
        }

        @Override
        public boolean isRest() {
            return false;
        }
    }

    class RestNote implements Note {
        private final Duration duration;

        private RestNote(Duration duration) {
            this.duration = duration;
        }

        @Override
        public long getPeriod() {
            return duration.asPeriod();
        }

        @Override
        public double getFrequency() {
            throw new IllegalStateException("Rest notes do not have a frequency");
        }

        @Override
        public boolean isRest() {
            return true;
        }
    }
}
