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


        while (1 == 1) {
            scoreboard.printScore();
            MultProvider multProvider = new MultProvider();

            System.out.println("Enter result for the equation or type 00 to exit");
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
            Dataholder currentMult = new Dataholder(multProvider.getA(), multProvider.getB(), input == multProvider.getResult(), timeElapsed < 5000);
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