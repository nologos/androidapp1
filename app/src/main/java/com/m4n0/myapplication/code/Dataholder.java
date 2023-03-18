package com.m4n0.myapplication.code;

public class Dataholder {
    private int changeCounter;
    private int a;
    private int b;
    private boolean correct;
    private boolean time;
    private boolean reworkFlag;

//    number1, number2, true if correct, true if in time

    public Dataholder(int changeCounter, int a, int b, boolean correct, boolean time) {
        this.changeCounter = changeCounter;
        this.a = a;
        this.b = b;
        this.correct = correct;
        this.time = time;
        this.reworkFlag = reworkFlag;
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

    public String printHistory(){
        return a + "x" + b;
    }

    public String printItem() {
        return "Dataholder{" +
                "changeCounter=" + changeCounter +
                ", a=" + a +
                ", b=" + b +
                ", correct=" + correct +
                ", time=" + time +
                ", reworkFlag=" + reworkFlag +
                '}';
    }

    public int getChangeCounter() {
        return changeCounter;
    }

    public boolean getReworkFlag() {
        return reworkFlag;
    }

    public void setReworkFlag(boolean reworkFlag) {
        this.reworkFlag = reworkFlag;
    }

    
}