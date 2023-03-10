package com.m4n0.myapplication.a2023app.code;

public class MultProvider {
// class is data + function
// this holds two ints
// this gives multiplication results of two ints
// this stores the result of the multiplication

    private int a;
    private int b;
    private int result;



    public MultProvider() {
        this.a = (int) (Math.random() * 10) + 2;
        this.b = (int) (Math.random() * 10) + 2;
        this.result = a * b;
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
}
