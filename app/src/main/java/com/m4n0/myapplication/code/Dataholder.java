package com.m4n0.myapplication.a2023app.code;

public class Dataholder {
    private int a;
    private int b;
    private boolean correct;
    private boolean time;

//    number1, number2, true if correct, true if in time

    public Dataholder(int a, int b, boolean correct, boolean time) {
        this.a = a;
        this.b = b;
        this.correct = correct;
        this.time = time;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isTime() {
        return time;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    // print out the list
    public void printItem() {
        System.out.println(a + "," + b + "," + correct + "," + time);
    }
}