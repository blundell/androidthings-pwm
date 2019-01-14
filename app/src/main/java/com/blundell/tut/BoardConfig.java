package com.blundell.tut;

import android.os.Build;

@SuppressWarnings("WeakerAccess")
public class BoardConfig {
    private static final String DEVICE_RPI3 = "rpi3";
    private static final String DEVICE_IMX7D_PICO = "imx7d_pico";

    /**
     * Return the RainbowHat piezo buzzer's PWM device.
     * https://github.com/androidthings/contrib-drivers/blob/master/rainbowhat/src/main/java/com/google/android/things/contrib/driver/rainbowhat/RainbowHat.java
     */
    public static String getBuzzerPin() {
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                return "PWM1";
            case DEVICE_IMX7D_PICO:
                return "PWM2";
            default:
                return "PWM1";
        }
    }
}
