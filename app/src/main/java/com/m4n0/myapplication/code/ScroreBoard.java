package com.m4n0.myapplication.a2023app.code;

public class ScroreBoard {

    private int score;
    private int streak = 1;
    private int changecounter;
    private int count;


    // print current score
    public void printScore() {
        System.out.println("score: " + score);
    }

    // increase scrore by var amount
    private void increaseScore(int amount) {
        System.out.println("you have gained " + amount + " for this answer");
        score += amount;
        streak++;
        changecounter++;
    }
    public void endStreak(){
        streak = 1;
        changecounter++;
    }
    
    // increase score based on streak every 5th answer gives x3 poitnts, every x10 gives x5 points, every 25 gives x10 points, every 100 gives x25 points
    // base score is 1 point untill 10, then 2 untill 25, then 3 untill 100
    public void increaseScoreBasedOnStreak() {
        if (changecounter < 10) {
            count = 10;
        } else if (changecounter < 25) {
            count = 20;
        } else{
            count = 30;
        }


        if (streak % 100 == 0) {
            increaseScore(count * 25);
        } else if (streak % 25 == 0) {
            increaseScore(count * 10);
        } else if (streak % 10 == 0) {
            increaseScore(count * 5);
        } else if (streak % 5 == 0) {
            increaseScore(count * 3);
        } else {
            increaseScore(count * 1);
        }
    }




    // clear score
    public void clearScore() {
        score = 0;
    }



}