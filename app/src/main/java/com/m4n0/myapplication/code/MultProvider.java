package com.m4n0.myapplication.code;

import java.util.List;

public class MultProvider {
// class is data + function
// this holds two ints
// this gives multiplication results of two ints
// this stores the result of the multiplication

    private int a;
    private int b;
    private int result;
    private boolean firstTimeAnswer;

    public MultProvider() {
        this.firstTimeAnswer = true;
    }

    public void reworkprovider(MyTuple<Integer, Integer> tuple){
        this.a = tuple.getA();
        this.b = tuple.getB();
        this.result = a * b;
    }


    public void MultProvider(List<Dataholder> holderOne, int diff) {
        // Initialize variables
        MyTuple<Integer, Integer> tuple = CheckForRecentRepeats(holderOne);
        int acompare = tuple.getA();
        int bcompare = tuple.getB();
        int num1, num2;



        while (true) {

            num1 = 0;
            num2 = 0;

            if (diff == 0) {
                num1 = (int) (Math.random() * 10) + 2;
                num2 = (int) (Math.random() * 10) + 2;
            }

            if (diff == 1) {
                num1 = (int) (Math.random() * 10) + 2;
                num2 = (int) (Math.random() * 90) + 11;
            }

            if (diff == 2) {
                num1 = (int) (Math.random() * 90) + 11;
                num2 = (int) (Math.random() * 90) + 11;
            }


            // Sort the numbers so that a is always the smaller number
            if (num1 > num2) {
                this.b = num1;
                this.a = num2;
            } else {
                this.a = num1;
                this.b = num2;
            }

            // Calculate the result
            this.result = a * b;

            // Compare with the previous result and regenerate if there is a repeat
            if (acompare != a || bcompare != b) {
                break;
            }
        }
    }




    public String showequasion() {
        return a + " * " + b + " = ";
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getResult() {
        return result;
    }

    public MyTuple<Integer, Integer>  CheckForRecentRepeats(List<Dataholder> list){
        // if list longer than 0
        if (list.size() == 0){
            return new MyTuple<>(0,0);
        }
        int recent = list.get(list.size() - 1).getA();
        int recent2 = list.get(list.size() - 1).getB();
        return new MyTuple<>(recent,recent2);
    };

    public void setFirstTimeAnswer(boolean firstTimeAnswer) {
        this.firstTimeAnswer = firstTimeAnswer;
    }

    public boolean getFirstTimeAnswer() {
        return this.firstTimeAnswer;
    }
}
