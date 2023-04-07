package com.m4n0.myapplication.a2023app;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.m4n0.myapplication.code.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
// ^^ boilerplate

import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    //    my definitions
    private TextView problemText;
    private TextView answerWindow;
    private TextView scoreboardwindow;
    private int goodAnswer;
    public TimeTracker timerOne = new TimeTracker();

    List<Dataholder> holderOne = new ArrayList<>();




    public MultProvider multProvider = new MultProvider();



    private GridView mGridView;             // textlist
    private List<String> data;            // textlist


public int diff = 0;
    // my functions
    



    private int presentEquasion(int diff){
        multProvider.MultProvider(holderOne,diff);
        int changeCounterMax = 0;
        if (!holderOne.isEmpty()) {
            changeCounterMax = holderOne.get(holderOne.size()-1).getChangeCounter();
        }

        int changeCounter = changeCounterMax - 5;

        if (diff == 0){
            // training assistance algorith to apply on easy only
            if (changeCounter > 0) {
                if (holderOne.size() > changeCounter && holderOne.get(changeCounter).getReworkFlag()) {
                    System.out.println("rework trigered");
                    holderOne.get(changeCounter).setReworkFlag(false);
                    MyTuple<Integer, Integer> reworkTuple = new MyTuple<>(holderOne.get(changeCounter).getA(), holderOne.get(changeCounter).getB());
                    multProvider.reworkprovider(reworkTuple);
                }
            }
        }


        int num1 = multProvider.getA();
        int num2 = multProvider.getB();

        String problem = num1 + " x " + num2 + " = ";

        problemText.setText(problem);

        // returns num1 * num2

        int answer = multProvider.getResult();
        return answer;
    }


    //        \/\/ boilerplate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ^^ boilerplate
// below is main
        problemText = findViewById(R.id.problemwindow);
        answerWindow = findViewById(R.id.answerwindow);
        goodAnswer = presentEquasion(diff);
        ScroreBoard scoreboard = new ScroreBoard();
        scoreboardwindow = findViewById(R.id.scoreWindow);
// adding holder one to history

//        adding textview list
        mGridView = findViewById(R.id.gridView);
        CustomAdapter adapter = new CustomAdapter(this, holderOne);
//        mGridView.setNumColumns(8);
        mGridView.setAdapter(adapter);

//        adding textview list close


        GridLayout numberPad = findViewById(R.id.number_pad);
        int childCount = numberPad.getChildCount();


        for (int i = 0; i < childCount; i++) {
            View view = numberPad.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                String number = button.getText().toString();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentAnswer = answerWindow.getText().toString();
                        if (currentAnswer.length() < 6){
                            answerWindow.setText(currentAnswer + number);
                            }else{
                                answerWindow.setText(currentAnswer);
                            }
                        
                    }
                });
            }
        }

        Button btnBack = findViewById(R.id.btnBack);
        Button btnEnter = findViewById(R.id.btnEnter);

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleButton);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    
                    switch (checkedId) {
                        case R.id.button1:
                            diff = 0;
                            break;
                        case R.id.button2:
                            diff = 1;
                            break;
                        case R.id.button3:
                            diff = 2;
                    }
                }
            }
        });;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentAnswer = answerWindow.getText().toString();
                if (currentAnswer.length() > 0) {
                    answerWindow.setText(currentAnswer.substring(0, currentAnswer.length() - 1));
                }
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if currentAnswer is equal to goodAnswer
                String currentAnswer = answerWindow.getText().toString();
                if (currentAnswer.length() > 0) {
                    int currentAnswerInt = Integer.parseInt(currentAnswer);
                    if (currentAnswerInt == goodAnswer) {
                        timerOne.endTimer();
                        long time = timerOne.getTimeElapsed();
                        // adding history
                        Dataholder currentMult = new Dataholder(scoreboard.getChangecounter(), multProvider.getA(), multProvider.getB(), multProvider.getFirstTimeAnswer(), time < 5000);
                        if (!multProvider.getFirstTimeAnswer()|| time > 5000){
                            currentMult.setReworkFlag(true);
                        }
                        System.out.println(currentMult.printItem());
                        holderOne.add(currentMult);
                        multProvider.setFirstTimeAnswer(true);  // reseting first time answer
                        timerOne.startTimer();
                        scoreboard.increaseScoreBasedOnStreak();
                        scoreboardwindow.setText(scoreboard.getScore());
                        Toast toast = Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 100);
                        toast.show();
                        goodAnswer = presentEquasion(diff); // present equasion local method to display a new mult
                        answerWindow.setText("");

                        mGridView.setAdapter(adapter);

                    } else {
                        multProvider.setFirstTimeAnswer(false);
                        scoreboard.endStreak();
                        Toast toast = Toast.makeText(MainActivity.this, "Incorrect!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 100);
                        toast.show();
                        answerWindow.setText("");
                    }
                }


            }
        });
    }
}