package com.m4n0.myapplication.code;

public class TimeTracker {

    // this class is used to track the time it takes to answer an equation

    private long startTime;
    private long endTime;
    private long timeElapsed;

    public TimeTracker() {
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.timeElapsed = 0;
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void endTimer() {
        this.endTime = System.currentTimeMillis();
        this.timeElapsed = endTime - startTime;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public String getTimeElapsedString() {
        return String.valueOf(timeElapsed);
    }

}
