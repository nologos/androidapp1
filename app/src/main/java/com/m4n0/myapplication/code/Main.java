package com.m4n0.myapplication.a2023app.code;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) {

        System.out.println("App is running");
        

        Scanner scanner = new Scanner(System.in);
        ScroreBoard scoreboard = new ScroreBoard();
        List<Dataholder> holderOne = new ArrayList<>();
        MultProvider multProvider = new MultProvider();

        while (1 == 1) {
            // scoreboard.printScore();
            multProvider.MultProvider(holderOne);
            
            //check if prievious 5 on the list was incorrect

            int changeCounterMax = 0;
            if (!holderOne.isEmpty()) {
                changeCounterMax = holderOne.get(holderOne.size()-1).getChangeCounter();
            }

            int changeCounter = changeCounterMax - 5;
            if (changeCounter > 0) {
                if (holderOne.get(changeCounter).getReworkFlag()){
                    System.out.println("rework trigered");
                    holderOne.get(changeCounter).setReworkFlag(false);
                    MyTuple<Integer, Integer> reworkTuple = new MyTuple<>(holderOne.get(changeCounter).getA(), holderOne.get(changeCounter).getB());
                    multProvider.reworkprovider(reworkTuple);
                }
            }


            // System.out.println("Enter result for the equation or type 00 to exit");
            System.out.println(multProvider.showequasion());
            // time the amount of time required to answer
            long startTime = System.currentTimeMillis();
            int input = scanner.nextInt();

            if (00 == input) {
                System.out.println("final score");
                scoreboard.printScore();
                System.out.println("exit");
                break;
            }

            if (input == multProvider.getResult()) {
                System.out.println("Correct");
                scoreboard.increaseScoreBasedOnStreak();
            } else {
                System.out.println("Wrong");
                scoreboard.endStreak();
            }

            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            Dataholder currentMult = new Dataholder(scoreboard.getChangecounter(), multProvider.getA(), multProvider.getB(), input == multProvider.getResult(), timeElapsed < 5000);
            if (input == multProvider.getResult() || timeElapsed < 5000 ){
                currentMult.setReworkFlag(false);
            }
            holderOne.add(currentMult);


        }
        scanner.close();
        scoreboard.clearScore();
        // print out the list
        for (Dataholder dataholder : holderOne) {
            dataholder.printItem();
        }
    }
}
