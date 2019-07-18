package com.soen6441.battleship.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
    private boolean isRunning = false;
    private long startTime;
    private long stopTime;
    private BehaviorSubject<Long> timerListener = BehaviorSubject.create();

    public static String printableTime(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long minutes = (milliseconds / 1000) / 60;
        totalSeconds -= minutes * 60;
        return String.format("%02d:%02d", minutes, totalSeconds);
    }

    public TimerUtil() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    timerListener.onNext(new Date().getTime() - startTime);
                } else {
                    timerListener.onNext(TimerUtil.this.stopTime - TimerUtil.this.startTime);
                }
            }
        }, 1000, 1000);
    }

    public void start() {
        if (isRunning) {
            throw new RuntimeException("Timer already started!");
        }

        isRunning = true;
        startTime = new Date().getTime();
    }

    public long stop() {
        if (!isRunning) {
            throw new RuntimeException("Timer has not been started!");
        }

        isRunning = false;
        stopTime = new Date().getTime();
        return stopTime - startTime;
    }

    public void reset() {
        this.isRunning = false;
        this.startTime = 0;
        this.stopTime = 0;
    }

    public Observable<Long> asObservable() {
        return this.timerListener;
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
