package com.m4n0.myapplication.code;

import android.widget.Button;

import com.m4n0.myapplication.a2023app.R;

import java.lang.Integer;
import java.lang.String;


public class ScroreBoard {

    private int score;
    private int streak = 1;
    private int changecounter;
    private int count;



    public String getScore() {
        return String.valueOf(score);
    }

    // print current score
    public void printScore() {
        System.out.println("score: " + score);
    }

    // increase scrore by var amount
    private void increaseScore(int amount) {
        System.out.println("SCORE: "+ score +" + " + amount);
        score += amount;
        streak++;
        changecounter++;
    }

    public int getChangecounter() {
        return changecounter;
    }

    public void endStreak(){
        streak = 1;
        changecounter++;
    }
    
    // increase score based on streak every 5th answer gives x3 poitnts, every x10 gives x5 points, every 25 gives x10 points, every 100 gives x25 points
    // base score is 1 point untill 10, then 2 untill 25, then 3 untill 100
    public void increaseScoreBasedOnStreak(int diff) {
        int mult =1 
        if (diff ==1 ){
            int mult= 4;
        }
        if (diff ==2 ){
            int mult =10;
        }
        if (changecounter < 10) {
            count = 10;
        } else if (changecounter < 25) {
            count = 20;
        } else{
            count = 30;
        }


        if (streak % 100 == 0) {
            increaseScore(count * mult * 25);
        } else if (streak % 25 == 0) {
            increaseScore(count * mult * 10);
        } else if (streak % 10 == 0) {
            increaseScore(count * mult * 5);
        } else if (streak % 5 == 0) {
            increaseScore(count * mult * 3);
        } else {
            increaseScore(count * mult * 1);
        }
    }




    // clear score
    public void clearScore() {
        score = 0;

    }



}


