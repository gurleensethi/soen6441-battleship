package com.soen6441.battleship.utils;

import java.util.Date;

public class TimerUtil {
    private boolean isStarted = false;
    private long startTime;

    public void start() {
        if (isStarted) {
            throw new RuntimeException("Timer already started!");
        }

        isStarted = true;
        startTime = new Date().getTime();
    }

    public long stop() {
        if (!isStarted) {
            throw new RuntimeException("Timer has not been started!");
        }

        isStarted = false;
        return new Date().getTime() - startTime;
    }
}
