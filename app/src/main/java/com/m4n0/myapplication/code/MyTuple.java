package com.m4n0.myapplication.a2023app.code;

public class MyTuple<A, B> {
    private final A a;
    private final B b;

    public MyTuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
